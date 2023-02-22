package common.network.responses;

import common.network.Response;

public class AddResponse extends Response {
  public final int newId;

  public AddResponse(int newId, String error) {
    super("add", error);
    this.newId = newId;
  }
}
