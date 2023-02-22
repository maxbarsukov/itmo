package common.network.responses;

import common.domain.Product;
import common.network.Response;

import java.util.List;

public class FilterByPriceResponse extends Response {
  public final List<Product> filteredProducts;

  public FilterByPriceResponse(List<Product> filteredProducts, String error) {
    super("filter_by_price", error);
    this.filteredProducts = filteredProducts;
  }
}
