package common.network.responses;

import common.user.User;
import common.utility.Commands;

public class RegisterResponse extends Response {
  public final User user;

  public RegisterResponse(User user, String error) {
    super(Commands.REGISTER, error);
    this.user = user;
  }
}
