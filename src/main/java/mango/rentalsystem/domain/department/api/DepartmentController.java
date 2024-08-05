package mango.rentalsystem.domain.department.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.auth.domain.LoginUser;
import mango.rentalsystem.domain.department.application.DepartmentService;
import mango.rentalsystem.domain.department.dto.request.DepartmentUpdateRequest;
import mango.rentalsystem.domain.department.dto.response.DepartmentResponse;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

	private final DepartmentService departmentService;

	@GetMapping
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<DepartmentResponse> getDepartment(@LoginUser String studentId) {
		return ResponseEntity.ok().build();
	}

	@PatchMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> updateDepartment(@LoginUser String studentId,
		@Valid @RequestBody DepartmentUpdateRequest request) {
		return ResponseEntity.ok().build();
	}
}
