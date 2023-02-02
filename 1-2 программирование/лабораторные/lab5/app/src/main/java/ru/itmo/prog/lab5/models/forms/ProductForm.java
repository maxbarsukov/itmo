package ru.itmo.prog.lab5.models.forms;

import ru.itmo.prog.lab5.exceptions.*;
import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.models.*;
import ru.itmo.prog.lab5.utility.Interrogator;
import ru.itmo.prog.lab5.utility.console.Console;

import java.time.LocalDate;
import java.util.NoSuchElementException;

/**
 * Форма продукта.
 * @author maxbarsukov
 */
public class ProductForm extends Form<Product> {
  private final Console console;
  private final CollectionManager collectionManager;

  private final long MIN_PRICE = 0;

  public ProductForm(Console console, CollectionManager collectionManager) {
    this.console = console;
    this.collectionManager = collectionManager;
  }

  @Override
  public Product build() throws IncorrectInputInScriptException, InvalidFormException {
    var product = new Product(
      askName(),
      askCoordinates(),
      LocalDate.now(),
      askPrice(),
      askPartNumber(),
      askUnitOfMeasureType(),
      askOrganization()
    );
    if (!product.validate()) throw new InvalidFormException();
    return product;
  }

  private String askName() throws IncorrectInputInScriptException {
    String name;
    var fileMode = Interrogator.fileMode();
    while (true) {
      try {
        console.println("Введите название продукта:");
        console.ps2();

        name = Interrogator.getUserScanner().nextLine().trim();
        if (fileMode) console.println(name);
        if (name.equals("")) throw new MustBeNotEmptyException();
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Название не распознано!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (MustBeNotEmptyException exception) {
        console.printError("Название не может быть пустым!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }

    return name;
  }

  private Coordinates askCoordinates() throws IncorrectInputInScriptException, InvalidFormException {
    return new CoordinatesForm(console).build();
  }

  private Long askPrice() throws IncorrectInputInScriptException {
    var fileMode = Interrogator.fileMode();
    long price;
    while (true) {
      try {
        console.println("Введите цену продукта:");
        console.ps2();

        var strPrice = Interrogator.getUserScanner().nextLine().trim();
        if (fileMode) console.println(strPrice);

        price = Long.parseLong(strPrice);
        if (price < MIN_PRICE) throw new NotInDeclaredLimitsException();
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Цена продукта не распознана!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (NotInDeclaredLimitsException exception) {
        console.printError("Цена продукта должна быть больше нуля!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (NumberFormatException exception) {
        console.printError("Цена продукта должна быть представлена числом!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (NullPointerException | IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }
    return price;
  }

  private String askPartNumber() throws IncorrectInputInScriptException {
    String partNumber;
    var fileMode = Interrogator.fileMode();
    while (true) {
      try {
        console.println("Введите номер части продукта:");
        console.ps2();

        partNumber = Interrogator.getUserScanner().nextLine().trim();
        if (fileMode) console.println(partNumber);
        if (partNumber.equals("")) {
          partNumber = null;
        }
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Номер части не распознан!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }

    return partNumber;
  }

  private UnitOfMeasure askUnitOfMeasureType() throws IncorrectInputInScriptException {
    return new UnitOfMeasureForm(console).build();
  }

  private Organization askOrganization() throws IncorrectInputInScriptException, InvalidFormException {
    return new OrganizationForm(console, collectionManager).build();
  }
}
