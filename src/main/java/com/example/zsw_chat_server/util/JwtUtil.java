package com.example.zsw_chat_server.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.example.zsw_chat_server.entity.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 安全密钥
	private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 1天

	// 生成 JWT 令牌
	public String generateToken(User user) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", user.getEmail()); // 主题（用户标识）
		claims.put("created", now);

		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expiration)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	// 校验 JWT 令牌
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 解析 JWT 内容
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
}
