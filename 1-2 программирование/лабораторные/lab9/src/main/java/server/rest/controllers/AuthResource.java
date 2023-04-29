package server.rest.controllers;

import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import server.models.User;
import server.presenters.UserPresenter;
import server.rest.dtos.Credentials;
import server.rest.dtos.ErrorResponse;
import server.rest.filters.authentication.Secured;
import server.services.auth.AuthService;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
  @EJB
  private AuthService authService;

  @EJB
  private UserPresenter userPresenter;

  @GET
  @Secured
  public Response currentUser(@Context HttpHeaders headers) {
    System.out.println("User #" + headers.getHeaderString("userId") + " is trying to get current user...");
    var userId = getUserId(headers);
    if (userId == -1) {
      return Response.status(422).entity(new ErrorResponse("UNPROCESSABLE_USER_ID_IN_TOKEN").json()).build();
    }

    var result = authService.getUserById(userId);
    if (result.isPresent()) {
      return Response.ok(tokenJson(headers.getHeaderString(AUTHORIZATION), result.get())).build();
    }
    return Response.status(FORBIDDEN).entity(new ErrorResponse("CANNOT_GET_USER").json()).build();
  }

  @POST
  @Path("/login")
  public Response login(@NotNull(message = "MISSING_CREDENTIALS") @Valid Credentials credentials) {
    System.out.println("User " + credentials.getName() + " is trying to login...");
    var result = authService.login(credentials.getName(), credentials.getPassword());
    if (result.isSuccessful()) {
      System.out.println("User " + credentials.getName() + " logged in successfully");
      return Response.ok(tokenJson(result.getToken(), result.getUser())).build();
    }
    System.err.println("User " + credentials.getName() + " cannot log in, " + result.getErrorMessage());
    return Response.status(FORBIDDEN).entity(new ErrorResponse(result.getErrorMessage()).json()).build();
  }

  @POST
  @Path("/register")
  public Response register(@NotNull(message = "MISSING_CREDENTIALS") @Valid Credentials credentials) {
    System.out.println("User " + credentials.getName() + " is trying to register...");
    var result = authService.register(credentials.getName(), credentials.getPassword());
    if (result.isSuccessful()) {
      System.out.println("User " + credentials.getName() + " registered successfully, ID: " + result.getUser().getId());
      return Response.ok(tokenJson(result.getToken(), result.getUser())).build();
    }
    System.err.println("User " + credentials.getName() + " cannot register, " + result.getErrorMessage());
    return Response.status(FORBIDDEN).entity(new ErrorResponse(result.getErrorMessage()).json()).build();
  }

  private String tokenJson(String token, User newUser) {
    return Json.createObjectBuilder()
      .add("token", token)
      .add("user", userPresenter.json(newUser))
      .build().toString();
  }

  private Integer getUserId(HttpHeaders headers) {
    var userId = headers.getHeaderString("userId");
    try {
      return Integer.valueOf(userId);
    } catch (NumberFormatException e) {
      System.err.println("Bad userId from JWT: " + userId);
      return -1;
    }
  }
}
