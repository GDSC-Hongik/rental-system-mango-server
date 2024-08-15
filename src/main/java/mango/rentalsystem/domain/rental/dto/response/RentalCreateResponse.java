package mango.rentalsystem.domain.rental.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import mango.rentalsystem.domain.rental.domain.Rental;
import mango.rentalsystem.domain.rental.domain.RentalStatus;

public record RentalCreateResponse(
	Long rentalId,
	Long itemId,
	RentalStatus rentalStatus,
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime requestDateTime,
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime deadlineDateTime) {

	public static RentalCreateResponse from(Rental rental) {
		return new RentalCreateResponse(
			rental.getId(),
			rental.getItem().getId(),
			rental.getRentalStatus(),
			rental.getRequestDateTime(),
			rental.getDeadlineDateTime()
		);
	}
}
