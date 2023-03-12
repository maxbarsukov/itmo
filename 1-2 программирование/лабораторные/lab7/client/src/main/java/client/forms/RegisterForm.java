package client.forms;

import client.utility.Interrogator;
import client.utility.console.Console;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.InvalidFormException;
import common.exceptions.MustBeNotEmptyException;
import common.user.User;

import java.util.NoSuchElementException;

public class RegisterForm extends AuthenticateForm {
  private final Console console;

  public RegisterForm(Console console) {
    super(console);
    this.console = console;
  }

  @Override
  public User build() throws IncorrectInputInScriptException, InvalidFormException {
    var user = new User(0, askName(), askPassword());
    if (!user.validate()) throw new InvalidFormException();
    return user;
  }

  /**
   * Запрашивает пароль пользователя и проверяет его.
   * @return Пароль пользователя.
   * @throws IncorrectInputInScriptException Если запущен скрипт и возникает ошибка.
   */
  @Override
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

        console.println("Повторите пароль пользователя:");
        console.ps2();

        var passwordRepeat = readPassword();
        if (fileMode) console.println(passwordRepeat);

        if (!password.equals(passwordRepeat)) throw new InvalidFormException();
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Пароль пользователя не распознан!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (InvalidFormException exception) {
        console.printError("Введенные пароли не совпадают!");
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
}
