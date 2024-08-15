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

	// 대여 생성
	@PostMapping
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<RentalCreateResponse> createRental(@LoginUser String loginId,
		@RequestBody @Valid RentalCreateRequest request) {
		return ResponseEntity.ok(rentalService.createRental(loginId, request));
	}

	// 대여 전체 조회
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<RentalFindResponse>> getAllRental(@LoginUser String loginId,
		@RequestBody(required = false) RentalFindRequest request) {
		return ResponseEntity.ok(rentalService.findAllRental(loginId, request));
	}

	// 내 대여 전체 조회
	@GetMapping("/myrental")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<List<RentalFindResponse>> getMyRental(@LoginUser String loginId,
		@RequestBody(required = false) RentalFindRequest request) {
		return ResponseEntity.ok(rentalService.findMyRental(loginId, request));
	}

	// 대여 단건 조회
	@GetMapping("/{rentalId}")
	@PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
	public ResponseEntity<RentalFindResponse> getRental(@LoginUser String loginId, @PathVariable Long rentalId) {
		return ResponseEntity.ok(rentalService.findRental(loginId, rentalId));
	}

	// 대여 상태 변경
	@PatchMapping("/{rentalId}")
	@PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
	public ResponseEntity<Void> updateRentalStatus(@LoginUser String loginId, @PathVariable Long rentalId,
		@RequestBody @Valid RentalStatusUpdateRequest request) {
		rentalService.updateRentalStatus(loginId, rentalId, request);
		return ResponseEntity.ok().build();
	}

	// 리뷰 메서드 필요
	@PatchMapping("/{rentalId}/review")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Void> updateRentalReview(@LoginUser String loginId, @PathVariable Long rentalId,
		@RequestBody @Valid RentalReviewRequest request) {
		rentalService.updateRentalReview(loginId, rentalId, request);
		return ResponseEntity.ok().build();
	}
}
