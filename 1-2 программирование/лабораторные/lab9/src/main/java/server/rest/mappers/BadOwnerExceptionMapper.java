package server.rest.mappers;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import server.exceptions.BadOwnerException;

import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;

@Provider
public class BadOwnerExceptionMapper implements ExceptionMapper<BadOwnerException> {
  @Override
  public Response toResponse(BadOwnerException exception) {
    return Response.status(FORBIDDEN)
      .entity(Json.createObjectBuilder().add("message", "YOU_ARE_NOT_THE_OWNER").build())
      .type("application/json")
      .build();
  }
}
