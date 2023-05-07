package server.rest.controllers;

import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import server.presenters.ProductPresenter;
import server.rest.dtos.ProductForm;
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
  public Response create(
    @Context HttpHeaders headers, @Valid ProductForm productForm
  ) {
    var userId = getUserId(headers);
    if (userId == -1) return badUserId();

    return Response.ok(
      productPresenter.json(productService.add(productForm, userId)).build()
    ).status(Response.Status.CREATED).build();
  }

  @Secured
  @PUT
  @Path("/{id}")
  public Response update(
    @Context HttpHeaders headers, @PathParam("id") Integer id, @Valid ProductForm productForm
  ) {
    var userId = getUserId(headers);
    if (userId == -1) return badUserId();

    return Response.ok(
      productPresenter.json(
        productService.update(id, productForm, userId)).build()
    ).build();
  }

  @Secured
  @DELETE
  public Response clear(@Context HttpHeaders headers) {
    var userId = getUserId(headers);
    if (userId == -1) return badUserId();

    var removed = productService.clear(userId);
    return Response.ok(
      Json.createObjectBuilder()
        .add("success", true)
        .add("removedCount", removed)
        .build()
    ).build();
  }

  @Secured
  @DELETE
  @Path("/{id}")
  public Response destroy(@Context HttpHeaders headers, @PathParam("id") Integer id) {
    var userId = getUserId(headers);
    if (userId == -1) return badUserId();

    var removed = productService.delete(id, userId);
    return Response.ok(
      Json.createObjectBuilder()
        .add("success", true)
        .add("removedCount", removed)
        .build()
    ).build();
  }

  private Response badUserId() {
    return Response.status(422).entity(new ErrorResponse("UNPROCESSABLE_USER_ID_IN_TOKEN").json()).build();
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
