package common.network.responses;

public class ErrorResponse extends Response {
  public ErrorResponse(String error) {
    super("error", error);
  }
}
