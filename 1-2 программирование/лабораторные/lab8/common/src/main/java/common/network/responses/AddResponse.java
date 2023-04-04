package common.network.responses;

import common.utility.Commands;

public class AddResponse extends Response {
  public final int newId;

  public AddResponse(int newId, String error) {
    super(Commands.ADD, error);
    this.newId = newId;
  }
}
