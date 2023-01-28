package ru.itmo.prog.lab5.models.forms;

import ru.itmo.prog.lab5.exceptions.IncorrectInputInScriptException;
import ru.itmo.prog.lab5.exceptions.InvalidFormException;

/**
 * Абстрактный класс формы для ввода пользовательских данных.
 * @param <T> создаваемый объект
 * @author maxbarsukov
 */
public abstract class Form<T> {
  public abstract T build() throws IncorrectInputInScriptException, InvalidFormException;
}
