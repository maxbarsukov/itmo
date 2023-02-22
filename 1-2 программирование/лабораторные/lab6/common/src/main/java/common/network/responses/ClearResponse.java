package common.network.responses;

import common.network.Response;

public class ClearResponse extends Response {
  public ClearResponse(String error) {
    super("clear", error);
  }
}
