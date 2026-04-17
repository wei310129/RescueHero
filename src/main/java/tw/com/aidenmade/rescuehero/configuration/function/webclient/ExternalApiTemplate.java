package tw.com.aidenmade.rescuehero.configuration.function.webclient;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.reactor.bulkhead.operator.BulkheadOperator;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 外部 API 呼叫抽象模板
 *
 * 子類別繼承後只需：
 *   1. 在 constructor 傳入 baseUrl 呼叫 super()
 *   2. 實作 {@link #fallback()} 定義降級回傳值
 *   3. 呼叫 {@code callHttp1} / {@code callHttp2} / {@code callBatch} 發出請求
 *
 * 已內建以下機制：
 *   - 非阻塞 I/O：全程 Reactor 鏈，timeout 由 HttpClient 層處理
 *   - onStatus：4xx / 5xx 轉換成明確的 WebClientResponseException
 *   - onError 降級：RateLimiter 超限、Bulkhead 滿、其他例外均導向 fallback()
 *   - RateLimiter：每秒 100 次上限，超過立即拒絕（非阻塞）
 *   - Bulkhead：同時 20 個並發，滿即拒絕（非阻塞 Semaphore）
 *   - flatMap 並發 = bulkhead.maxConcurrentCalls，兩層對齊避免 back-pressure 不一致
 *
 * Resilience4j operator 順序說明：
 *   .transformDeferred(RateLimiterOperator) 掛在外層（subscription 時最先檢查），
 *   .transformDeferred(BulkheadOperator)    掛在內層（RateLimiter 通過後再檢查）。
 *   此順序等同：先確認「本秒還有配額」，再確認「目前有艙位」，符合直覺語意。
 */
@Slf4j
public abstract class ExternalApiTemplate<T> {

    protected final WebClient http1WebClient;
    protected final WebClient http2WebClient;
    protected final RateLimiter rateLimiter;
    protected final Bulkhead bulkhead;

    protected ExternalApiTemplate(
            @Qualifier("http1WebClient") WebClient http1WebClient,
            @Qualifier("http2WebClient") WebClient http2WebClient,
            @Qualifier("externalApiRateLimiter") RateLimiter rateLimiter,
            @Qualifier("externalApiBulkhead") Bulkhead bulkhead) {
        this.http1WebClient = http1WebClient;
        this.http2WebClient = http2WebClient;
        this.rateLimiter = rateLimiter;
        this.bulkhead = bulkhead;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HTTP/1.1 單筆呼叫
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * HTTP/1.1 GET — 含 RateLimiter + Bulkhead + onStatus + fallback
     *
     * @param uri          完整 URI 或相對路徑（配合 baseUrl）
     * @param responseType 回傳型別的 Class
     */
    protected Mono<T> getHttp1(String uri, Class<T> responseType) {
        return http1WebClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new WebClientResponseException(
                                        response.statusCode().value(),
                                        "4xx Client Error: " + body,
                                        response.headers().asHttpHeaders(), null, null)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new WebClientResponseException(
                                        response.statusCode().value(),
                                        "5xx Server Error: " + body,
                                        response.headers().asHttpHeaders(), null, null)))
                )
                .bodyToMono(responseType)
                // 先套 RateLimiter（外層，subscription 時最先觸發）
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                // 再套 Bulkhead（內層，RateLimiter 通過後才進入）
                .transformDeferred(BulkheadOperator.of(bulkhead))
                .onErrorResume(RequestNotPermitted.class, e -> {
                    log.warn("[HTTP1] Rate limit exceeded, uri={}, applying fallback", uri);
                    return fallback();
                })
                .onErrorResume(BulkheadFullException.class, e -> {
                    log.warn("[HTTP1] Bulkhead full, uri={}, applying fallback", uri);
                    return fallback();
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("[HTTP1] HTTP error status={}, uri={}", e.getStatusCode(), uri);
                    return fallback();
                })
                .onErrorResume(e -> {
                    log.error("[HTTP1] Unexpected error, uri={}", uri, e);
                    return fallback();
                });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HTTP/2 單筆呼叫
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * HTTP/2 GET — 結構與 getHttp1 相同，使用支援 H2/ALPN 的 client
     *
     * 適用對象：支援 HTTPS + HTTP/2 的外部服務（Google API、AWS 服務端等）
     */
    protected Mono<T> getHttp2(String uri, Class<T> responseType) {
        return http2WebClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new WebClientResponseException(
                                        response.statusCode().value(),
                                        "4xx Client Error: " + body,
                                        response.headers().asHttpHeaders(), null, null)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new WebClientResponseException(
                                        response.statusCode().value(),
                                        "5xx Server Error: " + body,
                                        response.headers().asHttpHeaders(), null, null)))
                )
                .bodyToMono(responseType)
                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .transformDeferred(BulkheadOperator.of(bulkhead))
                .onErrorResume(RequestNotPermitted.class, e -> {
                    log.warn("[HTTP2] Rate limit exceeded, uri={}, applying fallback", uri);
                    return fallback();
                })
                .onErrorResume(BulkheadFullException.class, e -> {
                    log.warn("[HTTP2] Bulkhead full, uri={}, applying fallback", uri);
                    return fallback();
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("[HTTP2] HTTP error status={}, uri={}", e.getStatusCode(), uri);
                    return fallback();
                })
                .onErrorResume(e -> {
                    log.error("[HTTP2] Unexpected error, uri={}", uri, e);
                    return fallback();
                });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 批次並發呼叫（flatMap + Bulkhead）
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * HTTP/1.1 批次並發 GET
     *
     * 並發控制兩層對齊：
     *   - flatMap(concurrency) 限制「同時訂閱的內部 Mono 數量」（Reactor 層）
     *   - BulkheadOperator     限制「同時執行中的呼叫數」（Resilience4j 層）
     *
     * 兩層設定相同值（maxConcurrentCalls），確保 back-pressure 一致，
     * 避免 flatMap 允許 N 個並發但 Bulkhead 只放行 M 個造成的排隊堆積。
     *
     * 批次處理使用 Mono.empty() 跳過失敗項目，不中止整個 Flux。
     *
     * @param uris         URI 清單
     * @param responseType 回傳型別的 Class
     */
    protected Flux<T> getBatch(List<String> uris, Class<T> responseType) {
        int concurrency = bulkhead.getBulkheadConfig().getMaxConcurrentCalls();

        return Flux.fromIterable(uris)
                .flatMap(
                        uri -> http1WebClient.get()
                                .uri(uri)
                                .retrieve()
                                .onStatus(HttpStatusCode::is4xxClientError, response ->
                                        response.bodyToMono(String.class)
                                                .flatMap(body -> Mono.error(new WebClientResponseException(
                                                        response.statusCode().value(),
                                                        "4xx Client Error: " + body,
                                                        response.headers().asHttpHeaders(), null, null)))
                                )
                                .onStatus(HttpStatusCode::is5xxServerError, response ->
                                        Mono.error(new WebClientResponseException(
                                                response.statusCode().value(), "5xx Server Error",
                                                response.headers().asHttpHeaders(), null, null))
                                )
                                .bodyToMono(responseType)
                                // 批次中每個 Mono 各自掛 BulkheadOperator
                                // （批次不需要 RateLimiter，靠 flatMap concurrency 控速即可）
                                .transformDeferred(BulkheadOperator.of(bulkhead))
                                .onErrorResume(BulkheadFullException.class, e -> {
                                    log.warn("[Batch] Bulkhead full, uri={}, skipping item", uri);
                                    return Mono.empty();
                                })
                                .onErrorResume(e -> {
                                    log.warn("[Batch] Item failed, uri={}, skipping", uri, e);
                                    return Mono.empty();
                                }),
                        concurrency // flatMap 並發上限對齊 Bulkhead.maxConcurrentCalls
                );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 子類別必須實作
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 降級回退策略
     *
     * 可選擇：
     *   - 回傳快取值        → Mono.just(cachedValue)
     *   - 回傳空白預設物件  → Mono.just(T.empty())
     *   - 上拋業務例外      → Mono.error(new ServiceUnavailableException())
     *   - 靜默忽略          → Mono.empty()
     */
    protected abstract Mono<T> fallback();
}
