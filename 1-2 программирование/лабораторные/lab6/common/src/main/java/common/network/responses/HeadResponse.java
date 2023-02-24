package common.network.responses;

import common.domain.Product;
import common.utility.Commands;

public class HeadResponse extends Response {
  public final Product product;

  public HeadResponse(Product product, String error) {
    super(Commands.HEAD, error);
    this.product = product;
  }
}
