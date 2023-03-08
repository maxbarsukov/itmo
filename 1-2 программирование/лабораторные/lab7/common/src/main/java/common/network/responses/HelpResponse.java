package common.network.responses;

import common.utility.Commands;

public class HelpResponse extends Response {
  public final String helpMessage;

  public HelpResponse(String helpMessage, String error) {
    super(Commands.HELP, error);
    this.helpMessage = helpMessage;
  }
}
