package server.rest.dtos;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class Credentials {
  @NotNull(message = "{\"message\": \"USERNAME_MUST_NOT_BE_NULL_OR_EMPTY\" }")
  @NotEmpty(message = "{\"message\": \"USERNAME_MUST_NOT_BE_NULL_OR_EMPTY\" }")
  @Size(min = 4, message = "{\"message\": \"USERNAME_TOO_SHORT\" }")
  @Size(max = 15, message = "{\"message\": \"USERNAME_TOO_LONG\" }")
  private String name;

  @NotNull(message = "{\"message\": \"PASSWORD_MUST_NOT_BE_NULL_OR_EMPTY\" }")
  @NotEmpty(message = "{\"message\": \"PASSWORD_MUST_NOT_BE_NULL_OR_EMPTY\" }")
  @Size(min = 4, message = "{\"message\": \"PASSWORD_TOO_SHORT\" }")
  private String password;
}
