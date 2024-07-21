package mango.rentalsystem.domain.item.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemStatus {
	IDLE("대기"),
	BOOK("예약"),
	BORROW("대여 중"),
	OVERDUE("연체 중");

	private final String message;
}
