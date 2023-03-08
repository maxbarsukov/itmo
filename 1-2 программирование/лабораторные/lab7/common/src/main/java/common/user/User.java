package common.user;

import java.io.Serializable;

public class User implements Serializable {
  private final int id;
  private final String name;
  private final String password;

  public User(int id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
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
      ", password='" + password + '\'' +
      '}';
  }
}
