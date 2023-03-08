package server.managers;

import org.apache.logging.log4j.Logger;
import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.common.BaseDataSource;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

import server.App;

public class DatabaseManager {
  private final BaseDataSource connectionPool;
  private final Logger logger = App.logger;

  public DatabaseManager(String url, String user, String password) {
    this.connectionPool = new PGConnectionPoolDataSource();
    connectionPool.setUrl(url);
    connectionPool.setUser(user);
    connectionPool.setPassword(password);
    logger.info("Подключение к базе данных " + url + " от пользователя " + user + ".");
  }

  public Connection getConnection() {
    var delay = 100;

    while(true) {
      try {
        return connectionPool.getConnection();
      } catch (Exception e) {
        logger.error("Не удалось установить соединение! Повторная попытка...");
      }

      try {
        TimeUnit.MILLISECONDS.sleep(delay);
      } catch (InterruptedException e) {
        logger.error("Ожидание переподключения прервано!", e);
      }

      delay *= 1.5;
    }
  }
}
