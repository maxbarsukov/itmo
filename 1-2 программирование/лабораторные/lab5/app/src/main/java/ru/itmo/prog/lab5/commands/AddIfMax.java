package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.exceptions.IncorrectInputInScriptException;
import ru.itmo.prog.lab5.exceptions.InvalidFormException;
import ru.itmo.prog.lab5.exceptions.WrongAmountOfElementsException;
import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.models.Product;
import ru.itmo.prog.lab5.models.forms.ProductForm;
import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Команда 'add_if_max'. Добавляет новый элемент в коллекцию, если его цена выше максимальной.
 * @author maxbarsukov
 */
public class AddIfMax extends Command {
  private final Console console;
  private final CollectionManager collectionManager;

  public AddIfMax(Console console, CollectionManager collectionManager) {
    super("add_if_max {element}", "добавить новый элемент в коллекцию, если его цена превышает максимальную цену этой коллекции");
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
      console.println("* Создание нового продукта (add_if_max):");
      var product = (new ProductForm(console, collectionManager)).build();

      var maxPrice = maxPrice();
      if (product.getPrice() > maxPrice) {
        collectionManager.addToCollection(product);
        console.println("Продукт успешно добавлен!");
      } else {
        console.println("Продукт не добавлен, цена не максимальная (" + product.getPrice() + " < " + maxPrice +")");
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

  private Long maxPrice() {
    return collectionManager.getCollection().stream()
      .map(Product::getPrice)
      .mapToLong(Long::longValue)
      .max()
      .orElse(-1);
  }
}
