package common.network.responses;

import java.io.Serializable;
import java.util.Objects;

public abstract class Response implements Serializable {
  private final String name;
  private final String error;

  public Response(String name, String error) {
    this.name = name;
    this.error = error;
  }

  public String getName() {
    return name;
  }

  public String getError() {
    return error;
  }

  public boolean isErrorResponse() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Response response = (Response) o;
    return Objects.equals(name, response.name) && Objects.equals(error, response.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, error);
  }

  @Override
  public String toString() {
    return "Response{" +
      "name='" + name + '\'' +
      ", error='" + error + '\'' +
      '}';
  }
}
