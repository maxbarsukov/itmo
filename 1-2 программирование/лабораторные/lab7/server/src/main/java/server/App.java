package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.handlers.CommandHandler;
import server.managers.*;
import server.commands.*;
import server.network.UDPDatagramServer;
import server.repositories.ProductRepository;

import common.utility.Commands;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Серверная часть приложения.
 * @author maxbarsukov
 */
public class App {
  public static final int PORT = 23586;

  public static final Logger logger = LogManager.getLogger("ServerLogger");
  public static Dotenv dotenv;

  public static void main(String[] args) {
    var databaseManager = initializeDatabase();
    var persistenceManager = new PersistenceManager(databaseManager);
//    var authManager = new AuthManager(databaseManager);

    var repository = new ProductRepository(persistenceManager);
    var commandManager = initializeCommandManager(repository);

    try {
      var server = new UDPDatagramServer(InetAddress.getLocalHost(), PORT, new CommandHandler(commandManager)); // CommandHandler(commandManager, authManager));
      server.run();
    } catch (SocketException e) {
      logger.fatal("Случилась ошибка сокета", e);
    } catch (UnknownHostException e) {
      logger.fatal("Неизвестный хост", e);
    }
  }

  private static DatabaseManager initializeDatabase() {
    loadEnv();
    var url = dotenv.get("DB_URL");
    var user = dotenv.get("DB_USER");
    var password = dotenv.get("DB_PASSWORD");

    if (url == null || url.isEmpty() || user == null || user.isEmpty() || password == null || password.isEmpty()) {
      System.out.println("В .env файле не обнаружены данные для подключения к базе данных");
      System.exit(0);
    }

    return new DatabaseManager(url, user, password);
  }

  private static CommandManager initializeCommandManager(ProductRepository repository) {
    return new CommandManager() {{
      register(Commands.HELP, new Help(this));
      register(Commands.INFO, new Info(repository));
      register(Commands.SHOW, new Show(repository));
      register(Commands.ADD, new Add(repository));
      register(Commands.UPDATE, new Update(repository));
      register(Commands.REMOVE_BY_ID, new RemoveById(repository));
      register(Commands.CLEAR, new Clear(repository));
      register(Commands.HEAD, new Head(repository));
      register(Commands.ADD_IF_MAX, new AddIfMax(repository));
      register(Commands.ADD_IF_MIN, new AddIfMin(repository));
      register(Commands.SUM_OF_PRICE, new SumOfPrice(repository));
      register(Commands.FILTER_BY_PRICE, new FilterByPrice(repository));
      register(Commands.FILTER_CONTAINS_PART_NUMBER, new FilterContainsPartNumber(repository));
    }};
  }

  private static void loadEnv() {
    var environmentFile = ".env.dev";
    var isProduction =System.getenv("PROD");
    if (isProduction != null && isProduction.equals("true")) {
      environmentFile = ".env";
    }

    dotenv = Dotenv.configure()
      .filename(environmentFile)
      .ignoreIfMalformed()
      .ignoreIfMissing()
      .load();
  }
}
