package mango.rentalsystem.domain.rental.dto.request;

import java.util.List;

import mango.rentalsystem.domain.rental.domain.RentalStatus;

public record RentalFindRequest(List<RentalStatus> rentalStatuses) {
}
