package common.network.requests;

import common.domain.Product;
import common.network.Request;

public class AddIfMinRequest extends Request {
  public final Product product;

  public AddIfMinRequest(Product product) {
    super("add_if_min");
    this.product = product;
  }
}
