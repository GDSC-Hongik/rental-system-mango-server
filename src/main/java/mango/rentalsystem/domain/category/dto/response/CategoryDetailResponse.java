package mango.rentalsystem.domain.category.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.item.domain.ItemStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailResponse {
	private Long categoryId;
	private String name;
	private String description;
	private List<ItemResponse> items;

	public static CategoryDetailResponse of(Category category) {
		List<ItemResponse> itemResponses = category.getItems() != null
			? category.getItems().stream()
			.map(ItemResponse::of)
			.collect(Collectors.toList())
			: Collections.emptyList();

		return new CategoryDetailResponse(
			category.getId(),
			category.getName(),
			category.getDescription(),
			itemResponses
		);
	}

	@Getter
	@AllArgsConstructor
	public static class ItemResponse {
		private Long itemId;
		private ItemStatus itemStatus;
		private Double itemReview;

		public static ItemResponse of(Item item) {
			return new ItemResponse(
				item.getId(),
				item.getItemStatus(),
				item.getItemReview()
			);
		}
	}
}
