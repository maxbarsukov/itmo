package server.rest.dtos;

import jakarta.validation.Valid;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import server.utils.validators.EnumValidator;
import server.utils.validators.NullOrNotBlank;
import server.models.UnitOfMeasure;

@Data
public class ProductForm {
  @NotNull
  @NotBlank
  private String name;

  @NotNull
  private Integer x;

  @NotNull
  private Long y;

  @NotNull
  @Min(value = 1L)
  private Long price;

  @NullOrNotBlank(message = "PRODUCT_PART_NUMBER_MUST_NOT_BE_EMPTY")
  private String partNumber;

  @EnumValidator(
    enumClazz = UnitOfMeasure.class,
    message = "UOM_MUST_BE_IN_ENUM"
  )
  private UnitOfMeasure unitOfMeasure;

  @Valid
  private OrganizationForm manufacturer;
}
