package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class OrganizationDTO implements Serializable {
  private int id;
  private String name;
  private long employeesCount;
  private OrganizationType type;
  private String street;
  private String zipCode;
  private UserDTO creator;

  public OrganizationDTO(
    int id,
    String name,
    long employeesCount,
    OrganizationType type,
    String street,
    String zipCode,
    UserDTO creator
  ) {
    this.id = id;
    this.name = name;
    this.employeesCount = employeesCount;
    this.type = type;
    this.street = street;
    this.zipCode = zipCode;
    this.creator = creator;
  }
}
