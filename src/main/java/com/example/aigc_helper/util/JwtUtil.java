package com.example.aigc_helper.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

  // JWT密钥
  private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  // JWT过期时间：12小时
  private final long JWT_EXPIRATION = 12 * 60 * 60 * 1000L;

  /**
   * 生成JWT令牌
   * 
   * @param userId   用户ID
   * @param username 用户名
   * @return JWT令牌
   */
  public String generateToken(Long userId, String username) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId);
    claims.put("username", username);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * 从JWT令牌中获取用户名
   * 
   * @param token JWT令牌
   * @return 用户名
   */
  public String getUsernameFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.getSubject();
  }

  /**
   * 从JWT令牌中获取用户ID
   * 
   * @param token JWT令牌
   * @return 用户ID
   */
  public Long getUserIdFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return Long.valueOf(claims.get("userId").toString());
  }

  /**
   * 验证JWT令牌是否有效
   * 
   * @param token JWT令牌
   * @return 是否有效
   */
  public Boolean validateToken(String token) {
    try {
      Claims claims = getClaimsFromToken(token);
      return !claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 从JWT令牌中获取Claims
   * 
   * @param token JWT令牌
   * @return Claims
   */
  private Claims getClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}