package ru.itmo.prog.lab5.commands;

/**
 * Что-то, что можно назвать и описать.
 * @author maxbarsukov
 */
public interface Describable {
  /**
   * Получить имя.
   * @return имя
   */
  String getName();

  /**
   * Получить описание.
   * @return описание
   */
  String getDescription();
}
