package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Команда 'exit'. Завершает выполнение.
 * @author maxbarsukov
 */
public class Exit extends Command {
  private final Console console;

  public Exit(Console console) {
    super("exit", "завершить программу (без сохранения в файл)");
    this.console = console;
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

    console.println("Завершение выполнения...");
    return true;
  }
}
