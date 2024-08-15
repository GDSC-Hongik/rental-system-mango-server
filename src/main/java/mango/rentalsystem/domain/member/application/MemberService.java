package mango.rentalsystem.domain.member.application;

import static mango.rentalsystem.domain.member.domain.MemberRole.*;
import static mango.rentalsystem.global.exception.ErrorCode.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.member.dto.request.MemberCreateRequest;
import mango.rentalsystem.domain.member.dto.request.MemberInfoUpdateRequest;
import mango.rentalsystem.domain.member.dto.request.MemberPasswordUpdateRequest;
import mango.rentalsystem.domain.member.dto.response.MemberCreateResponse;
import mango.rentalsystem.domain.member.dto.response.MemberFindResponse;
import mango.rentalsystem.global.exception.CustomException;
import mango.rentalsystem.global.exception.ErrorCode;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mango.rentalsystem.domain.member.dao.MemberRepository;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.member.utils.CsvUtil;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final CsvUtil csvUtil;

	public MemberCreateResponse createMember(String loginId, MemberCreateRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Member targetMember = Member.createMember(request.studentId(), passwordEncoder.encode(request.phone()),
			request.name(), member.getDepartment(), request.phone());

		Member savedMember = memberRepository.save(targetMember);
		return MemberCreateResponse.from(savedMember);
	}

	public MemberFindResponse findMyMemberInfo(String loginId) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		return MemberFindResponse.from(member);
	}

	public void updateMyInfo(String loginId, MemberInfoUpdateRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		member.updateMemberInfo(request.name(), request.phone(), request.absenceStatus());
	}

	public void updateMyPassword(String loginId, MemberPasswordUpdateRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		if (!passwordEncoder.matches(request.currentPassword(), member.getPassword())) {
			throw new CustomException(INVALID_PASSWORD);
		}

		member.updateMemberPassword(passwordEncoder.encode(request.newPassword()));
	}

	public void updateInfo(String loginId, Long memberId, MemberInfoUpdateRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Member targetMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		validateAuthForMember(member, targetMember);

		targetMember.updateMemberInfo(request.name(), request.phone(), request.absenceStatus());
	}

	public void saveMembersFromCsv(String loginId, String filePath) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		List<Member> members = csvUtil.readMembersFromCsv(member, filePath);
		for (Member targetMember : members) {
			// studentId로 중복 확인
			Optional<Member> existingMember = memberRepository.findByStudentId(targetMember.getStudentId());
			if (!existingMember.isPresent()) {
				// 중복이 없다면 memberRepository에 새 targetMember 추가
				memberRepository.save(targetMember);
			}
		}
	}

	// 모든 회원 조회
	public List<MemberFindResponse> findAllMembers(String loginId) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Department department = member.getDepartment();

		List<Member> allMemberList = memberRepository.findAllByDepartment(department);

		return allMemberList.stream()
			.map(MemberFindResponse::from)
			.collect(Collectors.toList());
	}

	// 특정 회원 조회
	public MemberFindResponse findMember(String loginId, Long memberId) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Member targetMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		validateAuthForMember(member, targetMember);

		return MemberFindResponse.from(targetMember);
	}

	// 특정 회원 삭제
	public void deleteMember(String loginId, Long memberId) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Member targetMember = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		validateAuthForMember(member, targetMember);

		memberRepository.delete(targetMember);
	}

	private void validateAuthForMember(Member member, Member targetMember) {
		if (member.getRole() == ROLE_MEMBER && !(targetMember.equals(member))
			|| member.getRole() == ROLE_ADMIN && !(targetMember.getDepartment().equals(member.getDepartment()))) {
			throw new CustomException(UNAUTHORIZED_MEMBER);
		}
	}
}
