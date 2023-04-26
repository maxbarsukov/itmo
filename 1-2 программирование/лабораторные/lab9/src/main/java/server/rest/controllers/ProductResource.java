package server.rest.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import server.rest.filters.authentication.Secured;

@Path("/products")
@Secured
public class ProductResource {
  @Inject
  private Logger logger;
  @GET
  public Response check() {
    logger.info("Success!");
    return Response.ok().entity("Success!!").build();
  }
}
