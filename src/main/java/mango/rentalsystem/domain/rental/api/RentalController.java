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
import mango.rentalsystem.domain.rental.dto.request.RentalCreateRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalFindRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalReviewRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalStatusUpdateRequest;
import mango.rentalsystem.domain.rental.dto.response.RentalCreateResponse;
import mango.rentalsystem.domain.rental.dto.response.RentalFindResponse;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
public class RentalController {

	private final RentalService rentalService;

	@PostMapping
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<RentalCreateResponse> createRental(@LoginUser String studentId,
		@RequestBody @Valid RentalCreateRequest request) {
		return ResponseEntity.ok(rentalService.createRental(studentId, request));
	}

	// 페이지네이션 필요할 수도 있음
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<RentalFindResponse>> getAllRental(@LoginUser String studentId,
		@RequestBody(required = false) RentalFindRequest request) {
		return ResponseEntity.ok(rentalService.findAllRental(studentId, request));
	}

	// 페이지네이션 필요할 수도 있음
	@GetMapping("/myrental")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<List<RentalFindResponse>> getMyRental(@LoginUser String studentId) {
		return ResponseEntity.ok(rentalService.findMyRental(studentId));
	}

	@GetMapping("/{rentalId}")
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<RentalFindResponse> getRental(@LoginUser String studentId, @PathVariable Long rentalId) {
		return ResponseEntity.ok(rentalService.findRental(studentId, rentalId));
	}

	@PatchMapping("/{rentalId}")
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')") // role hierarchy 구현하는 게 좋아보임
	public ResponseEntity<Void> updateRentalStatus(@LoginUser String studentId, @PathVariable Long rentalId,
		@RequestBody @Valid RentalStatusUpdateRequest request) {
		rentalService.updateRentalStatus(studentId, rentalId, request);
		return ResponseEntity.ok().build();
	}

	// 리뷰 메서드 필요
	@PatchMapping("/{rentalId}/review")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Void> updateRentalReview(@LoginUser String studentId, @PathVariable Long rentalId,
		@RequestBody @Valid RentalReviewRequest request) {
		rentalService.updateRentalReview(studentId, rentalId, request);
		return ResponseEntity.ok().build();
	}
}
