package common.network.responses;

import common.network.Response;

public class UpdateResponse extends Response {
  public UpdateResponse(String error) {
    super("update", error);
  }
}
