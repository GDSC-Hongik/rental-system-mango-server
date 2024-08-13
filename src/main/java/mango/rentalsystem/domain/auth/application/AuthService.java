package mango.rentalsystem.domain.auth.application;

import static mango.rentalsystem.global.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.auth.dto.request.StudentIdPasswordRequest;
import mango.rentalsystem.domain.auth.dto.response.TokenResponse;
import mango.rentalsystem.domain.member.dao.MemberRepository;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.global.exception.CustomException;
import mango.rentalsystem.global.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final RedisTemplate<String, String> redisTemplate;

	//login 로직 구현
	public TokenResponse login(StudentIdPasswordRequest request) {
		final String studentId = request.studentId();
		final String password = request.password();

		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new UsernameNotFoundException("학번이 존재하지 않습니다."));

		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		String accessToken = jwtTokenProvider.createAccessToken(studentId, member.getRole());
		String refreshToken = jwtTokenProvider.createRefreshToken(studentId);

		return TokenResponse.of(accessToken, refreshToken);
	}

	//refresh 토큰 재발급 로직 구현
	public TokenResponse reissue(String refreshToken) {
		final String token = jwtTokenProvider.getJwtFromBearerToken(refreshToken);
		jwtTokenProvider.validateToken(token);

		String studentId = jwtTokenProvider.parseToken(token).getSubject();
		if (redisTemplate.hasKey(studentId)) {
			redisTemplate.delete(studentId);
			Optional<Member> optionalMember = memberRepository.findByStudentId(studentId);
			Member member = optionalMember.orElseThrow(() -> new BadCredentialsException("유효하지 않은 사용자입니다."));

			String accessToken = jwtTokenProvider.createAccessToken(studentId, member.getRole());
			refreshToken = jwtTokenProvider.createRefreshToken(studentId);

			return TokenResponse.of(accessToken, refreshToken);
		} else {
			throw new CustomException(INVALID_REFRESH_TOKEN);
		}
	}
}
