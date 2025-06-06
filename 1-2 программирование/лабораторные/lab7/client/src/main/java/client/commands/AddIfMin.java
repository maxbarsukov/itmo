package client.commands;

import client.auth.SessionHandler;
import client.forms.ProductForm;
import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'add_if_min'. Добавляет новый элемент в коллекцию, если его цена меньше минимальной.
 * @author maxbarsukov
 */
public class AddIfMin extends Command {
  private final Console console;
  private final UDPClient client;

  public AddIfMin(Console console, UDPClient client) {
    super("add_if_min {element}");
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
      console.println("* Создание нового продукта (add_if_min):");

      var newProduct = (new ProductForm(console)).build();
      var response = (AddIfMinResponse) client.sendAndReceiveCommand(
        new AddIfMinRequest(newProduct, SessionHandler.getCurrentUser())
      );
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      if (!response.isAdded) {
        console.println("Продукт не добавлен, цена " + newProduct.getPrice() + "не минимальная");
        return true;
      }

      console.println("Новый продукт с id=" + response.newId + " успешно добавлен!");
      return true;

    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    } catch (InvalidFormException exception) {
      console.printError("Поля продукта не валидны! Продукт не создан!");
    } catch(IOException e) {
      console.printError("Ошибка взаимодействия с сервером");
    } catch (APIException | ErrorResponseException e) {
      console.printError(e.getMessage());
    } catch (IncorrectInputInScriptException ignored) {}
    return false;
  }
}
