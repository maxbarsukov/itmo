package client.commands;

import client.forms.ProductForm;
import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 * @author maxbarsukov
 */
public class Add extends Command {
  private final Console console;
  private final UDPClient client;

  public Add(Console console, UDPClient client) {
    super("add {element}");
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
      console.println("* Создание нового продукта:");

      var newProduct = (new ProductForm(console)).build();
      var response = (AddResponse) client.sendAndReceiveCommand(new AddRequest(newProduct));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
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
    } catch (APIException e) {
      console.printError(e.getMessage());
    } catch (IncorrectInputInScriptException ignored) {}
    return false;
  }
}
