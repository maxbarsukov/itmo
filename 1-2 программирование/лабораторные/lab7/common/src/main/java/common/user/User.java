package common.user;

import common.utility.Element;

public class User extends Element {
  private final int id;
  private final String name;
  private final String password;

  public User(int id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }

  public boolean validate() {
    return getName().length() < 40;
  }

  public User copy(int id) {
    return new User(id, getName(), getPassword());
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", password='********'" +
      '}';
  }

  @Override
  public int compareTo(Element element) {
    return this.getId() - element.getId();
  }
}
