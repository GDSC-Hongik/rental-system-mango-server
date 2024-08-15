package mango.rentalsystem.global.security;

import static mango.rentalsystem.global.exception.ErrorCode.*;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.member.domain.MemberRole;
import mango.rentalsystem.global.exception.CustomException;
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

	public void validateToken(String token) {
		try {
			final Claims claims = parseToken(token);
			String tokenType = claims.get("type", String.class);
			if (tokenType.equals("access") || tokenType.equals("refresh")) {
				return;
			}
			throw new CustomException(WRONG_TYPE_TOKEN);
		} catch (ExpiredJwtException e) {
			throw new CustomException(EXPIRED_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new CustomException(UNSUPPORTED_TOKEN);
		} catch (MalformedJwtException e) {
			throw new CustomException(WRONG_TYPE_TOKEN);
		} catch (SignatureException e) {
			throw new CustomException(WRONG_SIGNATURE_TOKEN);
		} catch (IllegalArgumentException e) {
			throw new CustomException(UNKNOWN_TOKEN);
		}
	}

	public String getJwtFromBearerToken(String bearerToken) {
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7); // "Bearer ".length() == 7
		}
		return null;
	}

	private Key getSecretKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey()));
	}
}
