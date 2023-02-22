package common.network.responses;

import common.network.Response;

public class NoSuchCommandResponse extends Response {
  public NoSuchCommandResponse(String name) {
    super(name, "No such command");
  }
}
