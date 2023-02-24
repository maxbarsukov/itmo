package server.commands;

import common.network.requests.Request;
import common.network.responses.*;
import server.repositories.ProductRepository;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 * @author maxbarsukov
 */
public class Info extends Command {
  private final ProductRepository productRepository;

  public Info(ProductRepository productRepository) {
    super("info", "вывести информацию о коллекции");
    this.productRepository = productRepository;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public Response apply(Request request) {
    var lastInitTime = productRepository.getLastInitTime();
    var lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
      lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

    var lastSaveTime = productRepository.getLastSaveTime();
    var lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
      lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

    var message = "Сведения о коллекции:\n" +
      " Тип: " + productRepository.type() + "\n" +
      " Количество элементов: " + productRepository.size() + "\n" +
      " Дата последнего сохранения: " + lastSaveTimeString + "\n" +
      " Дата последней инициализации: " + lastInitTimeString;
    return new InfoResponse(message, null);
  }
}
