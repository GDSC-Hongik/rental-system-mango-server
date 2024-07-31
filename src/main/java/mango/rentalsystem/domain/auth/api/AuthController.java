package mango.rentalsystem.domain.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.auth.application.AuthService;
import mango.rentalsystem.domain.auth.dto.request.StudentIdPasswordRequest;
import mango.rentalsystem.domain.auth.dto.response.TokenResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody StudentIdPasswordRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("reissue")
	public ResponseEntity<TokenResponse> reissue(@RequestHeader("Authorization") final String refreshToken) {
		return ResponseEntity.ok(authService.reissue(refreshToken));
	}
}
