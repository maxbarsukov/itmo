package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.exceptions.WrongAmountOfElementsException;
import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.models.Product;
import ru.itmo.prog.lab5.utility.console.Console;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда 'filter_by_price'. Фильтрация продуктов по цене.
 * @author maxbarsukov
 */
public class FilterByPrice extends Command {
  private final Console console;
  private final CollectionManager collectionManager;

  public FilterByPrice(Console console, CollectionManager collectionManager) {
    super("filter_by_price <PRICE>", "вывести элементы, значение поля price которых равно заданному");
    this.console = console;
    this.collectionManager = collectionManager;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public boolean apply(String[] arguments) {
    try {
      if (arguments[1].isEmpty()) throw new WrongAmountOfElementsException();

      var price = Long.parseLong(arguments[1]);
      var products = filterByPrice(price);

      if (products.isEmpty()) {
        console.println("Продуктов с ценой " + price + " не обнаружено.");
      } else {
        console.println("Продуктов с ценой " + price + ": " + products.size() + " шт.\n");
        products.forEach(console::println);
      }

      return true;

    } catch (NumberFormatException exception) {
      console.printError("Цена должна быть представлена числом!");
    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    }
    return false;
  }

  private List<Product> filterByPrice(Long price) {
    return collectionManager.getCollection().stream()
      .filter(product -> (product.getPrice().equals(price)))
      .collect(Collectors.toList());
  }
}
