package common.network.requests;

import common.utility.Commands;

public class InfoRequest extends Request {
  public InfoRequest() {
    super(Commands.INFO);
  }
}
