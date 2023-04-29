package server.rest.controllers;

import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import server.presenters.ProductPresenter;
import server.rest.dtos.ErrorResponse;
import server.rest.filters.authentication.Secured;
import server.services.products.ProductService;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
  @EJB
  private ProductService productService;

  @EJB
  private ProductPresenter productPresenter;

  @GET
  public Response index() {
    return Response.ok(
      productPresenter.json(productService.getAllProducts()).build()
    ).build();
  }

  @GET
  @Path("/{id}")
  public Response show(@PathParam("id") Integer id) {
    return Response.ok(
      productPresenter.json(productService.getProductById(id)).build()
    ).build();
  }

  @Secured
  @POST
  public Response create(@Context HttpHeaders headers) {
    return Response.ok().build();
  }

  @Secured
  @PUT
  @Path("/{id}")
  public Response update(@Context HttpHeaders headers, @PathParam("id") Integer id) {
    return Response.ok().build();
  }

  @Secured
  @DELETE
  @Path("/{id}")
  public Response destroy(@Context HttpHeaders headers, @PathParam("id") Integer id) {
    return Response.ok().build();
  }

  @Secured
  @DELETE
  public Response clear(@Context HttpHeaders headers) {
    var userId = getUserId(headers);
    if (userId == -1) {
      return Response.status(422).entity(new ErrorResponse("UNPROCESSABLE_USER_ID_IN_TOKEN").json()).build();
    }

    var removed = productService.clear(userId);
    return Response.ok(
      Json.createObjectBuilder()
        .add("message", "SUCCESS")
        .add("removedCount", removed)
        .build()
    ).build();
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
