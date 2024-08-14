package mango.rentalsystem.domain.member.dto.response;

import mango.rentalsystem.domain.member.domain.Member;

public record MemberFindResponse(
    String studentId,
    String name,
    String phone,
    Boolean absenceStatus,
    String departmentName) {

	public static MemberFindResponse from(Member member) {
		return new MemberFindResponse(
            member.getStudentId(),
            member.getName(),
            member.getPhone(),
            member.getAbsenceStatus(),
            member.getDepartment().getName()
        );
	}
}
