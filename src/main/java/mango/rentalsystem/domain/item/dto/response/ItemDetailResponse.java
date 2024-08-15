package mango.rentalsystem.domain.item.dto.response;

import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.item.domain.ItemStatus;

public record ItemDetailResponse(
	Long itemId,
	ItemStatus itemStatus,
	Double itemReview) {

	public static ItemDetailResponse from(Item item) {
		return new ItemDetailResponse(
			item.getId(),
			item.getItemStatus(),
			item.getItemReview()
		);
	}
}