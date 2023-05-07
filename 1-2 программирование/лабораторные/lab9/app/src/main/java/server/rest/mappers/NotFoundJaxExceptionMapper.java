package server.rest.mappers;

import jakarta.json.Json;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
public class NotFoundJaxExceptionMapper implements ExceptionMapper<NotFoundException> {
  @Override
  public Response toResponse(NotFoundException exception) {
    return Response.status(NOT_FOUND)
      .entity(Json.createObjectBuilder().add("message", "NO_RESULTS_FOUND").add("description", exception.getMessage()).build())
      .type("application/json")
      .build();
  }
}
