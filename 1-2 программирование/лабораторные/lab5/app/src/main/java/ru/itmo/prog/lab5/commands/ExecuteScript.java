package ru.itmo.prog.lab5.commands;

import ru.itmo.prog.lab5.utility.console.Console;

/**
 * Команда 'execute_script'. Выполнить скрипт из файла.
 * @author maxbarsukov
 */
public class ExecuteScript extends Command {
  private final Console console;

  public ExecuteScript(Console console) {
    super("execute_script <file_name>", "исполнить скрипт из указанного файла");
    this.console = console;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public boolean apply(String[] arguments) {
    if (arguments[1].isEmpty()) {
      console.println("Использование: '" + getName() + "'");
      return false;
    }

    console.println("Выполнение скрипта '" + arguments[1] + "'...");
    return true;
  }
}
