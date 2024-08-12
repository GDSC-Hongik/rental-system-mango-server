package mango.rentalsystem.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryModifyRequest (
	@NotBlank(message = "물품 종류명을 입력해주세요")
	String name,

	String description
){}