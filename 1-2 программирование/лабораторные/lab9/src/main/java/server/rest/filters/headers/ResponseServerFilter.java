package server.rest.filters.headers;

import com.fasterxml.uuid.Generators;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResponseServerFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext requestContext,
                     ContainerResponseContext responseContext) {
    var uuid = Generators.timeBasedGenerator().generate();
    responseContext.getHeaders().add("X-Response-UUID", uuid.toString());
  }
}
