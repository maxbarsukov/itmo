package server.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
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
  @Operation(
    summary = "Get current user by token",
    tags = {"Auth"},
    security = {@SecurityRequirement(name = "Bearer")},
    responses = {
      @ApiResponse(responseCode = "200", description = "Current user data and token"),
      @ApiResponse(responseCode = "401", description = "Unauthorized or Bad token format"),
      @ApiResponse(responseCode = "422", description = "Bad user ID in token"),
      @ApiResponse(responseCode = "500", description = "Internal error")
    }
  )
  @Secured
  public Response currentUser(@Context HttpHeaders headers) {
    System.out.println("User #" + headers.getHeaderString("userId") + " is trying to get current user...");
    var userId = getUserId(headers);
    if (userId == -1) {
      return Response.status(422).entity(new ErrorResponse("UNPROCESSABLE_USER_ID_IN_TOKEN").json()).build();
    }

    var result = authService.getUserById(userId);
    if (result.isPresent()) {
      return Response.ok(tokenJson(headers.getHeaderString(AUTHORIZATION).substring(7), result.get())).build();
    }
    return Response.status(FORBIDDEN).entity(new ErrorResponse("CANNOT_GET_USER").json()).build();
  }

  @POST
  @Path("/login")
  @Operation(
    summary = "Login user by credentials",
    tags = {"Auth"},
    responses = {
      @ApiResponse(responseCode = "200", description = "Logged in user data and token"),
      @ApiResponse(responseCode = "400", description = "Bad credentials"),
      @ApiResponse(responseCode = "403", description = "Cannot log in"),
      @ApiResponse(responseCode = "500", description = "Internal error")
    }
  )
  public Response login(
    @Parameter(description = "User credentials", required = true)
    @NotNull(message = "MISSING_CREDENTIALS") @Valid Credentials credentials
  ) {
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
  @Operation(
    summary = "Register user by credentials",
    tags = {"Auth"},
    responses = {
      @ApiResponse(responseCode = "200", description = "Registered in user data and token"),
      @ApiResponse(responseCode = "400", description = "Bad credentials"),
      @ApiResponse(responseCode = "403", description = "Cannot register"),
      @ApiResponse(responseCode = "500", description = "Internal error")
    }
  )
  public Response register(
    @Parameter(description = "User credentials", required = true)
    @NotNull(message = "MISSING_CREDENTIALS") @Valid Credentials credentials
  ) {
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
