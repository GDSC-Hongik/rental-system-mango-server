package mango.rentalsystem.domain.rental.dto.response;

import java.time.LocalDateTime;

import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.rental.domain.Rental;
import mango.rentalsystem.domain.rental.domain.RentalStatus;

public record RentalCreateResponse(
	Long rentalId,
	Item item,
	RentalStatus rentalStatus,
	LocalDateTime borrowDateTime,
	LocalDateTime deadlineDateTime) {

	public static RentalCreateResponse from(Rental rental) {
		return new RentalCreateResponse(
			rental.getId(),
			rental.getItem(),
			rental.getRentalStatus(),
			rental.getBorrowDateTime(),
			rental.getDeadlineDateTime());
	}
}
