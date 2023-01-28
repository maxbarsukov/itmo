package ru.itmo.prog.lab5.models.forms;

import ru.itmo.prog.lab5.exceptions.*;
import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.models.*;
import ru.itmo.prog.lab5.utility.Interrogator;
import ru.itmo.prog.lab5.utility.console.Console;

import java.util.NoSuchElementException;

/**
 * Форма организации.
 * @author maxbarsukov
 */
public class OrganizationForm extends Form<Organization> {
  private final Console console;
  private final CollectionManager collectionManager;

  private final long MIN_EMPLOYEES_COUNT = 1;

  public OrganizationForm(Console console, CollectionManager collectionManager) {
    this.console = console;
    this.collectionManager = collectionManager;
  }

  @Override
  public Organization build() throws IncorrectInputInScriptException, InvalidFormException {
    console.println("Введите null, чтобы оставить организацию пустой или id=x, чтобы использовать организацию. " +
      "Любой другой ввод приведет к созданию новой организации");
    console.ps2();

    var fileMode = Interrogator.fileMode();
    String input = Interrogator.getUserScanner().nextLine().trim();
    if (fileMode) console.println(input);
    if (input.equals("null")) return null;

    try {
      if (input.startsWith("id=") || input.startsWith("ID=")) {
        input = input.replaceFirst("^(id=|ID=)", "");
        int id = Integer.parseInt(input);
        if (id < 1) throw new NotInDeclaredLimitsException();

        var organization = Organization.byId(id);
        if (organization == null) throw new WrongAmountOfElementsException();
        return organization;
      }
    } catch (NotInDeclaredLimitsException exception) {
      console.printError("ID должно быть больше нуля!");
      if (fileMode) throw new IncorrectInputInScriptException();
    } catch (NumberFormatException exception) {
      console.printError("ID должно быть представлено числом!");
      if (fileMode) throw new IncorrectInputInScriptException();
    } catch (NullPointerException | IllegalStateException exception) {
      console.printError("Непредвиденная ошибка!");
      System.exit(0);
    } catch (WrongAmountOfElementsException exception) {
      console.printError("Организации с таким ID не существует.");
    }


    console.println("! Создание новой организации:");
    var organization = new Organization(
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
