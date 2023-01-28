package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Команда 'head'. Выводит первый элемент коллекции.
 * @author maxbarsukov
 */
public class Head extends Command {
  private final Console console;
  private final CollectionManager collectionManager;

  public Head(Console console, CollectionManager collectionManager) {
    super("head", "вывести первый элемент коллекции");
    this.console = console;
    this.collectionManager = collectionManager;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public boolean apply(String[] arguments) {
    if (!arguments[1].isEmpty()) {
      console.println("Использование: '" + getName() + "'");
      return false;
    }

    if (collectionManager.getCollection().isEmpty()) {
      console.println("Коллекция пуста!");
    } else {
      console.println(collectionManager.getFirst());
    }

    return true;
  }
}
