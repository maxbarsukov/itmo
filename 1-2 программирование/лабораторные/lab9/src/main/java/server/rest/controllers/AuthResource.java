package server.rest.controllers;

import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.slf4j.Logger;
import server.models.User;
import server.rest.dtos.Credentials;
import server.rest.dtos.ErrorResponse;
import server.services.auth.AuthService;

import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
  @Inject
  private Logger logger;

  @EJB
  private AuthService authService;

  @POST
  @Path("/login")
  public Response login(@NotNull(message = "MISSING_CREDENTIALS") @Valid Credentials credentials) {
    logger.info("User " + credentials.getName() + " is trying to login...");
    var result = authService.login(credentials.getName(), credentials.getPassword());
    if (result.isSuccessful()) {
      logger.info("User " + credentials.getName() + " logged in successfully");
      return Response.ok(tokenJson(result.getToken())).build();
    }
    logger.info("User " + credentials.getName() + " cannot log in, " + result.getErrorMessage());
    return Response.status(FORBIDDEN).entity(new ErrorResponse(result.getErrorMessage()).json()).build();
  }

  @POST
  @Path("/register")
  public Response register(@NotNull(message = "MISSING_CREDENTIALS") @Valid Credentials credentials) {
    System.out.println("HELLO!");
    logger.info("User " + credentials.getName() + " is trying to register...");
    var result = authService.register(credentials.getName(), credentials.getPassword());
    if (result.isSuccessful()) {
      logger.info("User " + credentials.getName() + " registered successfully, ID: " + result.getNewUser().getId());
      return Response.ok(tokenJson(result.getToken(), result.getNewUser())).build();
    }
    logger.info("User " + credentials.getName() + " cannot register, " + result.getErrorMessage());
    return Response.status(FORBIDDEN).entity(new ErrorResponse(result.getErrorMessage()).json()).build();
  }

  private String tokenJson(String token) {
    return Json.createObjectBuilder().add("token", token).build().toString();
  }

  private String tokenJson(String token, User newUser) {
    return Json.createObjectBuilder()
      .add("token", "token")
      .add("user", JsonbBuilder.create().toJson(newUser))
      .build().toString();
  }
}
