package server.commands;

import common.network.requests.Request;
import common.network.responses.*;
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
  public Response apply(Request request) {
    var helpMessage = new StringBuilder();

    commandManager.getCommands().values().forEach(command -> {
      helpMessage.append(" %-35s%-1s%n".formatted(command.getName(), command.getDescription()));
    });

    return new HelpResponse(helpMessage.toString(), null);
  }
}
