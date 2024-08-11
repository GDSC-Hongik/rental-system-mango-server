package mango.rentalsystem.domain.category.dto.response;

import java.util.List;

import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.domain.Item;

public record CategoryDetailResponse(
	Long categoryId,
	String name,
	String description,
	Department department,
	List<Item> items
) {
	public static CategoryDetailResponse of(Category category) {
		return new CategoryDetailResponse(
			category.getId(),
			category.getName(),
			category.getDescription(),
			category.getDepartment(),
			category.getItems()
		);
	}
}
