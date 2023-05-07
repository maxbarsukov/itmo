package server.rest.dtos;
import lombok.Getter;
import server.models.User;
@Getter
public class AuthResponse {
  private final String errorMessage;
  private final String token;
  private final User user;
  private final boolean successful;

  private AuthResponse(String errorMessage, String token, User newUser, boolean successful) {
    this.errorMessage = errorMessage;
    this.token = token;
    this.user = newUser;
    this.successful = successful;
  }

  public static AuthResponse message(String message) {
    return new AuthResponse(message, null, null, false);
  }

  public static AuthResponse registered(String token, User newUser) {
    return new AuthResponse(null, token, newUser, true);
  }

  public static AuthResponse token(String token, User user) {
    return new AuthResponse(null, token, user, true);
  }
}
