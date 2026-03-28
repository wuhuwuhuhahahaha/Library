package cn.whpu.library.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * 生成 JWT Token
     * @param username 用户名
     * @param claims 自定义声明
     * @return JWT Token
     */
    public String generateToken(String username, Map<String, Object> claims) {
        SecretKey key = generateKey();
        
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    /**
     * 解析 JWT Token
     * @param token JWT Token
     * @return Claims 对象
     */
    public Claims parseToken(String token) {
        SecretKey key = generateKey();
        
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 Token 中提取用户名
     * @param token JWT Token
     * @return 用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从 Token 中提取指定声明
     * @param token JWT Token
     * @param claimsResolver 声明提取函数
     * @return 声明值
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 验证 Token 是否有效
     * @param token JWT Token
     * @param username 用户名
     * @return 是否有效
     */
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * 检查 Token 是否过期
     * @param token JWT Token
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * 生成密钥
     * @return SecretKey
     */
    private SecretKey generateKey() {
        return new SecretKeySpec(
            jwtSecret.getBytes(StandardCharsets.UTF_8),
            "HmacSHA256"
        );
    }
}
