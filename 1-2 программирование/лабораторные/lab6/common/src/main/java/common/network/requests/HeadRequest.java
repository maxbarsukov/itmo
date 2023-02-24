package common.network.requests;

import common.utility.Commands;

public class HeadRequest extends Request {
  public HeadRequest() {
    super(Commands.HEAD);
  }
}
