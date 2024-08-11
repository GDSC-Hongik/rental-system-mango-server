package mango.rentalsystem.domain.rental.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.rental.application.RentalService;
import mango.rentalsystem.domain.rental.domain.Rental;
import mango.rentalsystem.domain.rental.dto.request.RentalRequest;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
public class RentalController {

	private final RentalService rentalService;

	@GetMapping
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<List<Rental>> getRentals() {
		return null;
	}

	@PostMapping
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Void> postRental(@Valid @RequestBody RentalRequest rentalRequest) {
		return null;
	}

	@PatchMapping
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Void> patchRentalStatus(@Valid @RequestBody RentalRequest rentalRequest) {
		return null;
	}
}
