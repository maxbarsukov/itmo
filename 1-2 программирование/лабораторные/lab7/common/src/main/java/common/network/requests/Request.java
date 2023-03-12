package common.network.requests;

import common.user.User;

import java.io.Serializable;
import java.util.Objects;

public abstract class Request implements Serializable {
  private final String name;
  private final User user;

  public Request(String name, User user) {
    this.name = name;
    this.user = user;
  }

  public String getName() {
    return name;
  }

  public User getUser() {
    return user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Request request = (Request) o;
    return Objects.equals(name, request.name) && Objects.equals(user, request.user);
  }

  public boolean isAuth() {
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, user);
  }

  @Override
  public String toString() {
    return "Request{" +
      "name='" + name + '\'' +
      ", user=" + user +
      '}';
  }
}
