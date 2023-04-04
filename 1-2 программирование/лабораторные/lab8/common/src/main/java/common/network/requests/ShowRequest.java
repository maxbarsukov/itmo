package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class ShowRequest extends Request {
  public ShowRequest(User user) {
    super(Commands.SHOW, user);
  }
}
