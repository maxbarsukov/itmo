package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Команда 'save'. Сохраняет коллекцию в файл.
 * @author maxbarsukov
 */
public class Save extends Command {
  private final Console console;
  private final CollectionManager collectionManager;

  public Save(Console console, CollectionManager collectionManager) {
    super("save", "сохранить коллекцию в файл");
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

    collectionManager.saveCollection();
    return true;
  }
}
