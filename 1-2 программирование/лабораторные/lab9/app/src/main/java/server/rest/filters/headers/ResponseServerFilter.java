package server.rest.filters.headers;

import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResponseServerFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext requestContext,
                     ContainerResponseContext responseContext) {
    responseContext
      .getHeaders()
      .add(
        "X-Response-UUID",
        requestContext.getHeaders().get("requestUUID")
      );
  }
}
