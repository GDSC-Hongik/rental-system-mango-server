package mango.rentalsystem.domain.rental.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.rental.domain.Rental;
import mango.rentalsystem.domain.rental.domain.RentalStatus;

public record RentalFindResponse(
	Long rentalId,
	String studentId,
	Long itemId,
	RentalStatus rentalStatus,
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime requestDateTime,
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime borrowDateTime,
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime returnDateTime,
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime deadlineDateTime,
	Integer rentalReview) {

	public static RentalFindResponse from(Rental rental) {
		return new RentalFindResponse(
			rental.getId(),
			rental.getMember().getStudentId(),
			rental.getItem().getId(),
			rental.getRentalStatus(),
			rental.getRequestDateTime(),
			rental.getBorrowDateTime(),
			rental.getReturnDateTime(),
			rental.getDeadlineDateTime(),
			rental.getRentalReview());
	}
}
