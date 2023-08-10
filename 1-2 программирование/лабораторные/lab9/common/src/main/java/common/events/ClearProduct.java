package common.events;

import common.dto.UserDTO;

public class ClearProduct extends Event {
  private final int[] deletedIds;

  public ClearProduct(int[] deletedIds, UserDTO deleter, String requestUuid) {
    super(MessageType.CLEAR, deleter, requestUuid);
    this.deletedIds = deletedIds;
  }

  public int[] getDeletedIds() {
    return deletedIds;
  }
}
