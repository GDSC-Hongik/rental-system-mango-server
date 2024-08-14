package mango.rentalsystem.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberPasswordUpdateRequest(
	@NotNull(message = "현재 비밀번호를 입력해주세요.")
	String currentPassword,
	@NotNull(message = "변경할 비밀번호를 입력해주세요.")
	String newPassword) {
}
