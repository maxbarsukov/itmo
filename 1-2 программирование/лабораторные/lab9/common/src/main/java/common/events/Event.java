package common.events;

import common.dto.UserDTO;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
public abstract class Event implements Serializable {
  private static final long serialVersionUID = 1L;

  private UserDTO creator;
  private MessageType messageType;
  private String requestUuid;

  public Event(MessageType messageType, UserDTO creator, String requestUuid) {
    this.messageType = messageType;
    this.creator = creator;
    this.requestUuid = requestUuid;
  }
}
