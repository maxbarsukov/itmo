package server.commands;

import common.domain.Product;
import common.network.requests.*;
import common.network.responses.*;
import common.utility.ProductComparator;
import server.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'filter_contains_part_number'. Фильтрация продуктов по полю partNumber.
 * @author maxbarsukov
 */
public class FilterContainsPartNumber extends Command {
  private final ProductRepository productRepository;

  public FilterContainsPartNumber(ProductRepository productRepository) {
    super("filter_contains_part_number <PN>", "вывести элементы, значение поля partNumber которых содержит заданную подстроку");
    this.productRepository = productRepository;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public Response apply(Request request) {
    var req = (FilterContainsPartNumberRequest) request;
    try {
      return new FilterContainsPartNumberResponse(filterByPartNumber(req.partNumber), null);
    } catch (Exception e) {
      return new FilterContainsPartNumberResponse(null, e.toString());
    }
  }

  private List<Product> filterByPartNumber(String partNumberSubstring) {
    return productRepository.get().stream()
      .filter(product -> (product.getPartNumber() != null && product.getPartNumber().contains(partNumberSubstring)))
      .sorted(new ProductComparator())
      .collect(Collectors.toList());
  }
}
