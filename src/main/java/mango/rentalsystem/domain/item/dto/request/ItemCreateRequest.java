package mango.rentalsystem.domain.item.dto.request;

import jakarta.validation.constraints.NotNull;
import mango.rentalsystem.domain.item.domain.ItemStatus;

public record ItemCreateRequest (
	@NotNull(message = "카테고리를 입력해주세요")
	Long categoryId,

	ItemStatus itemStatus,

	Double itemReview
){}