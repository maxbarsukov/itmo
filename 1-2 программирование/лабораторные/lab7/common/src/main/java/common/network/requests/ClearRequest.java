package common.network.requests;

import common.utility.Commands;

public class ClearRequest extends Request {
  public ClearRequest() {
    super(Commands.CLEAR);
  }
}
