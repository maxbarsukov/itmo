package server.commands;

import common.domain.Product;
import common.network.requests.*;
import common.network.responses.*;
import common.utility.ProductComparator;
import server.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'filter_by_price'. Фильтрация продуктов по цене.
 * @author maxbarsukov
 */
public class FilterByPrice extends Command {
  private final ProductRepository productRepository;

  public FilterByPrice(ProductRepository productRepository) {
    super("filter_by_price <PRICE>", "вывести элементы, значение поля price которых равно заданному");
    this.productRepository = productRepository;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public Response apply(Request request) {
    var req = (FilterByPriceRequest) request;
    try {
      return new FilterByPriceResponse(filterByPrice(req.price), null);
    } catch (Exception e) {
      return new FilterByPriceResponse(null, e.toString());
    }
  }

  private List<Product> filterByPrice(Long price) {
    return productRepository.get().stream()
      .filter(product -> (product.getPrice().equals(price)))
      .sorted(new ProductComparator())
      .collect(Collectors.toList());
  }
}
