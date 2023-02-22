package common.network.responses;

import common.network.Response;

public class ExecuteScriptResponse extends Response {
  public final String logs;

  public ExecuteScriptResponse(String logs, String error) {
    super("execute_script", error);
    this.logs = logs;
  }
}
