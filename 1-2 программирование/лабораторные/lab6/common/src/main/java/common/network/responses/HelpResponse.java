package common.network.responses;

import common.network.Response;

public class HelpResponse extends Response {
  public final String helpMessage;

  public HelpResponse(String helpMessage, String error) {
    super("help", error);
    this.helpMessage = helpMessage;
  }
}
