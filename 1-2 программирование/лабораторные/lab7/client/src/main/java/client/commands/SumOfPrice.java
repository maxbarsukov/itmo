package client.commands;

import client.forms.ProductForm;
import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'sum_of_price'. Сумма цен всех продуктов.
 * @author maxbarsukov
 */
public class SumOfPrice extends Command {
  private final Console console;
  private final UDPClient client;

  public SumOfPrice(Console console, UDPClient client) {
    super("sum_of_price");
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

      var response = (SumOfPriceResponse) client.sendAndReceiveCommand(new SumOfPriceRequest());
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      console.println("Сумма цен всех продуктов: " + response.sum);
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
