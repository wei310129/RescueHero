package tw.com.aidenmade.rescuehero.configuration.config;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebClientConfig {

    /**
     * 全域共享連線池 — 所有 WebClient 共用，避免各自無限制建立連線
     *
     * - maxConnections(500)：TCP 連線上限
     * - maxIdleTime(30s)：閒置超過 30s 回收
     * - maxLifeTime(5m)：連線存活上限，防止 TCP 長連線老化問題
     * - pendingAcquireTimeout(5s)：等待可用連線的最長時間，超過拋 PoolAcquireTimeoutException
     * - pendingAcquireMaxCount(1000)：等待佇列上限
     * - evictInBackground(60s)：背景週期性清理過期連線
     */
    @Bean
    public ConnectionProvider sharedConnectionProvider() {
        return ConnectionProvider.builder("rescue-hero-shared-pool")
                .maxConnections(500)
                .maxIdleTime(Duration.ofSeconds(30))
                .maxLifeTime(Duration.ofMinutes(5))
                .pendingAcquireTimeout(Duration.ofSeconds(5))
                .pendingAcquireMaxCount(1000)
                .evictInBackground(Duration.ofSeconds(60))
                .build();
    }

    /**
     * HTTP/1.1 WebClient — 對應不支援 H2 的外部服務
     *
     * - CONNECT_TIMEOUT_MILLIS(3s)：TCP 三次握手超時
     * - responseTimeout(5s)：從發送請求到收到完整 response 的整體超時
     * - ReadTimeoutHandler(5s)：Netty channel level 讀取超時（防止對方無限期不回應）
     * - WriteTimeoutHandler(5s)：Netty channel level 寫入超時
     *
     * responseTimeout 與 ReadTimeoutHandler 的差別：
     *   responseTimeout 是 Reactor Netty 在 request/response 層的計時，
     *   ReadTimeoutHandler 是 Netty ChannelHandler 層的 idle 計時，兩者互補。
     */
    @Bean("http1WebClient")
    public WebClient http1WebClient(ConnectionProvider sharedConnectionProvider) {
        HttpClient httpClient = HttpClient.create(sharedConnectionProvider)
                .protocol(HttpProtocol.HTTP11)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS))
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(loggingFilter())
                .build();
    }

    /**
     * HTTP/2 WebClient — 對應支援 H2/ALPN 的外部 HTTPS 服務
     *
     * 透過 .secure() 開啟 TLS，Reactor Netty 會自動以 ALPN 協商 HTTP/2。
     * 若目標為內部明文 h2c 服務，改用 HttpProtocol.H2C 並移除 .secure()。
     *
     * HTTP/2 多工特性讓單條 TCP 連線能並發多個 stream，
     * 因此連線池數量可比 HTTP/1.1 設定得更小。
     */
    @Bean("http2WebClient")
    public WebClient http2WebClient(ConnectionProvider sharedConnectionProvider) {
        HttpClient httpClient = HttpClient.create(sharedConnectionProvider)
                .protocol(HttpProtocol.H2)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                .responseTimeout(Duration.ofSeconds(5))
                .secure(); // TLS + ALPN，讓 Netty 自動協商 H2

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(loggingFilter())
                .build();
    }

    /**
     * Resilience4j RateLimiter — 外部 API 呼叫頻率限制
     *
     * - limitForPeriod(100)：每個 limitRefreshPeriod 內允許 100 次呼叫
     * - limitRefreshPeriod(1s)：每秒重置一次計數器
     * - timeoutDuration(0)：非阻塞模式，超過立即拋出 RequestNotPermitted，不等待
     */
    @Bean("externalApiRateLimiter")
    public RateLimiter externalApiRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(100)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ZERO)
                .build();
        return RateLimiterRegistry.ofDefaults()
                .rateLimiter("externalApi", config);
    }

    /**
     * Resilience4j Semaphore Bulkhead — 外部 API 並發上限
     *
     * 選用 Semaphore Bulkhead（而非 ThreadPool Bulkhead）的原因：
     *   Semaphore Bulkhead 在 Reactor 鏈中以 tryAcquire/release 方式運作，
     *   不會佔用額外執行緒，完全非阻塞，適合 WebFlux 環境。
     *
     * - maxConcurrentCalls(20)：同時進行中的呼叫上限
     * - maxWaitDuration(0)：非阻塞，艙壁滿即拋出 BulkheadFullException
     */
    @Bean("externalApiBulkhead")
    public Bulkhead externalApiBulkhead() {
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(20)
                .maxWaitDuration(Duration.ZERO)
                .build();
        return BulkheadRegistry.ofDefaults()
                .bulkhead("externalApi", config);
    }

    private ExchangeFilterFunction loggingFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.debug("[WebClient] {} {}", request.method(), request.url());
            return Mono.just(request);
        });
    }
}
