package server.dao;

import common.domain.Product;
import common.domain.UnitOfMeasure;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;

@Entity(name="products")
@Table(name="products", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class ProductDAO implements Serializable {
  public ProductDAO() {
  }

  public ProductDAO(Product product) {
    this.name = product.getName();
    this.x = product.getCoordinates().getX();
    this.y = product.getCoordinates().getY();
    this.creationDate = product.getCreationDate();
    this.price = product.getPrice();
    this.partNumber = product.getPartNumber();
    this.unitOfMeasure = product.getUnitOfMeasure();
  }

  public void update(Product product) {
    this.name = product.getName();
    this.x = product.getCoordinates().getX();
    this.y = product.getCoordinates().getY();
    this.creationDate = product.getCreationDate();
    this.price = product.getPrice();
    this.partNumber = product.getPartNumber();
    this.unitOfMeasure = product.getUnitOfMeasure();
  }

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true, length=11)
  private int id;

  @NotBlank(message = "Название продукта не должно быть пустым.")
  @Column(name="name", nullable=false)
  private String name; // Поле не может быть null, Строка не может быть пустой

  @Column(name="x", nullable=false)
  private int x;

  @Column(name="y", nullable=false)
  private long y;

  @Column(name="creation_date", nullable=false)
  private LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически

  @Min(value = 1L, message = "Цена должна быть больше нуля.")
  @Column(name="price", nullable=false)
  private long price; // Поле не может быть null, Значение поля должно быть больше 0

  @NotBlank(message = "Part number не должно быть пустым.")
  @Column(name="part_number")
  private String partNumber; // Строка не может быть пустой, Поле может быть null

  @Column(name="unit_of_measure")
  @Enumerated(EnumType.STRING)
  private UnitOfMeasure unitOfMeasure; // Поле может быть null

  @ManyToOne
  @JoinColumn(name="manufacturer_id")
  private OrganizationDAO manufacturer; // Поле может быть null

  @ManyToOne
  @JoinColumn(name="creator_id", nullable=false)
  private UserDAO creator;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public long getY() {
    return y;
  }

  public void setY(long y) {
    this.y = y;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public long getPrice() {
    return price;
  }

  public void setPrice(long price) {
    this.price = price;
  }

  public String getPartNumber() {
    return partNumber;
  }

  public void setPartNumber(String partNumber) {
    this.partNumber = partNumber;
  }

  public UnitOfMeasure getUnitOfMeasure() {
    return unitOfMeasure;
  }

  public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
    this.unitOfMeasure = unitOfMeasure;
  }

  public OrganizationDAO getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(OrganizationDAO manufacturer) {
    this.manufacturer = manufacturer;
  }

  public UserDAO getCreator() {
    return creator;
  }

  public void setCreator(UserDAO creator) {
    this.creator = creator;
  }
}
