package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.exceptions.*;
import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 * @author maxbarsukov
 */
public class RemoveById extends Command {
  private final Console console;
  private final CollectionManager collectionManager;

  public RemoveById(Console console, CollectionManager collectionManager) {
    super("remove_by_id <ID>", "удалить элемент из коллекции по ID");
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
      if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

      var id = Integer.parseInt(arguments[1]);
      var productToRemove = collectionManager.getById(id);
      if (productToRemove == null) throw new NotFoundException();

      collectionManager.removeFromCollection(productToRemove);
      console.println("Продукт успешно удален.");
      return true;

    } catch (WrongAmountOfElementsException exception) {
      console.println("Использование: '" + getName() + "'");
    } catch (CollectionIsEmptyException exception) {
      console.printError("Коллекция пуста!");
    } catch (NumberFormatException exception) {
      console.printError("ID должен быть представлен числом!");
    } catch (NotFoundException exception) {
      console.printError("Продукта с таким ID в коллекции нет!");
    }
    return false;
  }
}
