package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author maxbarsukov
 */
public class Show extends Command {
  private final Console console;
  private final CollectionManager collectionManager;

  public Show(Console console, CollectionManager collectionManager) {
    super("show", "вывести все элементы коллекции");
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

    console.println(collectionManager);
    return true;
  }
}
