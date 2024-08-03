package mango.rentalsystem.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import mango.rentalsystem.domain.department.domain.Department;

public record CategoryRequest (
	@NotBlank(message = "물품 종류명을 입력해주세요")
	String name,

	String description,

	@NotBlank(message = "학과를 입력해주세요")
	Department department
){}
