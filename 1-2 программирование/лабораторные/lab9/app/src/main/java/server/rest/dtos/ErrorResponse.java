package server.rest.dtos;

import jakarta.json.Json;
import lombok.Getter;

@Getter
public class ErrorResponse {
  private final String message;

  public ErrorResponse(String message) {
    this.message = message;
  }

  public String json() {
    return Json.createObjectBuilder()
      .add("message", this.message)
      .build().toString();
  }
}
