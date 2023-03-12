package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class HelpRequest extends Request {
  public HelpRequest(User user) {
    super(Commands.HELP, user);
  }

  @Override
  public boolean isAuth() {
    return true;
  }
}
