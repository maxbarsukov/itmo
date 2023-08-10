package server.rest.dtos;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class Credentials {
  @NotNull
  @NotEmpty
  @Size(min = 4, max = 15)
  private String name;

  @NotNull
  @NotEmpty
  @Size(min = 4)
  private String password;
}
