package server.commands;

import common.network.requests.*;
import common.network.responses.*;
import org.postgresql.util.PSQLException;
import server.managers.AuthManager;

/**
 * Команда 'register'. Регистрирует пользователя.
 * @author maxbarsukov
 */
public class Register extends Command {
  private final AuthManager authManager;
  private final int MAX_USERNAME_LENGTH = 40;

  public Register(AuthManager authManager) {
    super("register", "зарегистрировать пользователя");
    this.authManager = authManager;
  }

  /**
   * Выполняет команду
   * @param request Запрос к серверу.
   * @return Ответ сервера.
   */
  @Override
  public Response apply(Request request) {
    var req = (RegisterRequest) request;
    var user = req.getUser();
    if (user.getName().length() >= MAX_USERNAME_LENGTH) {
      return new RegisterResponse(user, "Длина имени пользователя должна быть < " + MAX_USERNAME_LENGTH);
    }

    try {
      var newUserId = authManager.registerUser(user.getName(), user.getPassword());

      if (newUserId <= 0) {
        return new RegisterResponse(user, "Не удалось создать пользователя.");
      } else {
        return new RegisterResponse(user.copy(newUserId), null);
      }
    } catch (PSQLException e) {
      var message = "Ошибка PostgreSQL: " + e.getMessage();
      if (e.getMessage().contains("duplicate key value violates unique constraint \"users_name_key\"")) {
        message = "Неуникальное имя пользователя! Попробуйте другое.";
      }
      return new RegisterResponse(user, message);
    } catch (Exception e) {
      return new RegisterResponse(user, e.toString());
    }
  }
}
