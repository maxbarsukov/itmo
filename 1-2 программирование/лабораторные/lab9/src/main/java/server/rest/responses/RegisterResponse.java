package server.rest.responses;
import lombok.Getter;
import server.models.User;
@Getter
public class RegisterResponse {
  private final String errorMessage;
  private final String token;
  private final User newUser;
  private final boolean successful;

  private RegisterResponse(String errorMessage, String token, User newUser, boolean successful) {
    this.errorMessage = errorMessage;
    this.token = token;
    this.newUser = newUser;
    this.successful = successful;
  }

  public static RegisterResponse message(String message) {
    return new RegisterResponse(message, null, null, false);
  }

  public static RegisterResponse registered(String token, User newUser) {
    return new RegisterResponse(null, token, newUser, true);
  }
}
