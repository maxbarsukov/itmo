package common.events;

import common.dto.ProductDTO;
import common.dto.UserDTO;

public class UpdateProduct extends Event {
  private final ProductDTO updatedProduct;

  public UpdateProduct(ProductDTO updatedProduct, UserDTO updater, String requestUuid) {
    super(MessageType.UPDATE, updater, requestUuid);
    this.updatedProduct = updatedProduct;
  }

  public ProductDTO getUpdatedProduct() {
    return updatedProduct;
  }
}
