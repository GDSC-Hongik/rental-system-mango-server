package mango.rentalsystem.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberCreateRequest(
	@NotNull(message = "학번을 입력해주세요.")
	String studentId,
	@NotNull(message = "이름을 입력해주세요.")
	String name,
	@NotNull(message = "전화번호를 입력해주세요.")
	String phone) {
}
