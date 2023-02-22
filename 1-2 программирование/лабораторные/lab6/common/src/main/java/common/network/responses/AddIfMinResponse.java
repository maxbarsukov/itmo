package common.network.responses;

import common.network.Response;

public class AddIfMinResponse extends Response {
  public final boolean isAdded;
  public final int newId;

  public AddIfMinResponse(boolean isAdded, int newId, String error) {
    super("add_if_min", error);
    this.isAdded = isAdded;
    this.newId = newId;
  }
}
