package common.events;

import common.dto.ProductDTO;
import common.dto.UserDTO;

public class CreateProduct extends Event {
  private final ProductDTO createdProduct;

  public CreateProduct(ProductDTO createdProduct, UserDTO creator, String requestUuid) {
    super(MessageType.CREATE, creator, requestUuid);
    this.createdProduct = createdProduct;
  }

  public ProductDTO getCreatedProduct() {
    return createdProduct;
  }
}
