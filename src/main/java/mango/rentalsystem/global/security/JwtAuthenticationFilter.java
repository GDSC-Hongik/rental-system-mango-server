package mango.rentalsystem.global.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
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

		String bearerToken = request.getHeader("Authorization");
		String accessToken = jwtTokenProvider.getJwtFromBearerToken(bearerToken);
		jwtTokenProvider.validateToken(accessToken);

		Authentication authentication = getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	private Authentication getAuthentication(String accessToken) {
		final Claims claims = jwtTokenProvider.parseToken(accessToken);

		String studentId = claims.getSubject();
		String memberRole = claims.get("role", String.class);

		UserDetails userDetails = new AuthDetails(studentId, memberRole);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
