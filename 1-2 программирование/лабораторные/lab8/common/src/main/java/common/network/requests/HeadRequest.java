package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class HeadRequest extends Request {
  public HeadRequest(User user) {
    super(Commands.HEAD, user);
  }
}
