package ru.itmo.prog.lab5.commands;

/**
 * Интерфейс для всех выполняемых команд.
 * @author maxbarsukov
 */
public interface Executable {
  /**
   * Выполнить что-либо.
   *
   * @param arguments Аргумент для выполнения
   * @return результат выполнения
   */
  boolean apply(String[] arguments);
}
