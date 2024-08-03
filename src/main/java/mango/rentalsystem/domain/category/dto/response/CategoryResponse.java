package mango.rentalsystem.domain.category.dto.response;

import java.util.List;

import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.domain.Item;

public record CategoryResponse(
	Long categoryId,
	String name,
	String description,
	Department department,
	List<Item> items
) {
	public static CategoryResponse of (Category category) {
		return new CategoryResponse(
			category.getId(),
			category.getName(),
			category.getDescription(),
			category.getDepartment(),
			category.getItems()
		);
	}
}
