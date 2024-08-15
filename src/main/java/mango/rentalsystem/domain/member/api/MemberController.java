package mango.rentalsystem.domain.member.api;

import java.util.List;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.auth.domain.LoginUser;
import mango.rentalsystem.domain.member.dto.request.MemberCreateRequest;
import mango.rentalsystem.domain.member.dto.request.MemberInfoUpdateRequest;
import mango.rentalsystem.domain.member.dto.request.MemberPasswordUpdateRequest;
import mango.rentalsystem.domain.member.dto.response.MemberCreateResponse;
import mango.rentalsystem.domain.member.dto.response.MemberFindResponse;

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

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	// 회원 조회
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<MemberFindResponse>> getAllMembers(@LoginUser String loginId) {
		return ResponseEntity.ok(memberService.findAllMembers(loginId));
	}

	// 회원 추가
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MemberCreateResponse> addMember(@LoginUser String loginId,
		@RequestBody MemberCreateRequest request) {
		return ResponseEntity.ok(memberService.createMember(loginId, request));
	}

	// 특정 회원 정보 조회
	@GetMapping("/{studentId}")
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<MemberFindResponse> getMember(@LoginUser String loginId, @PathVariable String studentId) {
		return ResponseEntity.ok(memberService.findMember(loginId, studentId));
	}

	// 특정 회원 정보 수정
	@PutMapping("/{studentId}")
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Void> updateMember(@LoginUser String loginId, @PathVariable String studentId,
		@RequestBody @Valid MemberInfoUpdateRequest request) {
		memberService.updateInfo(loginId, studentId, request);
		return ResponseEntity.ok().build();
	}

	// 특정 회원 정보 삭제
	@DeleteMapping("/{studentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteMember(@LoginUser String loginId, @PathVariable String studentId) {
		memberService.deleteMember(loginId, studentId);
		return ResponseEntity.ok().build();
	}

	// 맨 처음 웹 실행시 ADMIN이 학생목록을 load해야 함
	@GetMapping("/load")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> loadMembers(@LoginUser String loginId, @RequestParam String filePath) {
		memberService.saveMembersFromCsv(loginId, filePath);
		return ResponseEntity.ok().build();
	}

	// 현재 로그인된 멤버의 정보 조회 (Member 전용)
	@GetMapping("/myinfo")
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<MemberFindResponse> getMyInfo(@LoginUser String loginId) {
		return ResponseEntity.ok(memberService.findMyMemberInfo(loginId));
	}

	// 현재 로그인된 멤버의 정보 수정 (Member 전용)
	@PutMapping("/myinfo")
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Void> updateMyInfo(@LoginUser String loginId,
		@RequestBody @Valid MemberInfoUpdateRequest request) {
		memberService.updateMyInfo(loginId, request);
		return ResponseEntity.ok().build();
	}

	// 비밀번호 수정
	@PatchMapping("/myinfo/password")
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Void> updateMyPassword(@LoginUser String loginId,
		@RequestBody @Valid MemberPasswordUpdateRequest request) {
		memberService.updateMyPassword(loginId, request);
		return ResponseEntity.ok().build();
	}
}
