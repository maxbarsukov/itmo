package common.network.responses;

public class BadCredentialsResponse extends ErrorResponse {
  public BadCredentialsResponse(String error) {
    super("bad_credentials", error);
  }
}
