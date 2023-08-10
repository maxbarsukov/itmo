package server.models;

import common.dto.OrganizationDTO;
import common.dto.ProductDTO;
import common.dto.UnitOfMeasure;
import common.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import server.rest.dtos.ProductForm;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="products")
public class Product implements Serializable {
  public Product(String name, int x, long y, LocalDate creationDate, long price, String partNumber, UnitOfMeasure unitOfMeasure) {
    this.name = name;
    this.x = x;
    this.y = y;
    this.creationDate = creationDate;
    this.price = price;
    this.partNumber = partNumber;
    this.unitOfMeasure = unitOfMeasure;
  }

  public ProductDTO toDTO() {
    return new ProductDTO(
      this.id,
      this.name,
      this.x,
      this.y,
      this.creationDate,
      this.price,
      this.partNumber,
      this.unitOfMeasure,
      ((this.manufacturer != null) ? manufacturer.toDTO() : null),
      creator.toDTO()
    );
  }

  public Product(ProductForm productForm, User creator) {
    this(
      productForm.getName(),
      productForm.getX(),
      productForm.getY(),
      LocalDate.now(),
      productForm.getPrice(),
      productForm.getPartNumber(),
      productForm.getUnitOfMeasure()
    );

    Organization organization = null;
    if (productForm.getManufacturer() != null) {
      organization = new Organization(
        productForm.getManufacturer().getName(),
        productForm.getManufacturer().getEmployeesCount(),
        productForm.getManufacturer().getType(),
        productForm.getManufacturer().getStreet(),
        productForm.getManufacturer().getZipCode()
      );
      organization.setCreator(creator);
    }

    this.setCreator(creator);
    this.setManufacturer(organization);
  }

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true, length=11)
  private int id;

  @NotBlank
  @Column(name="name", nullable=false)
  private String name;

  @NotNull
  @Column(name="x", nullable=false)
  private int x;

  @Column(name="y", nullable=false)
  private long y;

  @Column(name="creation_date", nullable=false)
  private LocalDate creationDate;

  @Min(value = 1L)
  @Column(name="price", nullable=false)
  private long price;

  @NotBlank
  @Column(name="part_number")
  private String partNumber;

  @Column(name="unit_of_measure")
  @Enumerated(EnumType.STRING)
  private UnitOfMeasure unitOfMeasure;

  @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
  @JoinColumn(name="manufacturer_id")
  @ToString.Exclude
  private Organization manufacturer;

  @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
  @JoinColumn(name="creator_id", nullable=false)
  @ToString.Exclude
  private User creator;
}
