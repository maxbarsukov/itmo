package server.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
  @Operation(
    summary = "Get all products",
    tags = {"Products"},
    responses = {
      @ApiResponse(responseCode = "200", description = "Products"),
      @ApiResponse(responseCode = "500", description = "Internal error")
    }
  )
  public Response index() {
    return Response.ok(
      productPresenter.json(productService.getAllProducts()).build()
    ).build();
  }

  @GET
  @Path("/{id}")
  @Operation(
    summary = "Get product by ID",
    tags = {"Products"},
    responses = {
      @ApiResponse(responseCode = "200", description = "Product"),
      @ApiResponse(responseCode = "404", description = "Product not found"),
      @ApiResponse(responseCode = "500", description = "Internal error")
    }
  )
  public Response show(
    @Parameter(description = "Product to show ID", required = true)
    @PathParam("id") Integer id
  ) {
    return Response.ok(
      productPresenter.json(productService.getProductById(id)).build()
    ).build();
  }

  @Secured
  @POST
  @Operation(
    summary = "Create products",
    tags = {"Products"},
    security = {@SecurityRequirement(name = "Bearer")},
    responses = {
      @ApiResponse(responseCode = "201", description = "Created product"),
      @ApiResponse(responseCode = "400", description = "Invalid product"),
      @ApiResponse(responseCode = "401", description = "Unauthorized or Bad token format"),
      @ApiResponse(responseCode = "422", description = "Bad user ID in token"),
      @ApiResponse(responseCode = "500", description = "Internal error")
    }
  )
  public Response create(
    @Context HttpHeaders headers,
    @Parameter(description = "Created product object", required = true)
    @Valid ProductForm productForm
  ) {
    var userId = getUserId(headers);
    if (userId == -1) return badUserId();

    return Response.ok(
      productPresenter.json(productService.add(productForm, userId, getRequestId(headers))).build()
    ).status(Response.Status.CREATED).build();
  }

  @Secured
  @PUT
  @Path("/{id}")
  @Operation(
    summary = "Update products",
    tags = {"Products"},
    security = {@SecurityRequirement(name = "Bearer")},
    responses = {
      @ApiResponse(responseCode = "200", description = "Updated product"),
      @ApiResponse(responseCode = "400", description = "Invalid product to update"),
      @ApiResponse(responseCode = "401", description = "Unauthorized or Bad token format"),
      @ApiResponse(responseCode = "403", description = "You are not creator"),
      @ApiResponse(responseCode = "404", description = "Product not found"),
      @ApiResponse(responseCode = "422", description = "Bad user ID in token"),
      @ApiResponse(responseCode = "500", description = "Internal error")
    }
  )
  public Response update(
    @Context HttpHeaders headers,
    @Parameter(description = "Product to update ID", required = true)
    @PathParam("id") Integer id,
    @Parameter(description = "Updated product object", required = true)
    @Valid ProductForm productForm
  ) {
    var userId = getUserId(headers);
    if (userId == -1) return badUserId();

    return Response.ok(
      productPresenter.json(
        productService.update(id, productForm, userId, getRequestId(headers))).build()
    ).build();
  }

  @Secured
  @DELETE
  @Operation(
    summary = "Remove all products of current user",
    tags = {"Products"},
    security = {@SecurityRequirement(name = "Bearer")},
    responses = {
      @ApiResponse(responseCode = "200", description = "Cleared products data"),
      @ApiResponse(responseCode = "401", description = "Unauthorized or Bad token format"),
      @ApiResponse(responseCode = "403", description = "You are not creator"),
      @ApiResponse(responseCode = "422", description = "Bad user ID in token"),
      @ApiResponse(responseCode = "500", description = "Internal error")
    }
  )
  public Response clear(@Context HttpHeaders headers) {
    var userId = getUserId(headers);
    if (userId == -1) return badUserId();

    var removed = productService.clear(userId, getRequestId(headers));
    return Response.ok(
      Json.createObjectBuilder()
        .add("success", true)
        .add("removedCount", removed.length)
        .build()
    ).build();
  }

  @Secured
  @DELETE
  @Path("/{id}")
  @Operation(
    summary = "Remove products by ID",
    tags = {"Products"},
    security = {@SecurityRequirement(name = "Bearer")},
    responses = {
      @ApiResponse(responseCode = "200", description = "Cleared product data"),
      @ApiResponse(responseCode = "401", description = "Unauthorized or Bad token format"),
      @ApiResponse(responseCode = "403", description = "You are not creator"),
      @ApiResponse(responseCode = "404", description = "Product not found"),
      @ApiResponse(responseCode = "422", description = "Bad user ID in token"),
      @ApiResponse(responseCode = "500", description = "Internal error")
    }
  )
  public Response destroy(
    @Context HttpHeaders headers,
    @Parameter(description = "Product to remove ID", required = true)
    @PathParam("id") Integer id
  ) {
    var userId = getUserId(headers);
    if (userId == -1) return badUserId();

    var removed = productService.delete(id, userId, getRequestId(headers));
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

  private String getRequestId(HttpHeaders headers) {
    return headers.getHeaderString("requestUUID");
  }
}
