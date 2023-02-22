package common.network.requests;

import common.network.Request;

public class RemoveByIdRequest extends Request {
  public final int id;

  public RemoveByIdRequest(int id) {
    super("remove_by_id");
    this.id = id;
  }
}
