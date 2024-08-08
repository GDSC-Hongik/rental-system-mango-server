package mango.rentalsystem.domain.rental.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.auth.domain.LoginUser;
import mango.rentalsystem.domain.rental.application.RentalService;
import mango.rentalsystem.domain.rental.domain.Rental;
import mango.rentalsystem.domain.rental.dto.request.AdminRentalStatusUpdateRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalRequest;
import mango.rentalsystem.domain.rental.dto.request.MemberRentalStatusUpdateRequest;
import mango.rentalsystem.domain.rental.dto.response.RentalCreateResponse;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
public class RentalController {

	private final RentalService rentalService;

	@PostMapping
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<RentalCreateResponse> createRental(@LoginUser String studentId,
		@RequestBody @Valid RentalRequest rentalRequest) {
		return ResponseEntity.ok(rentalService.createRental(studentId, rentalRequest));
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Rental>> getAllRentals() {
		return null;
	}

	@GetMapping("/myrental")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<List<Rental>> getMyRentals(@LoginUser String studentId) {
		return null;
	}

	@GetMapping("/{rentalId}")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Rental> getRental(@LoginUser String studentId, @PathVariable Long rentalId) {
		return null;
	}

	@PatchMapping("/{rentalId}")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Void> updateRentalStatusByMember(@PathVariable Long rentalId,
		@RequestBody @Valid MemberRentalStatusUpdateRequest request) {
		return null;
	}

	@PatchMapping("/{rentalId}/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> updateRentalStatusByAdmin(@PathVariable Long rentalId,
		@RequestBody @Valid AdminRentalStatusUpdateRequest request) {
		return null;
	}
}
