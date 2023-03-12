package server.commands;

import common.network.requests.Request;
import common.network.responses.ClearResponse;
import common.network.responses.Response;
import server.repositories.ProductRepository;

/**
 * Команда 'clear'. Очищает коллекцию.
 * @author maxbarsukov
 */
public class Clear extends Command {
  private final ProductRepository productRepository;

  public Clear(ProductRepository productRepository) {
    super("clear", "очистить коллекцию");
    this.productRepository = productRepository;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public Response apply(Request request) {
    try {
      productRepository.clear(request.getUser());
      return new ClearResponse(null);
    } catch (Exception e) {
      return new ClearResponse(e.toString());
    }
  }
}
