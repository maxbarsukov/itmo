package server.rest.filters.headers;

import com.fasterxml.uuid.Generators;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RequestServerFilter implements ContainerRequestFilter {
  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    var uuid = Generators.timeBasedGenerator().generate();
    containerRequestContext.getHeaders().add("requestUUID", uuid.toString());
  }
}
