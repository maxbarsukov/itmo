package common.network.requests;

import common.user.User;
import common.utility.Commands;

public class RemoveByIdRequest extends Request {
  public final int id;

  public RemoveByIdRequest(int id, User user) {
    super(Commands.REMOVE_BY_ID, user);
    this.id = id;
  }
}
