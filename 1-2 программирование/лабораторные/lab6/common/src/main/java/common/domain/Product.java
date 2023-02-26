package common.domain;

import common.utility.Element;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс продукта
 * @author maxbarsukov
 */
public class Product extends Element {
  @Serial
  private static final long serialVersionUID = 1L;

  private final int id; // Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
  private String name; // Поле не может быть null, Строка не может быть пустой
  private Coordinates coordinates; // Поле не может быть null
  private LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически
  private Long price; // Поле не может быть null, Значение поля должно быть больше 0
  private String partNumber; // Строка не может быть пустой, Поле может быть null
  private UnitOfMeasure unitOfMeasure; // Поле может быть null
  private Organization manufacturer; // Поле может быть null

  public Product(int id, String name, Coordinates coordinates, LocalDate creationDate,
                 Long price, String partNumber, UnitOfMeasure unitOfMeasure, Organization manufacturer) {
    this.id = id;
    this.name = name;
    this.coordinates = coordinates;
    this.creationDate = creationDate;
    this.price = price;
    this.partNumber = partNumber;
    this.unitOfMeasure = unitOfMeasure;
    this.manufacturer = manufacturer;
  }

  public Product copy(int id) {
    return new Product(id, this.name, this.coordinates, this.creationDate,
      this.price, this.partNumber, this.unitOfMeasure, this.manufacturer
    );
  }

  /**
   * Валидирует правильность полей.
   * @return true, если все верно, иначе false
   */
  @Override
  public boolean validate() {
    if (id <= 0) return false;
    if (name == null || name.isEmpty()) return false;
    if (coordinates == null) return false;
    if (creationDate == null) return false;
    if (price == null || price <= 0) return false;
    return partNumber == null || !partNumber.isEmpty();
  }

  public void update(Product product) {
    this.name = product.name;
    this.coordinates = product.coordinates;
    this.creationDate = product.creationDate;
    this.price = product.price;
    this.partNumber = product.partNumber;
    this.unitOfMeasure = product.unitOfMeasure;
    this.manufacturer = product.manufacturer;
  }

  @Override
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public Long getPrice() {
    return price;
  }

  public String getPartNumber() {
    return partNumber;
  }

  public UnitOfMeasure getUnitOfMeasure() {
    return unitOfMeasure;
  }

  public Organization getManufacturer() {
    return manufacturer;
  }

  @Override
  public int compareTo(Element element) {
    return (this.id - element.getId());
  }

  public int compareTo(Product product) {
    return this.price.compareTo(product.price);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return id == product.id && Objects.equals(name, product.name) && Objects.equals(coordinates, product.coordinates)
      && Objects.equals(creationDate, product.creationDate) && Objects.equals(price, product.price)
      && Objects.equals(partNumber, product.partNumber) && unitOfMeasure == product.unitOfMeasure
      && Objects.equals(manufacturer, product.manufacturer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, coordinates, creationDate, price, partNumber, unitOfMeasure, manufacturer);
  }

  @Override
  public String toString() {
    String info = "";
    info += "Продукт №" + id;
    info += " (добавлен " + creationDate.toString() + ")";
    info += "\n Название: " + name;
    info += "\n Местоположение: " + coordinates;
    info += "\n Цена: " + price;
    info += "\n partNumber: " + ((partNumber == null) ? null : "'" + partNumber + "'");
    info += "\n Единица измерения: " + unitOfMeasure;
    info += "\n Производитель:\n    " + manufacturer;
    return info;
  }
}
