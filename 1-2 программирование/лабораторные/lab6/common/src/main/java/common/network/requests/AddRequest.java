package common.network.requests;

import common.domain.Product;
import common.network.Request;

public class AddRequest extends Request {
  public final Product product;

  public AddRequest(Product product) {
    super("add");
    this.product = product;
  }
}
