package mango.rentalsystem.domain.member.domain;

import static jakarta.persistence.FetchType.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String studentId;

	private String password;

	private String name;

	private MemberRole role;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "department")
	private Department department;

	private String phone;

	private String pictureUrl;

	@OneToMany(mappedBy = "member")
	private List<Rental> rentalList = new ArrayList<>();

	private boolean absenceStatus;

	private LocalDateTime rentalUnavailableDate;
}
