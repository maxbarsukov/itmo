package common.network.responses;

import common.domain.Product;
import common.network.Response;

import java.util.List;

public class ShowResponse extends Response {
  public final List<Product> products;

  public ShowResponse(List<Product> products, String error) {
    super("show", error);
    this.products = products;
  }
}
