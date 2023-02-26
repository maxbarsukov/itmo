package client.commands;

import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author maxbarsukov
 */
public class Show extends Command {
  private final Console console;
  private final UDPClient client;

  public Show(Console console, UDPClient client) {
    super("show");
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

      var response = (ShowResponse) client.sendAndReceiveCommand(new ShowRequest());
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      if (response.products.isEmpty()) {
        console.println("Коллекция пуста!");
        return true;
      }

      for (var product : response.products) {
        console.println(product + "\n");
      }
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
