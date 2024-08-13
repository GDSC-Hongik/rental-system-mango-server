package mango.rentalsystem.domain.member.dto;

import java.time.LocalDate;

import lombok.Data;
import mango.rentalsystem.domain.member.domain.MemberRole;


// 관리자 수정
@Data
public class UpdateMemberRequestByAdmin {

	private String password;
	private String studentId;
	private String name;
	private String phone;
	private Boolean absenceStatus;
	private MemberRole role;
	private LocalDate rentalBannedDate;;

}
