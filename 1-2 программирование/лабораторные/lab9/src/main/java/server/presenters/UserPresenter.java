package server.presenters;

import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import server.models.User;

@Stateless
public class UserPresenter {
  public JsonObjectBuilder json(User user) {
    return Json.createObjectBuilder()
      .add("id", user.getId())
      .add("name", user.getName());
  }
}
