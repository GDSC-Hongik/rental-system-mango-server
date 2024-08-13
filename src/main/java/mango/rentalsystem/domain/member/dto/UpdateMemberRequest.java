package mango.rentalsystem.domain.member.dto;

import lombok.Data;


// 멤버 수정
@Data
public class UpdateMemberRequest {

	private String password;
	private String studentId;
	private String name;
	private String phone;
	private Boolean absenceStatus;
}
