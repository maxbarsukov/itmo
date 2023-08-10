package common.events;

import common.dto.UserDTO;

public class DeleteProduct extends Event {
  private final int deletedId;

  public DeleteProduct(int deletedId, UserDTO deleter, String requestUuid) {
    super(MessageType.DELETE, deleter, requestUuid);
    this.deletedId = deletedId;
  }

  public int getDeletedId() {
    return deletedId;
  }
}
