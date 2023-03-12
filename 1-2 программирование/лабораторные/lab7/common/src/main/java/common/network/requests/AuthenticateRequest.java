package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class AuthenticateRequest extends Request {
  public AuthenticateRequest(User user) {
    super(Commands.AUTHENTICATE, user);
  }

  @Override
  public boolean isAuth() {
    return true;
  }
}
