package server.rest.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import server.models.OrganizationType;
import server.utils.validators.EnumValidator;
import server.utils.validators.NullOrNotBlank;

@Data
public class OrganizationForm {
  @NotNull
  @NotBlank
  private String name;

  @NotNull
  @Min(value = 1L)
  private Long employeesCount;

  @NotNull
  @EnumValidator(
    enumClazz = OrganizationType.class,
    message = "ORG_TYPE_MUST_BE_IN_ENUM"
  )
  private OrganizationType type;

  @NullOrNotBlank(message="ORG_STREET_MUST_BE_NULL_OR_NOT_BLANK")
  private String street;

  @Size(min=6, message="ORG_ZIP_CODE_GE_6")
  private String zipCode;
}
