package common.network.requests;

import common.domain.Product;
import common.user.User;
import common.utility.Commands;

public class AddRequest extends Request {
  public final Product product;

  public AddRequest(Product product, User user) {
    super(Commands.ADD, user);
    this.product = product;
  }
}
