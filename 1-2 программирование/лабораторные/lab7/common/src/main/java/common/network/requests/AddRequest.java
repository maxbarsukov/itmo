package common.network.requests;

import common.domain.Product;
import common.utility.Commands;

public class AddRequest extends Request {
  public final Product product;

  public AddRequest(Product product) {
    super(Commands.ADD);
    this.product = product;
  }
}
