package mango.rentalsystem.domain.member.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import mango.rentalsystem.domain.member.dao.MemberRepository;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.member.utils.CsvUtil;
import mango.rentalsystem.domain.rental.application.RentalService;
import mango.rentalsystem.domain.rental.domain.RentalStatus;
import mango.rentalsystem.domain.rental.dto.response.RentalFindResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final RentalService rentalService;
	private final CsvUtil csvUtil;

	public void saveMembersFromCsv(String filePath) {
		List<Member> members = csvUtil.readMembersFromCsv(filePath);
		for (Member member : members) {
			// studentId로 중복 확인
			Optional<Member> existingMember = memberRepository.findByStudentId(member.getStudentId());
			if (!existingMember.isPresent()) {

				// 중복이 없다면 memberRepository에 새 member 추가
				memberRepository.save(member);

			}
		}
	}

	// 모든 회원 조회
	public List<Member> findAllMembers() {
		return memberRepository.findAll();
	}

	// 특정 회원 조회
	public Optional<Member> findById(Long id) {
		return memberRepository.findById(id);
	}

	// 특정 회원 삭제
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	// 회원 ID로 조회
	public Optional<Member> findByStudentId(String studentId) {
		return memberRepository.findByStudentId(studentId);
	}

	// 회원 생성 또는 수정
	public Member saveMember(Member member) {
		// studentId 중복 체크
		Optional<Member> existingMember = memberRepository.findByStudentId(member.getStudentId());
		if (existingMember.isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 학생 ID입니다: " + member.getStudentId());
		}

		// 중복이 없으면 새 회원 저장
		return memberRepository.save(member);
	}

	// 회원 정보 업데이트 (개인)
	public Member updateMember(Long id, UpdateMemberRequest request) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

		// 요청에서 제공된 값으로 업데이트

		if (request.getStudentId() != null) {
			member.setStudentId(request.getStudentId());
		}
		if (request.getName() != null) {
			member.setName(request.getName());
		}
		if (request.getPhone() != null) {
			member.setPhone(request.getPhone());
		}
		if (request.getAbsenceStatus() != null) {
			member.setAbsenceStatus(request.getAbsenceStatus());
		}
		if (request.getPassword() != null) {
			member.setPassword(request.getPassword());
		}

		// 변경된 회원 정보를 저장
		return memberRepository.save(member);
	}

	// 회원 정보 업데이트 (관리자)
	public Member updateMemberByAdmin(Long id, UpdateMemberRequestByAdmin request) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

		// 요청에서 제공된 값으로 업데이트
		if (request.getStudentId() != null) {
			member.setStudentId(request.getStudentId());
		}
		if (request.getName() != null) {
			member.setName(request.getName());
		}
		if (request.getPhone() != null) {
			member.setPhone(request.getPhone());
		}
		if (request.getAbsenceStatus() != null) {
			member.setAbsenceStatus(request.getAbsenceStatus());
		}
		if (request.getRole() != null) {
			member.setRole(request.getRole());
		}
		if (request.getPassword() != null) {
			member.setPassword(request.getPassword());
		}
		if (request.getRentalBannedDate() != null) {
			member.setRentalBannedDate(request.getRentalBannedDate());
		}

		// 변경된 회원 정보를 저장
		return memberRepository.save(member);
	}

	// 'BORROW' 상태인 항목만 필터링
	public List<RentalFindResponse> getCurrentRentals(String studentId) {
		List<RentalFindResponse> allRentals = rentalService.findMyRental(studentId);

		return allRentals.stream()
			.filter(rental -> rental.rentalStatus() == RentalStatus.BORROW)
			.collect(Collectors.toList());
	}

	// 'RETURN' 상태인 항목만 필터링
	public List<RentalFindResponse> getPastRentals(String studentId) {
		List<RentalFindResponse> allRentals = rentalService.findMyRental(studentId);

		return allRentals.stream()
			.filter(rental -> rental.rentalStatus() == RentalStatus.RETURN)
			.collect(Collectors.toList());
	}

	// 특정 학생의 정지 기간 조회
	public LocalDate getRentalBannedDate(String studentId) {
		return memberRepository.findByStudentId(studentId)
			.map(Member::getRentalBannedDate)
			.orElse(null);
	}

	// 회원 비밀번호 확인
	public boolean checkPassword(Long memberId, String password) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

		return passwordEncoder.matches(password, member.getPassword());
	}
}
