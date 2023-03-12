package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class ClearRequest extends Request {
  public ClearRequest(User user) {
    super(Commands.CLEAR, user);
  }
}
