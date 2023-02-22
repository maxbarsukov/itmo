package server.commands;

import common.exceptions.WrongAmountOfElementsException;
import server.repositories.ProductRepository;
import common.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'filter_contains_part_number'. Фильтрация продуктов по полю partNumber.
 * @author maxbarsukov
 */
public class FilterContainsPartNumber extends Command {
  private final ProductRepository repository;

  public FilterContainsPartNumber(ProductRepository repository) {
    super("filter_contains_part_number <PN>", "вывести элементы, значение поля partNumber которых содержит заданную подстроку");
    this.repository = repository;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public boolean apply(String[] arguments) {
    try {
      if (arguments[1].isEmpty()) throw new WrongAmountOfElementsException();
      var products = filterByPartNumber(arguments[1]);

      if (products.isEmpty()) {
        console.println("Продуктов, чьи partNumber содержат '" + arguments[1] + "' не обнаружено.");
      } else {
        console.println("Продуктов, чьи partNumber содержат '" + arguments[1] + "' обнаружено " + products.size() + " шт.\n");
        products.forEach(console::println);
      }

      return true;
    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    }
    return false;
  }

  private List<Product> filterByPartNumber(String partNumberSubstring) {
    return repository.getCollection().stream()
      .filter(product -> (product.getPartNumber() != null && product.getPartNumber().contains(partNumberSubstring)))
      .collect(Collectors.toList());
  }
}
