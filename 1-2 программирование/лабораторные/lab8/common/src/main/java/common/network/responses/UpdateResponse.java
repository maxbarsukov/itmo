package common.network.responses;

import common.utility.Commands;

public class UpdateResponse extends Response {
  public UpdateResponse(String error) {
    super(Commands.UPDATE, error);
  }
}
