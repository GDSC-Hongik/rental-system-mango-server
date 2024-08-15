package mango.rentalsystem.domain.member.dto.response;

import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.member.domain.MemberRole;

public record MemberCreateResponse(
	String studentId,
	String name,
	MemberRole role,
	String departmentName,
	String phone,
	Boolean absenceStatus) {

	public static MemberCreateResponse from(Member member) {
		return new MemberCreateResponse(
			member.getStudentId(),
			member.getName(),
			member.getRole(),
			member.getDepartment().getName(),
			member.getPhone(),
			member.getAbsenceStatus()
		);
	}
}
