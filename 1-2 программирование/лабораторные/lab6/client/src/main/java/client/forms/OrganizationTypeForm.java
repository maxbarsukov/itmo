package client.forms;

import client.utility.console.Console;
import client.utility.Interrogator;
import common.domain.OrganizationType;
import common.exceptions.*;

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
