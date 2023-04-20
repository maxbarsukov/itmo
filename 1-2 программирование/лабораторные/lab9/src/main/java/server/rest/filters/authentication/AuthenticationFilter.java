package server.rest.filters.authentication;


import jakarta.ejb.EJB;
import jakarta.annotation.Priority;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import server.services.auth.AuthService;

import java.util.Optional;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;

@Secured
@Priority(Priorities.AUTHENTICATION)
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
  private static final String AUTHENTICATION_SCHEME = "Bearer";

  @EJB
  private AuthService authService;

  @Override
  public void filter(ContainerRequestContext containerRequestContext) {
    Optional<String> token = getTokenFromContext(containerRequestContext);

    if (!token.isPresent()) {
      containerRequestContext.abortWith(
        Response.status(FORBIDDEN).entity("MISSING_AUTHORIZATION_TOKEN").build()
      );
      return;
    }

    Optional<String> userIdByToken = authService.getUserIdByToken(token.get());
    if (userIdByToken.isEmpty()) {
      containerRequestContext.abortWith(
        Response.status(FORBIDDEN).entity("INVALID_TOKEN").build()
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

