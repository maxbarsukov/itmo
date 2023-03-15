package server.dao;

import common.domain.Organization;
import common.domain.OrganizationType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name="organizations")
@Table(name="organizations", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class OrganizationDAO implements Serializable {
  public OrganizationDAO() {
  }

  public OrganizationDAO(Organization organization) {
    this.name = organization.getName();
    this.employeesCount = organization.getEmployeesCount();
    this.type = organization.getType();
    this.street = organization.getPostalAddress().getStreet();
    this.zipCode = organization.getPostalAddress().getZipCode();
  }

  public void update(Organization organization) {
    this.name = organization.getName();
    this.employeesCount = organization.getEmployeesCount();
    this.type = organization.getType();
    this.street = organization.getPostalAddress().getStreet();
    this.zipCode = organization.getPostalAddress().getZipCode();
  }

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true, length=11)
  private int id;

  @Column(name="name", nullable=false)
  private String name; // Поле не может быть null, Строка не может быть пустой

  @Column(name="employees_count", nullable=false)
  private long employeesCount; // Значение поля должно быть больше 0

  @Column(name="type", nullable=false)
  @Enumerated(EnumType.STRING)
  private OrganizationType type; // Поле не может быть null

  @Column(name="street", nullable=false)
  private String street; // Поле не может быть null

  @Column(name="zip_code", nullable=false)
  private String zipCode; // Поле не может быть null

  @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
  @JoinColumn(name="manufacturer_id")
  private List<ProductDAO> products = new ArrayList<ProductDAO>();

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

  public long getEmployeesCount() {
    return employeesCount;
  }

  public void setEmployeesCount(long employeesCount) {
    this.employeesCount = employeesCount;
  }

  public OrganizationType getType() {
    return type;
  }

  public void setType(OrganizationType type) {
    this.type = type;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public UserDAO getCreator() {
    return creator;
  }

  public void setCreator(UserDAO creator) {
    this.creator = creator;
  }

  public List<ProductDAO> getProducts() {
    return products;
  }

  public void setProducts(List<ProductDAO> products) {
    this.products = products;
  }
}
