package mango.rentalsystem.domain.member.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import mango.rentalsystem.domain.member.domain.Member;

public record MemberFindResponse(
	String studentId,
	String name,
	String departmentName,
	String phone,
	Boolean absenceStatus,
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime rentalBannedDate) {

	public static MemberFindResponse from(Member member) {
		return new MemberFindResponse(
			member.getStudentId(),
			member.getName(),
			member.getDepartment().getName(),
			member.getPhone(),
			member.getAbsenceStatus(),
			member.getRentalBannedDate()
		);
	}
}
