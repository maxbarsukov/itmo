package common.network.requests;

import common.domain.Product;
import common.user.User;
import common.utility.Commands;

public class AddIfMinRequest extends Request {
  public final Product product;

  public AddIfMinRequest(Product product, User user) {
    super(Commands.ADD_IF_MIN, user);
    this.product = product;
  }
}
