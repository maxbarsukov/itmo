package common.network.responses;

public class ErrorResponse extends Response {
  public ErrorResponse(String name, String error) {
    super(name, error);
  }

  @Override
  public boolean isErrorResponse() {
    return true;
  }
}
