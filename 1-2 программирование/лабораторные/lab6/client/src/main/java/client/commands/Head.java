package client.commands;

import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'head'. Выводит первый элемент коллекции.
 * @author maxbarsukov
 */
public class Head extends Command {
  private final Console console;
  private final UDPClient client;

  public Head(Console console, UDPClient client) {
    super("head");
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

      var response = (HeadResponse) client.sendAndReceiveCommand(new HeadRequest());
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      if (response.product == null) {
        console.println("Коллекция пуста!");
        return true;
      }

      console.println(response.product);
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
