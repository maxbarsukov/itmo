package client.commands;

import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'clear'. Очищает коллекцию.
 * @author maxbarsukov
 */
public class Clear extends Command {
  private final Console console;
  private final UDPClient client;

  public Clear(Console console, UDPClient client) {
    super("clear");
    this.console = console;
    this.client = client;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public boolean apply(String[] arguments) {
    try {
      if (!arguments[1].isEmpty()) throw new WrongAmountOfElementsException();

      var response = (ClearResponse) client.sendAndReceiveCommand(new ClearRequest());
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      console.println("Коллекция очищена!");
      return true;
    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    } catch(IOException e) {
      console.printError("Ошибка взаимодействия с сервером");
    } catch (APIException e) {
      console.printError(e.getMessage());
    }
    return false;
  }
}
