package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.handlers.CommandHandler;
import server.managers.DumpManager;
import server.managers.CommandManager;
import server.network.UDPDatagramServer;
import server.repositories.ProductRepository;
import server.commands.*;

import java.net.SocketException;

/**
 * Серверная часть приложения.
 * @author maxbarsukov
 */
public class App {
  public static final int PORT = 2345;

  public static Logger logger = LogManager.getLogger("ServerLogger");

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Введите имя загружаемого файла как аргумент командной строки");
      System.exit(1);
    }

    var dumpManager = new DumpManager(args[0]);
    var repository = new ProductRepository(dumpManager);
    repository.validateAll();

    Runtime.getRuntime().addShutdownHook(new Thread(repository::saveCollection));

    var commandManager = new CommandManager() {{
//      register("help", new Help(this));
//      register("info", new Info(repository));
//      register("show", new Show(repository));
      register("add", new Add(repository));
//      register("update", new Update(repository));
//      register("remove_by_id", new RemoveById(repository));
//      register("clear", new Clear(repository));
//      register("execute_script", new ExecuteScript());
//      register("head", new Head(repository));
      register("add_if_max", new AddIfMax(repository));
//      register("add_if_min", new AddIfMin(repository));
//      register("sum_of_price", new SumOfPrice(repository));
//      register("filter_by_price", new FilterByPrice(repository));
//      register("filter_contains_part_number", new FilterContainsPartNumber(repository));
    }};

    try {
      var server = new UDPDatagramServer(PORT, new CommandHandler(commandManager));
      server.run();
    } catch (Exception e) {
      logger.fatal("Случилась ошибка сокета", e);
    }
  }
}
