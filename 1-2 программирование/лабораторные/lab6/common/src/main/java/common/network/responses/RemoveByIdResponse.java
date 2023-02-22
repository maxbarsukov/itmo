package common.network.responses;

import common.network.Response;

public class RemoveByIdResponse extends Response {
  public RemoveByIdResponse(String error) {
    super("remove_by_id", error);
  }
}
