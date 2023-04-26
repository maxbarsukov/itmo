package server.rest.dtos;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class Credentials {
  @NotNull(message = "USERNAME_MUST_NOT_BE_NULL_OR_EMPTY")
  @NotEmpty(message = "USERNAME_MUST_NOT_BE_NULL_OR_EMPTY")
  @Size(min = 4, message = "USERNAME_TOO_SHORT")
  @Size(max = 15, message = "USERNAME_TOO_LONG")
  private String name;

  @NotNull(message = "PASSWORD_MUST_NOT_BE_NULL_OR_EMPTY")
  @NotEmpty(message = "PASSWORD_MUST_NOT_BE_NULL_OR_EMPTY")
  @Size(min = 4, message = "PASSWORD_TOO_SHORT")
  private String password;
}
