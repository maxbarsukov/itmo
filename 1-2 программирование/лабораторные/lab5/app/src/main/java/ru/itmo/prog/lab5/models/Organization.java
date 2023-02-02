package ru.itmo.prog.lab5.models;

import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.utility.Element;

import java.util.*;

/**
 * Класс организации
 * @author maxbarsukov
 */
public class Organization extends Element {
  private static Integer nextId = 1;
  private static transient Map<Integer, Organization> organizations = new HashMap<>();

  private final Integer id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
  private final String name; // Поле не может быть null, Строка не может быть пустой
  private final long employeesCount; // Значение поля должно быть больше 0
  private final OrganizationType type; // Поле не может быть null
  private final Address postalAddress; // Поле не может быть null

  public Organization(String name, long employeesCount, OrganizationType type, Address postalAddress) {
    this.id = nextId;
    nextId++;
    this.name = name;
    this.employeesCount = employeesCount;
    this.type = type;
    this.postalAddress = postalAddress;
    organizations.put(this.id, this);
  }

  /**
   * Обновляет указатель следующего ID
   * @param collectionManager манагер коллекций
   */
  public static void updateNextId(CollectionManager collectionManager) {
    collectionManager
      .getCollection()
      .stream()
      .map(Product::getManufacturer)
      .filter(Objects::nonNull)
      .forEach(organization -> {
        organizations.put(organization.id, organization);
      });

    var maxId = collectionManager
      .getCollection()
      .stream()
      .map(Product::getManufacturer)
      .filter(Objects::nonNull)
      .map(Organization::getId)
      .mapToInt(Integer::intValue).max().orElse(0);
    nextId = maxId + 1;
  }

  public static Map<Integer, Organization> allOrganizations() {
    return organizations;
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

  public static Organization byId(Integer id) {
    return organizations.get(id);
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
