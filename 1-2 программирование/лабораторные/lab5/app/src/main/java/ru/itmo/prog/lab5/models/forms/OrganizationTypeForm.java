package ru.itmo.prog.lab5.models.forms;

import ru.itmo.prog.lab5.exceptions.IncorrectInputInScriptException;
import ru.itmo.prog.lab5.models.OrganizationType;
import ru.itmo.prog.lab5.utility.Interrogator;
import ru.itmo.prog.lab5.utility.console.Console;

import java.util.NoSuchElementException;

/**
 * Форма типа организации.
 * @author maxbarsukov
 */
public class OrganizationTypeForm extends Form<OrganizationType> {
  private final Console console;

  public OrganizationTypeForm(Console console) {
    this.console = console;
  }

  @Override
  public OrganizationType build() throws IncorrectInputInScriptException {
    var fileMode = Interrogator.fileMode();

    String strOrganizationType;
    OrganizationType organizationType;
    while (true) {
      try {
        console.println("Список типов организации - " + OrganizationType.names());
        console.println("Введите тип организации:");
        console.ps2();

        strOrganizationType = Interrogator.getUserScanner().nextLine().trim();
        if (fileMode) console.println(strOrganizationType);

        organizationType = OrganizationType.valueOf(strOrganizationType.toUpperCase());
        break;
      } catch (NoSuchElementException exception) {
        console.printError("Тип организации не распознан!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (IllegalArgumentException exception) {
        console.printError("Типа организации нет в списке!");
        if (fileMode) throw new IncorrectInputInScriptException();
      } catch (IllegalStateException exception) {
        console.printError("Непредвиденная ошибка!");
        System.exit(0);
      }
    }
    return organizationType;
  }
}
