package client.forms;

import client.utility.Interrogator;
import client.utility.console.Console;
import common.exceptions.*;
import common.user.User;

import java.util.NoSuchElementException;

public class AuthenticateForm extends Form<User> {
  private final Console console;

  public AuthenticateForm(Console console) {
    this.console = console;
  }

  @Override
  public User build() throws IncorrectInputInScriptException, InvalidFormException {
    var user = new User(0, askName(), askPassword());
    if (!user.validate()) throw new InvalidFormException();
    return user;
  }

  /**
   * Запрашивает имя пользователя.
   * @return Имя пользователя.
   * @throws IncorrectInputInScriptException Если запущен скрипт и возникает ошибка.
   */
  protected String askName() throws IncorrectInputInScriptException {
    String name;
    var fileMode = Interrogator.fileMode();
    while (true) {
      try {
        console.println("Введите имя пользователя:");
        console.ps2();

        name = Interrogator.getUserScanner().nextLine().trim();
        if (fileMode) console.println(name);

        if (name.equals("") || name.length() >= 40) throw new MustBeNotEmptyException();
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Имя пользователя не распознано!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (MustBeNotEmptyException exception) {
        console.printError("Размер имени должен быть от 1 до 40 символов!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }

    return name;
  }

  /**
   * Запрашивает пароль пользователя.
   * @return Пароль пользователя.
   * @throws IncorrectInputInScriptException Если запущен скрипт и возникает ошибка.
   */
  protected String askPassword() throws IncorrectInputInScriptException {
    String password;
    var fileMode = Interrogator.fileMode();
    while (true) {
      try {
        console.println("Введите пароль пользователя:");
        console.ps2();

        password = readPassword();
        if (fileMode) console.println(password);

        if (password.equals("")) throw new MustBeNotEmptyException();
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Пароль пользователя не распознан!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (MustBeNotEmptyException exception) {
        console.printError("Пароль не должен быть пустым!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }

    return password;
  }

  protected String readPassword() {
    if (System.console() == null) {
      return Interrogator.getUserScanner().nextLine().trim();
    }
    return new String(System.console().readPassword());
  }
}
