package server.commands;

import server.managers.CommandManager;

/**
 * Команда 'help'. Выводит справку по доступным командам
 * @author maxbarsukov
 */
public class Help extends Command {
  private final CommandManager commandManager;

  public Help(CommandManager commandManager) {
    super("help", "вывести справку по доступным командам");
    this.commandManager = commandManager;
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

    commandManager.getCommands().values().forEach(command -> {
      console.printTable(command.getName(), command.getDescription());
    });
    return true;
  }
}
