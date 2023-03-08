package common.network.requests;

import common.domain.Product;
import common.utility.Commands;

public class AddIfMaxRequest extends Request {
  public final Product product;

  public AddIfMaxRequest(Product product) {
    super(Commands.ADD_IF_MAX);
    this.product = product;
  }
}
