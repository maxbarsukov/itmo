package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;
  private String name;

  public UserDTO(int id, String name) {
    this.id = id;
    this.name = name;
  }
}
