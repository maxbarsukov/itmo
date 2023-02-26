package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.handlers.CommandHandler;
import server.managers.DumpManager;
import server.managers.CommandManager;
import server.network.UDPDatagramServer;
import server.repositories.ProductRepository;
import server.commands.*;

import common.utility.Commands;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Серверная часть приложения.
 * @author maxbarsukov
 */
public class App {
  public static final int PORT = 23586;

  public static Logger logger = LogManager.getLogger("ServerLogger");

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Введите имя загружаемого файла как аргумент командной строки");
      System.exit(1);
    }

    var dumpManager = new DumpManager(args[0]);
    var repository = new ProductRepository(dumpManager);
    if(!repository.validateAll()) {
      logger.fatal("Невалидные продукты в загруженном файле!");
      System.exit(2);
    }

    Runtime.getRuntime().addShutdownHook(new Thread(repository::save));

    var commandManager = new CommandManager() {{
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

    try {
      var server = new UDPDatagramServer(InetAddress.getLocalHost(), PORT, new CommandHandler(commandManager));
      server.setAfterHook(repository::save);
      server.run();
    } catch (SocketException e) {
      logger.fatal("Случилась ошибка сокета", e);
    } catch (UnknownHostException e) {
      logger.fatal("Неизвестный хост", e);
    }
  }
}
