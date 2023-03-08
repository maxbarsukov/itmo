package client.commands;

import client.network.UDPClient;
import client.utility.console.Console;
import common.network.requests.HelpRequest;
import common.network.responses.HelpResponse;

import java.io.IOException;

/**
 * Команда 'help'. Выводит справку по доступным командам
 * @author maxbarsukov
 */
public class Help extends Command {
  private final Console console;
  private final UDPClient client;

  public Help(Console console, UDPClient client) {
    super("help");
    this.console = console;
    this.client = client;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public boolean apply(String[] arguments) {
    if (!arguments[1].isEmpty()) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
      return false;
    }

    try {
      var response = (HelpResponse) client.sendAndReceiveCommand(new HelpRequest());
      console.print(response.helpMessage);
      return true;
    } catch(IOException e) {
      console.printError("Ошибка взаимодействия с сервером");
    }
    return false;
  }
}
