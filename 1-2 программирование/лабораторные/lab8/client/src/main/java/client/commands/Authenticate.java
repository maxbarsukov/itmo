package client.commands;

import client.auth.SessionHandler;
import client.forms.AuthenticateForm;
import client.network.UDPClient;
import client.utility.console.Console;
import common.exceptions.*;
import common.network.requests.AuthenticateRequest;
import common.network.responses.AuthenticateResponse;

import java.io.IOException;

/**
 * Команда 'authenticate'. Аутентифицирует пользователя по логину и паролю.
 * @author maxbarsukov
 */
public class Authenticate extends Command {
  private final Console console;
  private final UDPClient client;

  public Authenticate(Console console, UDPClient client) {
    super("authenticate");
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
      console.println("Вход в аккаунт:");

      var user = (new AuthenticateForm(console)).build();

      var response = (AuthenticateResponse) client.sendAndReceiveCommand(new AuthenticateRequest(user));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      SessionHandler.setCurrentUser(response.user);
      console.println("Пользователь " + response.user.getName() +
        " с id=" + response.user.getId() + " успешно аутентифицирован!");
      return true;

    } catch (WrongAmountOfElementsException exception) {
      console.printError("Неправильное количество аргументов!");
      console.println("Использование: '" + getName() + "'");
    } catch (InvalidFormException exception) {
      console.printError("Введенные данные не валидны! Пользователь на аутентифицирован");
    } catch(IOException e) {
      console.printError("Ошибка взаимодействия с сервером");
    } catch (APIException | ErrorResponseException e) {
      console.printError(e.getMessage());
    } catch (IncorrectInputInScriptException ignored) {}
    return false;
  }
}
