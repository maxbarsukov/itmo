package server.models;

import common.dto.OrganizationDTO;
import common.dto.OrganizationType;
import common.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="organizations")
public class Organization implements Serializable {
  public Organization(String name, long employeesCount, OrganizationType type, String street, String zipCode) {
    this.name = name;
    this.employeesCount = employeesCount;
    this.type = type;
    this.street = street;
    this.zipCode = zipCode;
  }

  public OrganizationDTO toDTO() {
    return new OrganizationDTO(
      this.id,
      this.name,
      this.employeesCount,
      this.type,
      this.street,
      this.zipCode,
      creator.toDTO()
    );
  }

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true, length=11)
  private int id;

  @NotBlank
  @Column(name="name", nullable=false)
  private String name;

  @Min(value=1L)
  @Column(name="employees_count", nullable=false)
  private long employeesCount;

  @Column(name="type", nullable=false)
  @Enumerated(EnumType.STRING)
  private OrganizationType type;

  @NotBlank
  @Column(name="street", nullable=false)
  private String street;

  @Size(min=6)
  @Column(name="zip_code")
  private String zipCode;

  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
  @JoinColumn(name="manufacturer_id")
  @ToString.Exclude
  private List<Product> products;

  @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
  @JoinColumn(name="creator_id", nullable=false)
  @ToString.Exclude
  private User creator;
}
