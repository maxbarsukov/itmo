package server.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.services.info.InfoService;
import server.services.products.ProductService;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;


@Path("/info")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InfoResource {
  @EJB
  private InfoService infoService;

  @EJB
  private ProductService productService;

  @GET
  @Operation(
    summary = "Get information about collection & database",
    tags = {"Info"},
    responses = {
      @ApiResponse(responseCode = "200", description = "Information"),
      @ApiResponse(responseCode = "404", description = "Cannot fetch information"),
    }
  )
  public Response info() {
    try {
      return Response.ok(jsonInfo(infoService.info())).build();
    } catch (SQLException e) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  private String jsonInfo(DatabaseMetaData info) throws SQLException {
    return Json.createObjectBuilder()
      .add("productsCount", productService.count())
      .add("databaseName", info.getDatabaseProductName())
      .add("databaseVersion", info.getDatabaseProductVersion())
      .add("driverName", info.getDriverName())
      .add("driverVersion", info.getDriverVersion())
      .add("JDBCMajorVersion", info.getJDBCMajorVersion())
      .add("JDBCMinorVersion", info.getJDBCMinorVersion())
      .add("maxConnections", info.getMaxConnections())
      .build().toString();
  }
}
