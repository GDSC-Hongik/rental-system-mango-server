package mango.rentalsystem.domain.member.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.auth.domain.LoginUser;
import mango.rentalsystem.domain.member.dto.request.MemberInfoUpdateRequest;
import mango.rentalsystem.domain.member.dto.request.MemberPasswordUpdateRequest;
import mango.rentalsystem.domain.member.dto.response.MemberFindResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mango.rentalsystem.domain.member.application.MemberService;
import mango.rentalsystem.domain.member.domain.Member;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	// 회원 조회
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<MemberFindResponse>> getAllMembers(@LoginUser String studentId) {
		return ResponseEntity.ok(memberService.findAllMembers(studentId));
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

	// 현재 로그인된 멤버의 정보 수정 (Member 전용)
	@PutMapping("/myinfo")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Void> updateMyInfo(@LoginUser String studentId,
		@RequestBody @Valid MemberInfoUpdateRequest request) {
		memberService.updateMyInfo(studentId, request);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/myinfo/password")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Void> updateMyPassword(@LoginUser String studentId,
		@RequestBody @Valid MemberPasswordUpdateRequest request) {
		memberService.updateMyPassword(studentId, request);
		return ResponseEntity.ok().build();
	}
}
