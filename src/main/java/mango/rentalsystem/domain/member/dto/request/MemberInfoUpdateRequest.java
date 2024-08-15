package mango.rentalsystem.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberInfoUpdateRequest(
	@NotNull(message = "이름을 입력해주세요.")
	String name,
	@NotNull(message = "전화번호를 입력해주세요.")
	String phone,
	@NotNull(message = "휴학 여부를 입력해주세요.")
	Boolean absenceStatus) {
}
