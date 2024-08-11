package mango.rentalsystem.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import mango.rentalsystem.domain.department.domain.Department;

public record CategoryCreateRequest (
	@NotBlank(message = "물품 종류명을 입력해주세요")
	String name,

	@NotBlank(message = "학과를 입력해주세요")
	Long departmentId,

	String description
){}
