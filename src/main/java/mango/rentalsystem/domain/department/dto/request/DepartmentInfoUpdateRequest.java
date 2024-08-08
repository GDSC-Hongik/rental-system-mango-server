package mango.rentalsystem.domain.department.dto.request;

import jakarta.validation.constraints.NotNull;

public record DepartmentInfoUpdateRequest(
	@NotNull(message = "학과명을 입력해주세요.")
	String name,
	@NotNull(message = "대여 장소를 입력해주세요.")
	String rentalPlace,
	@NotNull(message = "공지사항을 입력해주세요.")
	String notice) {
}
