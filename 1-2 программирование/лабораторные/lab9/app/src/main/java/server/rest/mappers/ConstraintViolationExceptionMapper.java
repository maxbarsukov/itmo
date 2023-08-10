package server.rest.mappers;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Iterator;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
  @Override
  public Response toResponse(ConstraintViolationException exception) {
    return Response.status(BAD_REQUEST)
      .entity(response(exception).build())
      .type("application/json")
      .build();
  }

  private JsonObjectBuilder response(ConstraintViolationException exception) {
    return Json.createObjectBuilder().add("errors", violationsToJSON(exception));
  }

  private JsonArrayBuilder violationsToJSON(ConstraintViolationException exception) {
    var builder = Json.createArrayBuilder();
    for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
      Iterator<Path.Node> it = violation.getPropertyPath().iterator();

      while (it.hasNext()) {
        var node = it.next();
        if (ElementKind.PROPERTY == node.getKind() || (ElementKind.PARAMETER == node.getKind() && !it.hasNext())) {
          if (node.getKey() != null) {
            builder.add(node.getKey().toString() + " " + violation.getMessage());
          }
          builder.add(node.getName() + " " + violation.getMessage());
        }
      }
    }
    return builder;
  }
}
