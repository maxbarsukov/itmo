package common.network.responses;

import common.utility.Commands;

public class AddIfMinResponse extends Response {
  public final boolean isAdded;
  public final int newId;

  public AddIfMinResponse(boolean isAdded, int newId, String error) {
    super(Commands.ADD_IF_MIN, error);
    this.isAdded = isAdded;
    this.newId = newId;
  }
}
