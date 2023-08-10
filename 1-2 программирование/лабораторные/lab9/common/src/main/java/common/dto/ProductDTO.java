package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProductDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;
  private String name;
  private int x;
  private long y;
  private LocalDate creationDate;
  private long price;
  private String partNumber;
  private UnitOfMeasure unitOfMeasure;
  private OrganizationDTO manufacturer;
  private UserDTO creator;

  public ProductDTO(
    int id,
    String name,
    int x,
    long y,
    LocalDate creationDate,
    long price,
    String partNumber,
    UnitOfMeasure unitOfMeasure,
    OrganizationDTO manufacturer,
    UserDTO creator
  ) {
    this.id = id;
    this.name = name;
    this.x = x;
    this.y = y;
    this.creationDate = creationDate;
    this.price = price;
    this.partNumber = partNumber;
    this.unitOfMeasure = unitOfMeasure;
    this.manufacturer = manufacturer;
    this.creator = creator;
  }
}
