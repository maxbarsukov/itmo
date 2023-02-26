package client.forms;

import client.utility.console.Console;
import client.utility.Interrogator;
import common.domain.Address;
import common.domain.Organization;
import common.domain.OrganizationType;
import common.exceptions.*;

import java.util.NoSuchElementException;

/**
 * Форма организации.
 * @author maxbarsukov
 */
public class OrganizationForm extends Form<Organization> {
  private final Console console;

  public OrganizationForm(Console console) {
    this.console = console;
  }

  @Override
  public Organization build() throws IncorrectInputInScriptException, InvalidFormException {
    console.println("Введите null или пустую строку, чтобы оставить организацию пустой. Любой другой ввод приведет к созданию новой организации");
    console.ps2();

    var fileMode = Interrogator.fileMode();
    String input = Interrogator.getUserScanner().nextLine().trim();
    if (fileMode) console.println(input);
    if (input.equals("null") || input.isEmpty()) return null;

    console.println("! Создание новой организации:");
    var organization = new Organization(
      1,
      askName(),
      askEmployeesCount(),
      askType(),
      askPostalAddress()
    );

    if (!organization.validate()) throw new InvalidFormException();
    return organization;
  }

  private String askName() throws IncorrectInputInScriptException {
    String name;
    var fileMode = Interrogator.fileMode();
    while (true) {
      try {
        console.println("Введите название организации:");
        console.ps2();

        name = Interrogator.getUserScanner().nextLine().trim();
        if (fileMode) console.println(name);
        if (name.equals("")) throw new MustBeNotEmptyException();
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Название организации не распознано!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (MustBeNotEmptyException exception) {
        console.printError("Название организации не может быть пустым!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }

    return name;
  }

  private long askEmployeesCount() throws IncorrectInputInScriptException {
    var fileMode = Interrogator.fileMode();
    long employeesCount;
    while (true) {
      try {
        console.println("Введите количество сотрудников:");
        console.ps2();

        var strEmployeesCount = Interrogator.getUserScanner().nextLine().trim();
        if (fileMode) console.println(strEmployeesCount);

        employeesCount = Long.parseLong(strEmployeesCount);
        long MIN_EMPLOYEES_COUNT = 1;
        if (employeesCount < MIN_EMPLOYEES_COUNT) throw new NotInDeclaredLimitsException();
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Количество сотрудников не распознано!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (NotInDeclaredLimitsException exception) {
        console.printError("Количество сотрудников должно быть больше нуля!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (NumberFormatException exception) {
        console.printError("Количество сотрудников должно быть представлено числом!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (NullPointerException | IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }
    return employeesCount;
  }

  private OrganizationType askType() throws IncorrectInputInScriptException {
    return new OrganizationTypeForm(console).build();
  }

  private Address askPostalAddress() throws IncorrectInputInScriptException, InvalidFormException {
    return new AddressForm(console).build();
  }
}
