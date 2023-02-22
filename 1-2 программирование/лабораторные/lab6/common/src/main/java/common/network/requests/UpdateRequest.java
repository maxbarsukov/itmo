package common.network.requests;

import common.domain.Product;
import common.network.Request;

public class UpdateRequest extends Request {
  public final int id;
  public final Product updatedProduct;

  public UpdateRequest(int id, Product updatedProduct) {
    super("update");
    this.id = id;
    this.updatedProduct = updatedProduct;
  }
}
