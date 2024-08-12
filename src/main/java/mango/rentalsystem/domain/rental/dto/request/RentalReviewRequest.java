package mango.rentalsystem.domain.rental.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RentalReviewRequest(
	@Min(0) @Max(5)
	@NotNull(message = "별점을 입력해주세요.")
	Integer rentalReview) {
}
