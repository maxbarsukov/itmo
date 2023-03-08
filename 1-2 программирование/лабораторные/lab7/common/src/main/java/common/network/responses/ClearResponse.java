package common.network.responses;

import common.utility.Commands;

public class ClearResponse extends Response {
  public ClearResponse(String error) {
    super(Commands.CLEAR, error);
  }
}
