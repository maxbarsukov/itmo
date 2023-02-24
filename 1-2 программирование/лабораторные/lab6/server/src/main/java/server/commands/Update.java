package server.commands;

import common.network.requests.*;
import common.network.responses.*;
import server.repositories.ProductRepository;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 * @author maxbarsukov
 */
public class Update extends Command {
  private final ProductRepository productRepository;

  public Update(ProductRepository productRepository) {
    super("update <ID> {element}", "обновить значение элемента коллекции по ID");
    this.productRepository = productRepository;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public Response apply(Request request) {
    var req = (UpdateRequest) request;
    try {
      if (!productRepository.checkExist(req.id)) {
        return new UpdateResponse("Продукта с таким ID в коллекции нет!");
      }
      if (!req.updatedProduct.validate()) {
        return new UpdateResponse( "Поля продукта не валидны! Продукт не обновлен!");
      }

      productRepository.getById(req.id).update(req.updatedProduct);
      return new UpdateResponse(null);
    } catch (Exception e) {
      return new UpdateResponse(e.toString());
    }
  }
}
