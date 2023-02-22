package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.exceptions.CollectionIsEmptyException;
import ru.itmo.prog.lab5.exceptions.WrongAmountOfElementsException;
import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.models.Product;
import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Команда 'sum_of_price'. Сумма цен всех продуктов.
 * @author maxbarsukov
 */
public class SumOfPrice extends Command {
  private final Console console;
  private final CollectionManager collectionManager;

  public SumOfPrice(Console console, CollectionManager collectionManager) {
    super("sum_of_price", "вывести сумму значений поля price для всех элементов коллекции");
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

      var sumOfPrice = getSumOfPrice();
      if (sumOfPrice == 0) throw new CollectionIsEmptyException();

      console.println("Сумма цен всех продуктов: " + sumOfPrice);
      return true;
    } catch (WrongAmountOfElementsException exception) {
      console.println("Использование: '" + getName() + "'");
    } catch (CollectionIsEmptyException exception) {
      console.println("Коллекция пуста!");
    }
    return false;
  }

  private Long getSumOfPrice() {
    return collectionManager.getCollection().stream()
      .map(Product::getPrice)
      .mapToLong(Long::longValue)
      .sum();
  }
}
