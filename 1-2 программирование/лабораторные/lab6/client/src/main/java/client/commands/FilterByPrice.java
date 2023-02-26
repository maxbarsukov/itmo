package client.commands;

import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'filter_by_price'. Фильтрация продуктов по цене.
 * @author maxbarsukov
 */
public class FilterByPrice extends Command {
  private final Console console;
  private final UDPClient client;

  public FilterByPrice(Console console, UDPClient client) {
    super("filter_by_price <PRICE>");
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

      var price = Long.parseLong(arguments[1]);
      var response = (FilterByPriceResponse) client.sendAndReceiveCommand(new FilterByPriceRequest(price));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      if (response.filteredProducts.isEmpty()) {
        console.println("Продуктов с ценой " + price + " не обнаружено.");
        return true;
      }

      console.println("Продуктов с ценой " + price + ": " + response.filteredProducts.size() + " шт.\n");
      response.filteredProducts.forEach(console::println);

      return true;
    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    } catch(IOException e) {
      console.printError("Ошибка взаимодействия с сервером");
    } catch (NumberFormatException exception) {
      console.printError("Цена должна быть представлена числом!");
    } catch (APIException e) {
      console.printError(e.getMessage());
    }
    return false;
  }
}
