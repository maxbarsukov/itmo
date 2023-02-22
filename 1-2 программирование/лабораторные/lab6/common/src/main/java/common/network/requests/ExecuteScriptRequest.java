package common.network.requests;

import common.network.Request;

public class ExecuteScriptRequest extends Request {
  public final String script;

  public ExecuteScriptRequest(String script) {
    super("execute_script");
    this.script = script;
  }
}
