package mango.rentalsystem.global.security;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.member.domain.MemberRole;
import mango.rentalsystem.global.properties.JwtProperties;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final JwtProperties jwtProperties;
	private final RedisTemplate<String, String> redisTemplate;

	private Claims createBaseClaims(Long expTime) {
		final Date now = new Date();
		final Date expiration =
			new Date(now.getTime() + expTime * 1000);

		return Jwts.claims()
			.setIssuer("HongikRentalService")
			.setIssuedAt(now)
			.setExpiration(expiration);
	}

	public String createAccessToken(String studentId, MemberRole memberRole) {
		String accessToken = Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setClaims(createBaseClaims(jwtProperties.accessTokenExpTime()))
			.claim("type", "access")
			.setSubject(studentId)
			.claim("role", memberRole.name())
			.signWith(getSecretKey(), SignatureAlgorithm.HS256) // 서명 알고리즘 적용
			.compact();

		return accessToken;
	}

	public String createRefreshToken(String studentId) {
		String refreshToken = Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setClaims(createBaseClaims(jwtProperties.refreshTokenExpTime()))
			.claim("type", "refresh")
			.setSubject(studentId)
			.signWith(getSecretKey(), SignatureAlgorithm.HS256) // 서명 알고리즘 적용
			.compact();

		redisTemplate.opsForValue().set(
			String.valueOf(studentId),
			refreshToken,
			jwtProperties.refreshTokenExpTime(),
			TimeUnit.SECONDS
		);

		return refreshToken;
	}

	public Claims parseToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSecretKey()) // 서명 검증 위해 비밀 키 설정
			.build()
			.parseClaimsJws(token) // 서명 검증 포함
			.getBody();
	}

	private Key getSecretKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey()));
	}
}
