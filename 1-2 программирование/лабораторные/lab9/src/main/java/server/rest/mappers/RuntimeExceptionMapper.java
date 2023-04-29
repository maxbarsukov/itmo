package server.rest.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
  @Override
  public Response toResponse(RuntimeException exception) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
  }
}
