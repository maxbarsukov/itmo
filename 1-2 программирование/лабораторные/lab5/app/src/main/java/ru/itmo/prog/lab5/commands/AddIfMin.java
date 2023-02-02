package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.exceptions.IncorrectInputInScriptException;
import ru.itmo.prog.lab5.exceptions.InvalidFormException;
import ru.itmo.prog.lab5.exceptions.WrongAmountOfElementsException;
import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.models.Product;
import ru.itmo.prog.lab5.models.forms.ProductForm;
import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Команда 'add_if_min'. Добавляет новый элемент в коллекцию, если его цена меньше минимальной.
 * @author maxbarsukov
 */
public class AddIfMin extends Command {
  private final Console console;
  private final CollectionManager collectionManager;

  public AddIfMin(Console console, CollectionManager collectionManager) {
    super("add_if_min {element}", "добавить новый элемент в коллекцию, если его цена меньше минимальной цены этой коллекции");
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
      if (!arguments[1].isEmpty()) throw new WrongAmountOfElementsException();
      console.println("* Создание нового продукта (add_if_min):");
      var product = (new ProductForm(console, collectionManager)).build();

      var minPrice = minPrice();
      if (product.getPrice() < minPrice) {
        collectionManager.addToCollection(product);
        console.println("Продукт успешно добавлен!");
      } else {
        console.println("Продукт не добавлен, цена не минимальная (" + product.getPrice() + " > " + minPrice +")");
      }
      return true;

    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    } catch (InvalidFormException exception) {
      console.printError("Поля продукта не валидны! Продукт не создан!");
    } catch (IncorrectInputInScriptException ignored) {}
    return false;
  }

  private Long minPrice() {
    return collectionManager.getCollection().stream()
      .map(Product::getPrice)
      .mapToLong(Long::longValue)
      .min()
      .orElse(Long.MAX_VALUE);
  }
}
