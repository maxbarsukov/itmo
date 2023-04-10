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
    var lastSaveTime = productRepository.getLastSaveTime();
    return new InfoResponse(productRepository.type(), String.valueOf(productRepository.size()), lastSaveTime, lastInitTime, null);
  }
}
