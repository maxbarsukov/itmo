package client.commands;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.ui.DialogManager;
import client.utility.console.Console;
import common.exceptions.APIException;
import common.exceptions.ErrorResponseException;
import common.network.requests.InfoRequest;
import common.network.responses.InfoResponse;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 * @author maxbarsukov
 */
public class Info extends Command {
  private final Console console;
  private final UDPClient client;

  public Info(Console console, UDPClient client) {
    super("info");
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
      var response = (InfoResponse) client.sendAndReceiveCommand(new InfoRequest(SessionHandler.getCurrentUser()));
      console.println("Type: " + response.type);
      console.println("Size: " + response.size);
      console.println("Last init time: " + response.lastInitTime);
      return true;
    } catch(IOException e) {
      console.printError("Ошибка взаимодействия с сервером");
    } catch (ErrorResponseException e) {
      console.printError(e.getMessage());
    }
    return false;
  }
}
