package mango.rentalsystem.domain.category.dto.response;

import mango.rentalsystem.domain.category.domain.Category;

public record CategorySummaryResponse(
	Long categoryId,
	String name,
	String description
) {
	public static CategorySummaryResponse of(Category category) {
		return new CategorySummaryResponse(
			category.getId(),
			category.getName(),
			category.getDescription()
		);
	}
}
