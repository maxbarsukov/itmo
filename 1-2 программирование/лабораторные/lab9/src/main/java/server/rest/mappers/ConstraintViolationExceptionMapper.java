package server.rest.mappers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
  @Override
  public Response toResponse(ConstraintViolationException exception) {
    return Response.status(BAD_REQUEST)
      .entity(violationsToJSON(exception))
      .type("application/json")
      .build();
  }

  private String violationsToJSON(ConstraintViolationException exception) {
    for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
      return violation.getMessage();
    }
    return null;
  }
}
