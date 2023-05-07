package server.utils.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
public @interface EnumValidator {
  Class<? extends Enum<?>> enumClazz();
  String message() default "Value is not valid";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
