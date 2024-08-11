package mango.rentalsystem.domain.rental.validator;

import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mango.rentalsystem.domain.member.domain.MemberRole;
import mango.rentalsystem.domain.rental.domain.RentalStatus;
import mango.rentalsystem.global.security.AuthDetails;

public class RentalStatusSubsetValidator implements ConstraintValidator<RentalStatusSubset, RentalStatus> {
	RentalStatus[] memberSubset;
	RentalStatus[] adminSubset;

	@Override
	public void initialize(RentalStatusSubset constraintAnnotation) {
		this.memberSubset = constraintAnnotation.memberAvailableStatus();
		this.adminSubset = constraintAnnotation.adminAvailableStatus();
	}

	@Override
	public boolean isValid(RentalStatus rentalStatus, ConstraintValidatorContext constraintValidatorContext) {
		MemberRole memberRole = currentMemberRole();
		if (memberRole == MemberRole.ROLE_MEMBER) {
			if (memberSubset == null) {
				return true;
			}
			return Arrays.asList(memberSubset).contains(rentalStatus);
		} else if (memberRole == MemberRole.ROLE_ADMIN) {
			if (adminSubset == null) {
				return true;
			}
			return Arrays.asList(adminSubset).contains(rentalStatus);
		} else {
			throw new IllegalStateException("MemberRole 값에 오류가 있습니다. MemberRole: " + memberRole);
		}
	}

	private MemberRole currentMemberRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
		return MemberRole.valueOf(authDetails.getMemberRole());
	}
}
