package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.utility.console.Console;

import java.time.LocalDateTime;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 * @author maxbarsukov
 */
public class Info extends Command {
  private final Console console;
  private final CollectionManager collectionManager;

  public Info(Console console, CollectionManager collectionManager) {
    super("info", "вывести информацию о коллекции");
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

    LocalDateTime lastInitTime = collectionManager.getLastInitTime();
    String lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
      lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

    LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
    String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
      lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

    console.println("Сведения о коллекции:");
    console.println(" Тип: " + collectionManager.collectionType());
    console.println(" Количество элементов: " + collectionManager.collectionSize());
    console.println(" Дата последнего сохранения: " + lastSaveTimeString);
    console.println(" Дата последней инициализации: " + lastInitTimeString);
    return true;
  }
}
