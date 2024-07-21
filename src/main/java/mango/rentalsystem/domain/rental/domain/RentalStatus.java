package mango.rentalsystem.domain.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RentalStatus {

	//대여 요청 후
	APPROVAL_REQUESTED("승인 요청됨"),
	APPROVED("승인됨"),
	REJECTED("거절됨"),

	//대여 후
	BORROW("대여 중"),
	OVERDUE("연체 중"),

	//반납 요청 후
	RETURN_REQUESTED("반납 요청됨"),
	RETURN("반납됨");

	private final String message;
}