package client.commands;

import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 * @author maxbarsukov
 */
public class RemoveById extends Command {
  private final Console console;
  private final UDPClient client;

  public RemoveById(Console console, UDPClient client) {
    super("remove_by_id <ID>");
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
      if (arguments[1].isEmpty()) throw new WrongAmountOfElementsException();
      var id = Integer.parseInt(arguments[1]);

      var response = (RemoveByIdResponse) client.sendAndReceiveCommand(new RemoveByIdRequest(id));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      console.println("Продукт успешно удален.");
      return true;
    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    } catch (NumberFormatException exception) {
      console.printError("ID должен быть представлен числом!");
    } catch(IOException e) {
      console.printError("Ошибка взаимодействия с сервером");
    } catch (APIException e) {
      console.printError(e.getMessage());
    }
    return false;
  }
}
