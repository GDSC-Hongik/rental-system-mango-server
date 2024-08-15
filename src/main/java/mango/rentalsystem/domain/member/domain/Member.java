package mango.rentalsystem.domain.member.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.global.exception.CustomException;
import mango.rentalsystem.global.exception.ErrorCode;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private String studentId;

	private String password;

	private String name;

	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	private String phone;

	private String pictureUrl;

	private Boolean absenceStatus;

	private LocalDateTime rentalBannedDate;

	@Builder(access = AccessLevel.PRIVATE)
	private Member(String studentId, String password, String name, MemberRole role, Department department, String phone,
		String pictureUrl, Boolean absenceStatus, LocalDateTime rentalBannedDate) {
		this.studentId = studentId;
		this.password = password;
		this.name = name;
		this.role = role;
		this.department = department;
		this.phone = phone;
		this.pictureUrl = pictureUrl;
		this.absenceStatus = absenceStatus;
		this.rentalBannedDate = rentalBannedDate;
	}

	public static Member createMember(String studentId, String password, String name, Department department,
		String phone) {
		return Member.builder()
			.studentId(studentId)
			.password(password)
			.name(name)
			.role(MemberRole.ROLE_MEMBER)
			.department(department)
			.phone(phone)
			.absenceStatus(false)
			.build();
	}

	public static Member createAdmin(String studentId, String password, String name, Department department,
		String phone) {
		return Member.builder()
			.studentId(studentId)
			.password(password)
			.name(name)
			.role(MemberRole.ROLE_ADMIN)
			.department(department)
			.phone(phone)
			.absenceStatus(false)
			.build();
	}

	public void updateMemberInfo(String name, String phone, Boolean absenceStatus) {
		this.name = name;
		this.phone = phone;
		this.absenceStatus = absenceStatus;
	}

	public void updateMemberPassword(String password) {
		this.password = password;
	}

	public void validateRentalBannedDate() {
		if (rentalBannedDate != null && LocalDateTime.now().isBefore(this.rentalBannedDate)) {
			throw new CustomException(ErrorCode.RENTAL_BANNED);
		}
	}

	public void updateRentalBannedDate() {
		this.rentalBannedDate = LocalDateTime.now().plusDays(3);
	}
}
