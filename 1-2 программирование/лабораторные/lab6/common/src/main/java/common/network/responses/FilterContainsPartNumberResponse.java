package common.network.responses;

import common.domain.Product;
import common.network.Response;

import java.util.List;

public class FilterContainsPartNumberResponse extends Response {
  public final List<Product> filteredProducts;

  public FilterContainsPartNumberResponse(List<Product> filteredProducts, String error) {
    super("filter_contains_part_number", error);
    this.filteredProducts = filteredProducts;
  }
}
