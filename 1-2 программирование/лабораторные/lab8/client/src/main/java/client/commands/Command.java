package client.commands;

import java.util.Objects;

/**
 * Абстрактная команда с именем и описанием
 * @author maxbarsukov
 */
public abstract class Command {
  private final String name;

  public Command(String name) {
    this.name = name;
  }

  public boolean resolve(String name) {
    return name.equals(this.name);
  }

  /**
   * @return Название и использование команды.
   */
  public String getName() {
    return name;
  }

  /**
   * Выполнить что-либо.
   * @param arguments запрос с данными для выполнения команды
   * @return результат выполнения
   */
  public abstract boolean apply(String[] arguments);

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Command command = (Command) o;
    return Objects.equals(name, command.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return "Command{" +
      "name='" + name + '\'' +
      '}';
  }
}
