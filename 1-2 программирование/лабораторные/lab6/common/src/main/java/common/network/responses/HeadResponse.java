package common.network.responses;

import common.domain.Product;
import common.network.Response;

public class HeadResponse extends Response {
  public final Product product;

  public HeadResponse(Product product, String error) {
    super("head", error);
    this.product = product;
  }
}
