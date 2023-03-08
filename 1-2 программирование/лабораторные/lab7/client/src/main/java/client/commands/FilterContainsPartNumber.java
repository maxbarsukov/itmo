package client.commands;

import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'filter_contains_part_number'. Фильтрация продуктов по полю partNumber.
 * @author maxbarsukov
 */
public class FilterContainsPartNumber extends Command {
  private final Console console;
  private final UDPClient client;

  public FilterContainsPartNumber(Console console, UDPClient client) {
    super("filter_contains_part_number <PN>");
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

      var response = (FilterContainsPartNumberResponse) client.sendAndReceiveCommand(
        new FilterContainsPartNumberRequest(arguments[1])
      );
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      if (response.filteredProducts.isEmpty()) {
        console.println("Продуктов, чьи partNumber содержат '" + arguments[1] + "' не обнаружено.");
        return true;
      }

      console.println("Продуктов, чьи partNumber содержат '" + arguments[1] + "' обнаружено " + response.filteredProducts.size() + " шт.\n");
      response.filteredProducts.forEach(console::println);

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
