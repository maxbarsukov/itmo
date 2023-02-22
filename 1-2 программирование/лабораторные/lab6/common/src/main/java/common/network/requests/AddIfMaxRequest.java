package common.network.requests;

import common.domain.Product;
import common.network.Request;

public class AddIfMaxRequest extends Request {
  public final Product product;

  public AddIfMaxRequest(Product product) {
    super("add_if_max");
    this.product = product;
  }
}
