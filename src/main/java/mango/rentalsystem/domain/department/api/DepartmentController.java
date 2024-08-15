package mango.rentalsystem.domain.department.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.auth.domain.LoginUser;
import mango.rentalsystem.domain.department.application.DepartmentService;
import mango.rentalsystem.domain.department.dto.request.DepartmentRentalTimeUpdateRequest;
import mango.rentalsystem.domain.department.dto.request.DepartmentInfoUpdateRequest;
import mango.rentalsystem.domain.department.dto.response.DepartmentResponse;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

	private final DepartmentService departmentService;

	@GetMapping
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<DepartmentResponse> getDepartment(@LoginUser String loginId) {
		return ResponseEntity.ok(departmentService.getDepartment(loginId));
	}

	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> updateDepartmentInfo(@LoginUser String loginId,
		@Valid @RequestBody DepartmentInfoUpdateRequest request) {
		departmentService.updateDepartmentInfo(loginId, request);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/rentaltime")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> updateDepartmentRentalTime(@LoginUser String loginId,
		@Valid @RequestBody DepartmentRentalTimeUpdateRequest request) {
		departmentService.updateDepartmentRentalTime(loginId, request);
		return ResponseEntity.ok().build();
	}
}
