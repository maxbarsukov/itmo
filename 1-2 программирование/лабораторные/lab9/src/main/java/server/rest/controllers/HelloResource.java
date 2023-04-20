package server.rest.controllers;

import jakarta.json.Json;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import org.slf4j.Logger;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {
  @Inject
  private Logger logger;

  @GET
  public String hello() {
    logger.debug("HELLO!");
    return jsonMessage("Hello, World!");
  }

  private String jsonMessage(String message) {
    return Json.createObjectBuilder().add("message", message).build().toString();
  }
}
