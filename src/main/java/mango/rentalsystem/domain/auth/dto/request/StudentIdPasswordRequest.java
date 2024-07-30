package mango.rentalsystem.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record StudentIdPasswordRequest(
	@NotNull(message = "학번을 입력해주세요.")
	String studentId,
	@NotNull(message = "비밀번호를 입력해주세요.")
	String password) {
}
