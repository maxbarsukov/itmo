package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class InfoRequest extends Request {
  public InfoRequest(User user) {
    super(Commands.INFO, user);
  }
}
