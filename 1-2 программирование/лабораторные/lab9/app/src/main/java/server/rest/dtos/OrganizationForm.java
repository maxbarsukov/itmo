package server.rest.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import common.dto.OrganizationType;
import server.utils.validators.EnumValidator;

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

  @NotNull
  @NotBlank
  private String street;

  @Size(min=6)
  private String zipCode;
}
