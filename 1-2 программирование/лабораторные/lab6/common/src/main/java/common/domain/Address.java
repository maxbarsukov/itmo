package common.domain;

import common.utility.Validatable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс адреса.
 * @author maxbarsukov
 */
public class Address implements Validatable, Serializable {
  private final String street; // Строка не может быть пустой, Поле не может быть null
  private final String zipCode; // Длина строки должна быть не меньше 6, Поле может быть null

  public Address(String street, String zipCode) {
    this.street = street;
    this.zipCode = zipCode;
  }

  /**
   * Валидирует правильность полей.
   * @return true, если все верно, иначе false
   */
  @Override
  public boolean validate() {
    if (street == null || street.isEmpty()) return false;
    return zipCode == null || zipCode.length() >= 6;
  }

  public String getStreet() {
    return street;
  }

  public String getZipCode() {
    return zipCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return Objects.equals(street, address.street) && Objects.equals(zipCode, address.zipCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(street, zipCode);
  }

  @Override
  public String toString() {
    return "ул. " + street + (zipCode == null ? "" : ", " + zipCode);
  }
}
