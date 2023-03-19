package server.managers;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import server.App;
import server.dao.UserDAO;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class AuthManager {
  private final SessionFactory sessionFactory;
  private final int SALT_LENGTH = 10;
  private final String pepper;

  private final Logger logger = App.logger;

  public AuthManager(SessionFactory sessionFactory, String pepper) {
    this.sessionFactory = sessionFactory;
    this.pepper = pepper;
  }

  public int registerUser(String login, String password) throws SQLException {
    logger.info("Создание нового пользователя " + login);

    var salt = generateSalt();
    var passwordHash = generatePasswordHash(password, salt);

    var dao = new UserDAO();
    dao.setName(login);
    dao.setPasswordDigest(passwordHash);
    dao.setSalt(salt);

    var session = sessionFactory.getCurrentSession();
    session.beginTransaction();
    session.persist(dao);
    session.getTransaction().commit();
    session.close();

    var newId = dao.getId();
    logger.info("Пользователь успешно создан, id#" + newId);
    return newId;
  }

  public int authenticateUser(String login, String password) throws SQLException {
    logger.info("Аутентификация пользователя " + login);
    var session = sessionFactory.getCurrentSession();
    session.beginTransaction();

    var query = session.createQuery("SELECT u FROM users u WHERE u.name = :name");
    query.setParameter("name", login);

    List<UserDAO> result = (List<UserDAO>) query.list();

    if (result.isEmpty()) {
      logger.warn("Неправильный пароль для пользователя " + login);
      return 0;
    }

    var user = result.get(0);
    session.getTransaction().commit();
    session.close();

    var id = user.getId();
    var salt = user.getSalt();
    var expectedHashedPassword = user.getPasswordDigest();

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
