package client.forms;

import client.utility.console.Console;
import client.utility.Interrogator;
import common.domain.Address;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.InvalidFormException;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;

import java.util.NoSuchElementException;

public class AddressForm extends Form<Address> {
  private final Console console;

  public AddressForm(Console console) {
    this.console = console;
  }

  @Override
  public Address build() throws IncorrectInputInScriptException, InvalidFormException {
    var address = new Address(askStreet(), askZipCode());
    if (!address.validate()) throw new InvalidFormException();
    return address;
  }

  /**
   * Запрашивает у пользователя улицу.
   * @return Улица.
   * @throws IncorrectInputInScriptException Если запущен скрипт и возникает ошибка.
   */
  public String askStreet() throws IncorrectInputInScriptException {
    String street;
    var fileMode = Interrogator.fileMode();
    while (true) {
      try {
        console.println("Введите название улицы:");
        console.ps2();

        street = Interrogator.getUserScanner().nextLine().trim();
        if (fileMode) console.println(street);
        if (street.equals("")) throw new MustBeNotEmptyException();
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Улица не распознана!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (MustBeNotEmptyException exception) {
        console.printError("Улица не может быть пустой!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }

    return street;
  }

  /**
   * Запрашивает у пользователя zip code.
   * @return zip code.
   * @throws IncorrectInputInScriptException Если запущен скрипт и возникает ошибка.
   */
  public String askZipCode() throws IncorrectInputInScriptException {
    String zipCode;
    var fileMode = Interrogator.fileMode();
    while (true) {
      try {
        console.println("Введите zip code (или null):");
        console.ps2();

        zipCode = Interrogator.getUserScanner().nextLine().trim();
        if (fileMode) console.println(zipCode);

        if (zipCode.equals("null") || zipCode.equals("")) return null;
        if (zipCode.length() < 6) throw new NotInDeclaredLimitsException();
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Zip code не распознан!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (NotInDeclaredLimitsException exception) {
        console.printError("Zip code должен быть не меньше 6!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }

    return zipCode;
  }
}
