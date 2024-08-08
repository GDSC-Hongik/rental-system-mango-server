package mango.rentalsystem.domain.rental.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import mango.rentalsystem.domain.rental.domain.RentalStatus;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RentalStatusSubsetValidator.class)
public @interface RentalStatusSubset {
	RentalStatus[] anyOf();
	String message() default "{anyOf} 중 하나의 상태여야 합니다.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
