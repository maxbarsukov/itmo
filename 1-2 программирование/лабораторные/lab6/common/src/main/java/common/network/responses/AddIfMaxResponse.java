package common.network.responses;

import common.utility.Commands;

public class AddIfMaxResponse extends Response {
  public final boolean isAdded;
  public final int newId;

  public AddIfMaxResponse(boolean isAdded, int newId, String error) {
    super(Commands.ADD_IF_MAX, error);
    this.isAdded = isAdded;
    this.newId = newId;
  }
}
