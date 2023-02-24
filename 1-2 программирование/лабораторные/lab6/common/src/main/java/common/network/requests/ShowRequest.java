package common.network.requests;

import common.utility.Commands;

public class ShowRequest extends Request {
  public ShowRequest() {
    super(Commands.SHOW);
  }
}
