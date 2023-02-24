package common.network.responses;

import common.utility.Commands;

public class RemoveByIdResponse extends Response {
  public RemoveByIdResponse(String error) {
    super(Commands.REMOVE_BY_ID, error);
  }
}
