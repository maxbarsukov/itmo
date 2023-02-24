package common.network.requests;

import common.domain.Product;
import common.utility.Commands;

public class UpdateRequest extends Request {
  public final int id;
  public final Product updatedProduct;

  public UpdateRequest(int id, Product updatedProduct) {
    super(Commands.UPDATE);
    this.id = id;
    this.updatedProduct = updatedProduct;
  }
}
