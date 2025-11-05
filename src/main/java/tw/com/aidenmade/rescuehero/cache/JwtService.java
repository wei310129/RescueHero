package tw.com.aidenmade.rescuehero.cache;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tw.com.aidenmade.rescuehero.definition.cache.CacheDefinition;
import tw.com.aidenmade.rescuehero.definition.cache.CacheName;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
        System.getProperty("JWT_SECRET").getBytes(StandardCharsets.UTF_8));


    private final CacheManager cacheManager;

    public boolean validateToken(String token, String cacheName) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!claims.getExpiration().after(new Date())) {
                return false;
            }

            Cache cache = cacheManager.getCache(cacheName);
            if (cache == null) {
                return false; // 快取不存在，視為無效
            }
            // 若有快取，要求 token 必須存在快取中（表示已登入/未被撤銷）
            String key = String.format("%s:%s:%s",
                    claims.get("id"), claims.getSubject(), claims.get("role"));
            return cache.get(key) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Cacheable(
            value = CacheName.JWT_ACCESS_TOKEN,
            key = "T(String).format('%s:%s:%s', #userId, #username, #roleUniquePattern)",
            unless = "#result == null"
    )
    public String generateAccessToken(@NonNull Long userId, @NonNull String username, @NonNull String roleUniquePattern) {
        return Jwts.builder()
                .claim("id", userId)
                .claim("role", roleUniquePattern)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + CacheDefinition.JWT_ACCESS_TOKEN.getTtlType().getMilliseconds()))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    @Cacheable(
            value = CacheName.JWT_REFRESH_TOKEN,
            key = "T(String).format('%s:%s:%s', #userId, #username, #roleUniquePattern)",
            unless = "#result == null"
    )
    public String generateRefreshToken(@NonNull Long userId, @NonNull String username, @NonNull String roleUniquePattern) {
        log.info("SECRET_KEY: {}",SECRET_KEY);
        return Jwts.builder()
                .claim("id", userId)
                .claim("role", roleUniquePattern)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + CacheDefinition.JWT_REFRESH_TOKEN.getTtlType().getMilliseconds()))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 登出，移除token
     */
    @CacheEvict(value = CacheName.JWT_ACCESS_TOKEN, key = "#token")
    public void invalidateAccessToken(String token) {
        // 透過 @CacheEvict 移除快取中的 token
    }

    /**
     * 登出，移除token
     */
    @CacheEvict(value = CacheName.JWT_REFRESH_TOKEN, key = "#token")
    public void invalidateRefreshToken(String token) {
        // 透過 @CacheEvict 移除快取中的 token
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id", Long.class);
    }

    public String getRoleUniquePatternFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }
}
