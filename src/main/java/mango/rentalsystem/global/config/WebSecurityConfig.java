package mango.rentalsystem.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.global.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
			.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(
				SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()) // @PreAuthorization 사용.
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowCredentials(true); // 자격 증명 요청 허용
		configuration.addAllowedOrigin("*"); // (클라이언트 도메인으로 설정 필요) 클라이언트 오리진 허용
		configuration.addAllowedHeader("*"); // 클라이언트의 모든 헤더 허용
		configuration.addAllowedMethod("*"); // 클라이언트의 모든 http 메서드 허용
		configuration.addExposedHeader("Authorization"); // 서버가 노출할 헤더 설정
		configuration.setMaxAge(3600L); // 클라이언트의 Preflight 요청 캐싱 시간 설정 (1시간)

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // 모든 경로에 configuration 적용
		return source;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
