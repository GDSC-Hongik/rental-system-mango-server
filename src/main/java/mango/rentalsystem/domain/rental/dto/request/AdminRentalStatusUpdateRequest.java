package mango.rentalsystem.domain.rental.dto.request;

import static mango.rentalsystem.domain.rental.domain.RentalStatus.*;

import jakarta.validation.constraints.NotNull;
import mango.rentalsystem.domain.rental.domain.RentalStatus;
import mango.rentalsystem.domain.rental.validator.RentalStatusSubset;

public record AdminRentalStatusUpdateRequest(
	@RentalStatusSubset(anyOf = {APPROVED, REJECTED, BORROW, RETURN})
	@NotNull(message = "상태를 지정해주세요.")
	RentalStatus rentalStatus) implements RentalStatusUpdateRequest {
}
