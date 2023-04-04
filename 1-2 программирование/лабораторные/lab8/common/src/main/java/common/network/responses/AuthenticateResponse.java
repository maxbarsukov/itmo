package common.network.responses;

import common.user.User;
import common.utility.Commands;

public class AuthenticateResponse extends Response {
  public final User user;

  public AuthenticateResponse(User user, String error) {
    super(Commands.AUTHENTICATE, error);
    this.user = user;
  }
}
