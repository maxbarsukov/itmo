package server.rest.mappers;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import server.exceptions.NotFoundException;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
  @Override
  public Response toResponse(NotFoundException exception) {
    return Response.status(NOT_FOUND)
      .type("application/json")
      .build();
  }
}
