package mango.rentalsystem.domain.member.api;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.auth.domain.LoginUser;
import mango.rentalsystem.domain.member.dto.response.MemberFindResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mango.rentalsystem.domain.member.application.MemberService;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.rental.dto.response.RentalFindResponse;
import mango.rentalsystem.global.security.AuthDetails;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	// 회원 조회
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Map<String, Object>>> getAllMembers() {
		List<Member> members = memberService.findAllMembers();
		List<Map<String, Object>> memberDataList = members.stream()
			.map(member -> {
				Map<String, Object> memberData = new HashMap<>();
				memberData.put("studentId", member.getStudentId());
				memberData.put("name", member.getName());
				memberData.put("phone", member.getPhone());
				memberData.put("absenceStatus", member.isAbsenceStatus());
				memberData.put("rentalBannedDate", member.getRentalBannedDate());
				memberData.put("departmentName",
					member.getDepartment() != null ? member.getDepartment().getName() : null);
				return memberData;
			})
			.collect(Collectors.toList());

		return ResponseEntity.ok(memberDataList);
	}

	// 회원 추가
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addMember(@RequestBody Member member) {
		try {
			Member savedMember = memberService.saveMember(member);
			return ResponseEntity.ok(savedMember);
		} catch (IllegalArgumentException e) {
			// studentId 중복 시 발생한 예외를 처리
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// 특정 회원 정보 조회
	@GetMapping("/{memberId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> getMemberById(@PathVariable Long memberId) {
		Optional<Member> member = memberService.findById(memberId);

		return member.map(m -> {
			Map<String, Object> memberData = new HashMap<>();
			memberData.put("studentId", m.getStudentId());
			memberData.put("name", m.getName());
			memberData.put("phone", m.getPhone());
			memberData.put("absenceStatus", m.isAbsenceStatus());
			memberData.put("rentalBannedDate", m.getRentalBannedDate());
			memberData.put("departmentName", m.getDepartment() != null ? m.getDepartment().getName() : null);
			return ResponseEntity.ok(memberData);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 특정 회원 정보 수정
	@PatchMapping("/{memberId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> updateMember(@PathVariable("memberId") Long memberId,
		@RequestBody @Valid UpdateMemberRequestByAdmin request) {

		// 회원 정보를 업데이트
		memberService.updateMemberByAdmin(memberId, request);

		// 업데이트된 회원 정보를 조회
		Optional<Member> updatedMember = memberService.findById(memberId);

		return updatedMember.map(m -> {
			Map<String, Object> memberData = new HashMap<>();
			memberData.put("studentId", m.getStudentId());
			memberData.put("name", m.getName());
			memberData.put("phone", m.getPhone());
			memberData.put("absenceStatus", m.isAbsenceStatus());
			memberData.put("rentalBannedDate", m.getRentalBannedDate());
			memberData.put("departmentName", m.getDepartment() != null ? m.getDepartment().getName() : null);
			return ResponseEntity.ok(memberData);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 특정 회원 정보 삭제
	@DeleteMapping("/{memberId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
		Optional<Member> member = memberService.findById(memberId);
		if (member.isPresent()) {
			memberService.deleteMember(memberId);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 맨 처음 웹 실행시 ADMIN이 학생목록을 load해야 함
	@GetMapping("/load")
	@PreAuthorize("hasRole('ADMIN')")
	public String loadMembers(@RequestParam String filePath) {
		memberService.saveMembersFromCsv(filePath);
		return "csv파일 로드 중";
	}

	// 현재 로그인된 멤버의 정보 조회 (Member 전용)
	@GetMapping("/myinfo")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<MemberFindResponse> getMyInfo(@LoginUser String studentId) {
		return ResponseEntity.ok(memberService.findMyMemberInfo(studentId));
	}

	// 현재 로그인된 멤버의 정보 수정 (Member 전용), 비밀번호를 입력해야 수정 가능
	@PatchMapping("/myinfo")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Map<String, Object>> updateMemberDetails(
		@AuthenticationPrincipal AuthDetails authDetails,
		@RequestParam String password,
		@RequestBody @Valid UpdateMemberRequest request) {

		String studentId = authDetails.getUsername();
		Long memberId = memberService.findByStudentId(studentId)
			.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")).getId();

		// 비밀번호 확인
		if (!memberService.checkPassword(memberId, password)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", "비밀번호가 일치하지 않습니다."));
		}

		// 회원 정보를 업데이트
		memberService.updateMember(memberId, request);

		// 업데이트된 회원 정보를 조회
		Optional<Member> updatedMember = memberService.findById(memberId);

		return updatedMember.map(m -> {
			Map<String, Object> memberData = new HashMap<>();
			memberData.put("studentId", m.getStudentId());
			memberData.put("name", m.getName());
			memberData.put("phone", m.getPhone());
			memberData.put("absenceStatus", m.isAbsenceStatus());
			memberData.put("departmentName", m.getDepartment() != null ? m.getDepartment().getName() : null);
			return ResponseEntity.ok(memberData);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 현재 로그인된 멤버의 대여 중인 항목 조회 (Member 전용)
	@GetMapping("/current-rental")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<List<RentalFindResponse>> getCurrentRentals(@AuthenticationPrincipal AuthDetails authDetails) {
		String studentId = authDetails.getUsername();
		List<RentalFindResponse> currentRentals = memberService.getCurrentRentals(studentId);
		return ResponseEntity.ok(currentRentals);
	}

	// 현재 로그인된 멤버의 대여 기록 조회 (Member 전용)
	@GetMapping("/history")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<List<RentalFindResponse>> getPastRentals(@AuthenticationPrincipal AuthDetails authDetails) {
		String studentId = authDetails.getUsername();
		List<RentalFindResponse> pastRentals = memberService.getPastRentals(studentId);
		return ResponseEntity.ok(pastRentals);
	}

	// 현재 로그인된 멤버의 정지 기간 조회 (Member 전용)
	@GetMapping("/suspension")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<String> getMySuspensionPeriod(@AuthenticationPrincipal AuthDetails authDetails) {
		String studentId = authDetails.getUsername();

		// 정지 기간을 조회
		LocalDate rentalBannedDate = memberService.getRentalBannedDate(studentId);

		// 정지 기간이 존재하는지 확인하고 적절한 응답을 반환
		if (rentalBannedDate != null) {
			return ResponseEntity.ok(rentalBannedDate.toString()); //
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("대여가 정지되지 않았습니다");
		}
	}
}
