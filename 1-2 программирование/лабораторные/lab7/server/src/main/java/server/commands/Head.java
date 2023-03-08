package server.commands;

import common.network.requests.Request;
import common.network.responses.*;
import server.repositories.ProductRepository;

/**
 * Команда 'head'. Выводит первый элемент коллекции.
 * @author maxbarsukov
 */
public class Head extends Command {
  private final ProductRepository productRepository;

  public Head(ProductRepository productRepository) {
    super("head", "вывести первый элемент коллекции");
    this.productRepository = productRepository;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public Response apply(Request request) {
    try {
      return new HeadResponse(productRepository.first(), null);
    } catch (Exception e) {
      return new HeadResponse(null, e.toString());
    }
  }
}
