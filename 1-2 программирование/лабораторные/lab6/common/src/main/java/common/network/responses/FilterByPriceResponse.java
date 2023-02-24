package common.network.responses;

import common.domain.Product;
import common.utility.Commands;

import java.util.List;

public class FilterByPriceResponse extends Response {
  public final List<Product> filteredProducts;

  public FilterByPriceResponse(List<Product> filteredProducts, String error) {
    super(Commands.FILTER_BY_PRICE, error);
    this.filteredProducts = filteredProducts;
  }
}
