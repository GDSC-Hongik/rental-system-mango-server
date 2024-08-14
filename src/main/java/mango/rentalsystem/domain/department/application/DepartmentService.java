package mango.rentalsystem.domain.department.application;

import static mango.rentalsystem.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.department.dto.request.DepartmentRentalTimeUpdateRequest;
import mango.rentalsystem.domain.department.dto.request.DepartmentInfoUpdateRequest;
import mango.rentalsystem.domain.department.dto.response.DepartmentResponse;
import mango.rentalsystem.domain.member.dao.MemberRepository;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.global.exception.CustomException;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {

	private final MemberRepository memberRepository;

	public DepartmentResponse getDepartment(String studentId) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
		Department department = member.getDepartment();
		return DepartmentResponse.from(department);
	}

	public void updateDepartmentInfo(String studentId, DepartmentInfoUpdateRequest request) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
		Department department = member.getDepartment();
		department.updateDepartmentInfo(request.name(), request.rentalPlace(), request.notice());
	}

	public void updateDepartmentRentalTime(String studentId, DepartmentRentalTimeUpdateRequest request) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
		Department department = member.getDepartment();
		department.updateWeeklyRentalTime(request.weeklyRentalTime());
	}
}
