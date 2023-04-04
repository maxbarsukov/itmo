package common.network.responses;

import common.domain.Product;
import common.utility.Commands;

import java.util.List;

public class ShowResponse extends Response {
  public final List<Product> products;

  public ShowResponse(List<Product> products, String error) {
    super(Commands.SHOW, error);
    this.products = products;
  }
}
