package server.commands;

import common.network.requests.*;
import common.network.responses.*;
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
    var req = (AddRequest) request;
    try {
      if (!req.product.validate()) {
        return new AddResponse(-1, "Поля продукта не валидны! Продукт не добавлен!");
      }
      var newId = productRepository.add(req.getUser(), req.product);
      return new AddResponse(newId, null);
    } catch (Exception e) {
      return new AddResponse(-1, e.toString());
    }
  }
}
