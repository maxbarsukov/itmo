package client.commands;

import client.forms.ProductForm;
import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.UpdateRequest;
import common.network.responses.UpdateResponse;

import java.io.IOException;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 * @author maxbarsukov
 */
public class Update extends Command {
  private final Console console;
  private final UDPClient client;

  public Update(Console console, UDPClient client) {
    super("update ID {element}");
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

      console.println("* Введите данные обновленного продукта:");
      var updatedProduct = (new ProductForm(console)).build();

      var response = (UpdateResponse) client.sendAndReceiveCommand(new UpdateRequest(id, updatedProduct));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      console.println("Продукт успешно обновлен.");
      return true;

    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    } catch (InvalidFormException exception) {
      console.printError("Поля продукта не валидны! Продукт не создан!");
    } catch (NumberFormatException exception) {
      console.printError("ID должен быть представлен числом!");
    } catch(IOException e) {
      console.printError("Ошибка взаимодействия с сервером");
    } catch (APIException e) {
      console.printError(e.getMessage());
    } catch (IncorrectInputInScriptException ignored) {}
    return false;
  }
}
