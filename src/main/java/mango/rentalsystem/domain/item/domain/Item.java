package mango.rentalsystem.domain.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mango.rentalsystem.domain.category.domain.Category;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Item {
	@Id
	@GeneratedValue
	@Column(name = "item_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@Enumerated(EnumType.STRING)
	private ItemStatus itemStatus;

	private Double itemReview;

	private Integer itemRentalCount;

	public static Item create(Category category, ItemStatus itemStatus, Double itemReview) {
		return Item.builder()
			.category(category)
			.itemStatus(itemStatus)
			.itemReview(itemReview)
			.build();
	}

	public void updateItemStatus(ItemStatus itemStatus) {
		this.itemStatus = itemStatus;
	}

	public void updateItemReview(Integer itemReview) {
		Double itemReviewSum = this.itemReview * this.itemRentalCount++ + itemReview;
		this.itemReview = itemReviewSum / this.itemRentalCount;
	}
}
