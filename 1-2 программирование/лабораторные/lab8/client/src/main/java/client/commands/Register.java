package client.commands;

import client.auth.SessionHandler;
import client.forms.RegisterForm;
import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.RegisterRequest;
import common.network.responses.RegisterResponse;

import java.io.IOException;

/**
 * Команда 'register'. Регистрирует пользователя.
 * @author maxbarsukov
 */
public class Register extends Command {
  private final Console console;
  private final UDPClient client;

  public Register(Console console, UDPClient client) {
    super("register");
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
      console.println("Создание пользователя:");

      var user = (new RegisterForm(console)).build();

      var response = (RegisterResponse) client.sendAndReceiveCommand(new RegisterRequest(user));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      SessionHandler.setCurrentUser(response.user);
      console.println("Пользователь " + response.user.getName() +
        " с id=" + response.user.getId() + " успешно создан!");
      return true;

    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    } catch (InvalidFormException exception) {
      console.printError("Введенные данные не валидны! Пользователь на зарегистрирован");
    } catch(IOException e) {
      console.printError("Ошибка взаимодействия с сервером");
    } catch (APIException | ErrorResponseException e) {
      console.printError(e.getMessage());
    } catch (IncorrectInputInScriptException ignored) {}
    return false;
  }
}
