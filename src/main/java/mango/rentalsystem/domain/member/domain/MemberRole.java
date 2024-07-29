package mango.rentalsystem.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
	MEMBER("ROLE_MEMBER"),
	ADMIN("ROLE_ADMIN");

	private final String value;
}
