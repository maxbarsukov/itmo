package server.managers;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;
import server.App;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class AuthManager {
  private final DatabaseManager databaseManager;
  private final int SALT_LENGTH = 10;
  private final String pepper;

  private final Logger logger = App.logger;

  public AuthManager(DatabaseManager databaseManager, String pepper) {
    this.databaseManager = databaseManager;
    this.pepper = pepper;
  }

  public int registerUser(String login, String password) throws SQLException {
    logger.info("Создание нового пользователя " + login);

    var salt = generateSalt();
    var connection = databaseManager.getConnection();
    var statement = connection.prepareStatement(
      "INSERT INTO users(name, password_digest, salt) VALUES (?, ?, ?) RETURNING id"
    );

    var passwordHash = generatePasswordHash(password, salt);

    statement.setString(1, login);
    statement.setString(2, passwordHash);
    statement.setString(3, salt);

    var result = statement.executeQuery();
    connection.close();

    result.next();
    var newId = result.getInt(1);
    logger.info("Пользователь успешно создан, id#" + newId);
    return newId;
  }

  public int authenticateUser(String login, String password) throws SQLException {
    logger.info("Аутентификация пользователя " + login);

    var connection = databaseManager.getConnection();
    var statement = connection.prepareStatement(
      "SELECT id, password_digest, salt FROM users WHERE name = ?"
    );

    statement.setString(1, login);

    var result = statement.executeQuery();
    connection.close();

    if (!result.next()) {
      logger.warn("Неправильный пароль для пользователя " + login);
      return 0;
    }

    var id = result.getInt("id");
    var salt = result.getString("salt");
    var expectedHashedPassword = result.getString("password_digest");

    var actualHashedPassword = generatePasswordHash(password, salt);
    if (expectedHashedPassword.equals(actualHashedPassword)) {;
      logger.info("Пользователь " + login + " аутентифицирован c id#" + id);
      return id;
    }

    logger.warn(
      "Неправильный пароль для пользователя " + login +
        ". Ожидалось '" + expectedHashedPassword + "', получено '" + actualHashedPassword + "'"
    );
    return 0;
  }

  private String generateSalt() {
    return RandomStringUtils.randomAlphanumeric(SALT_LENGTH);
  }

  private String generatePasswordHash(String password, String salt) {
    return Hashing.sha256()
      .hashString(pepper + password + salt, StandardCharsets.UTF_8)
      .toString();
  }
}
