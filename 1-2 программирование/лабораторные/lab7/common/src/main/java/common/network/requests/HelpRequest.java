package common.network.requests;

import common.utility.Commands;

public class HelpRequest extends Request {
  public HelpRequest() {
    super(Commands.HELP);
  }
}
