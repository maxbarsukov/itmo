package server.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

  @NotBlank(message = "EMPTY_PRODUCT_NAME")
  @Column(name="name", nullable=false)
  private String name;

  @Column(name="x", nullable=false)
  private int x;

  @Column(name="y", nullable=false)
  private long y;

  @Column(name="creation_date", nullable=false)
  private LocalDate creationDate;

  @Min(value = 1L, message = "PRODUCT_PRICE_NOT_POSITIVE")
  @Column(name="price", nullable=false)
  private long price;

  @NotBlank(message = "EMPTY_PRODUCT_PART_NUMBER")
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
