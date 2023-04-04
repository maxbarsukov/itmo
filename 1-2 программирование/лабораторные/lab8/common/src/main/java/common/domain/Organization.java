package common.domain;

import common.utility.Element;

import java.io.Serial;
import java.util.Objects;

/**
 * Класс организации
 * @author maxbarsukov
 */
public class Organization extends Element {
  @Serial
  private static final long serialVersionUID = 1L;

  private Integer id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
  private final String name; // Поле не может быть null, Строка не может быть пустой
  private final long employeesCount; // Значение поля должно быть больше 0
  private final OrganizationType type; // Поле не может быть null
  private final Address postalAddress; // Поле не может быть null

  private int creatorId;

  public Organization(Integer id, String name, long employeesCount, OrganizationType type, Address postalAddress) {
    this.id = id;
    this.name = name;
    this.employeesCount = employeesCount;
    this.type = type;
    this.postalAddress = postalAddress;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Валидирует правильность полей.
   * @return true, если все верно, иначе false
   */
  @Override
  public boolean validate() {
    if (id == null || id <= 0) return false;
    if (name == null || name.isEmpty()) return false;
    if (employeesCount <= 0) return false;
    if (type == null) return false;
    return postalAddress != null;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public long getEmployeesCount() {
    return employeesCount;
  }

  public OrganizationType getType() {
    return type;
  }

  public Address getPostalAddress() {
    return postalAddress;
  }

  public int getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(int creatorId) {
    this.creatorId = creatorId;
  }

  @Override
  public int compareTo(Element element) {
    return (this.id - element.getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Organization that = (Organization) o;
    return employeesCount == that.employeesCount && Objects.equals(id, that.id) && Objects.equals(name, that.name) && type == that.type && Objects.equals(postalAddress, that.postalAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, employeesCount, type, postalAddress);
  }

  @Override
  public String toString() {
    return "Организация \"" + name+ "\" №" + id +
      "; Число сотрудников: " + employeesCount +
      "; Вид: " + type +
      "; Адрес: " + postalAddress;
  }
}
