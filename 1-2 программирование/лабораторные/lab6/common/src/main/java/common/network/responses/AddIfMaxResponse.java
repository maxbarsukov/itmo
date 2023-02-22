package common.network.responses;

import common.network.Response;

public class AddIfMaxResponse extends Response {
  public final boolean isAdded;
  public final int newId;

  public AddIfMaxResponse(boolean isAdded, int newId, String error) {
    super("add_if_max", error);
    this.isAdded = isAdded;
    this.newId = newId;
  }
}
