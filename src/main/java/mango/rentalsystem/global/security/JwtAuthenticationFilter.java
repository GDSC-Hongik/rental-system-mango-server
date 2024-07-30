package mango.rentalsystem.global.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		for (String whiteUrl : WhiteUrl.FILTER_WHITE_LIST) {
			if (request.getRequestURI().contains(whiteUrl)) {
				filterChain.doFilter(request, response);
				return;
			}
		}
		String accessToken = getJwtFromRequest(request);
		try {
			Authentication authentication = getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (ExpiredJwtException e) { // 만료된 토큰
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Access token has expired");
			return;
		} catch (Exception e) { // 이외 예외 전부 (예외 로직 개선 필요)
			System.out.println("토큰 인증 실패"); // 예외 로직 개선하면서 삭제
			return;
		}
		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7); // "Bearer ".length() == 7
		}
		return null;
	}

	private Authentication getAuthentication(String accessToken) {
		final Claims claims = jwtTokenProvider.parseToken(accessToken);

		String studentId = claims.getSubject();
		String memberRole = claims.get("role", String.class);

		UserDetails userDetails = new AuthDetails(studentId, memberRole);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
