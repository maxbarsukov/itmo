package server.utils.validators;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, Enum> {

  List<String> valueList = null;

  @Override
  public boolean isValid(Enum value, ConstraintValidatorContext context) {
    return valueList.contains(value.toString().toUpperCase());
  }

  @Override
  public void initialize(EnumValidator constraintAnnotation) {
    valueList = new ArrayList<>();
    Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

    @SuppressWarnings("rawtypes")
    Enum[] enumValArr = enumClass.getEnumConstants();

    for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
      valueList.add(enumVal.toString().toUpperCase());
    }
  }
}
