package server.rest.filters.authentication;


import jakarta.ejb.EJB;
import jakarta.annotation.Priority;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import server.rest.dtos.ErrorResponse;
import server.services.auth.AuthService;

import java.util.Optional;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

@Secured
@Priority(Priorities.AUTHENTICATION)
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
  private static final String AUTHENTICATION_SCHEME = "Bearer ";

  @EJB
  private AuthService authService;

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    Optional<String> token = getTokenFromContext(containerRequestContext);

    if (token.isEmpty()) {
      containerRequestContext.abortWith(
        Response.status(UNAUTHORIZED).entity(new ErrorResponse("MISSING_AUTHORIZATION_TOKEN").json()).build()
      );
      return;
    }

    if (!token.get().startsWith(AUTHENTICATION_SCHEME)) {
      containerRequestContext.abortWith(
        Response.status(UNAUTHORIZED).entity(new ErrorResponse("BAD_BEARER_TOKEN_FORMAT").json()).build()
      );
      return;
    }

    var authenticationToken = token.get().substring(7);
    Optional<String> userIdByToken = authService.getUserIdByToken(authenticationToken);
    if (userIdByToken.isEmpty()) {
      containerRequestContext.abortWith(
        Response.status(UNAUTHORIZED).entity(new ErrorResponse("INVALID_TOKEN").json()).build()
      );
      return;
    }

    containerRequestContext.getHeaders().add("userId", userIdByToken.get());
  }

  private Optional<String> getTokenFromContext(ContainerRequestContext containerRequestContext) {
    String authHeaderString = containerRequestContext.getHeaderString(AUTHORIZATION);
    return authHeaderString == null ?
      Optional.empty() : Optional.of(authHeaderString.trim());
  }
}

