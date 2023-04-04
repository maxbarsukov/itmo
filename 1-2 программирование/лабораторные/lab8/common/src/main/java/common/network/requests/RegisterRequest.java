package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class RegisterRequest extends Request {
  public RegisterRequest(User user) {
    super(Commands.REGISTER, user);
  }

  @Override
  public boolean isAuth() {
    return true;
  }
}
