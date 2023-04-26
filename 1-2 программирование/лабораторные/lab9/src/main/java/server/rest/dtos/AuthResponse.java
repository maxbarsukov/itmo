package server.rest.dtos;
import lombok.Getter;

@Getter
public class AuthResponse {
  private final String errorMessage;
  private final String token;
  private final boolean successful;

  private AuthResponse(String errorMessage, String token, boolean successful) {
    this.errorMessage = errorMessage;
    this.token = token;
    this.successful = successful;
  }

  public static AuthResponse message(String message) {
    return new AuthResponse(message, null, false);
  }

  public static AuthResponse token(String token) {
    return new AuthResponse(null, token, true);
  }
}
