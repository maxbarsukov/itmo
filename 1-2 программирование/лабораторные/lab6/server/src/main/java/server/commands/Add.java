package server.commands;

import common.network.Request;
import common.network.Response;
import common.network.requests.AddRequest;
import common.network.responses.AddResponse;
import server.repositories.ProductRepository;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 * @author maxbarsukov
 */
public class Add extends Command {
  private final ProductRepository productRepository;

  public Add(ProductRepository productRepository) {
    super("add {element}", "добавить новый элемент в коллекцию");
    this.productRepository = productRepository;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public Response apply(Request request) {
    try {
      var newId = productRepository.addToCollection(((AddRequest) request).product);
      return new AddResponse(newId, null);
    } catch (Exception e) {
      return new AddResponse(-1, e.toString());
    }
  }
}
