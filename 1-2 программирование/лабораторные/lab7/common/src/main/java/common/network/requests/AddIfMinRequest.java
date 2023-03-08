package common.network.requests;

import common.domain.Product;
import common.utility.Commands;

public class AddIfMinRequest extends Request {
  public final Product product;

  public AddIfMinRequest(Product product) {
    super(Commands.ADD_IF_MIN);
    this.product = product;
  }
}
