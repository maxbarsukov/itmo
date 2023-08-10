package server.presenters;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import server.models.Product;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class ProductPresenter {
  @EJB
  private UserPresenter userPresenter;

  @EJB
  private OrganizationPresenter organizationPresenter;

  public JsonObjectBuilder json(Product product) {
    var builder = Json.createObjectBuilder()
      .add("id", product.getId())
      .add("name", product.getName())
      .add("x", product.getX())
      .add("y", product.getY())
      .add("creationDate", product.getCreationDate().format(DateTimeFormatter.ISO_DATE))
      .add("price", product.getPrice())
      .add("partNumber", product.getPartNumber());

    if (product.getUnitOfMeasure() != null) {
      builder.add("unitOfMeasure", product.getUnitOfMeasure().toString());
    } else {
      builder.add("unitOfMeasure", JsonValue.NULL);
    }

    if (product.getManufacturer() != null) {
      builder.add("manufacturer", organizationPresenter.json(product.getManufacturer()));
    } else {
      builder.add("manufacturer", JsonValue.NULL);
    }

    if (product.getCreator() != null) {
      builder.add("creator", userPresenter.json(product.getCreator()));
    } else {
      builder.add("creator", JsonValue.NULL);
    }

    return builder;
  }

  public JsonArrayBuilder json(List<Product> products) {
    var builder = Json.createArrayBuilder();
    products.forEach(product -> {
      builder.add(json(product));
    });
    return builder;
  }
}
