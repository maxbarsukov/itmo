package client.forms;

import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.InvalidFormException;

/**
 * Абстрактный класс формы для ввода пользовательских данных.
 * @param <T> создаваемый объект
 * @author maxbarsukov
 */
public abstract class Form<T> {
  public abstract T build() throws IncorrectInputInScriptException, InvalidFormException;
}
