package server.repositories;

import common.domain.Product;

import common.exceptions.BadOwnerException;
import common.user.User;
import common.utility.ProductComparator;
import org.apache.logging.log4j.Logger;
import server.managers.PersistenceManager;
import server.App;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Оперирует коллекцией.
 * @author maxbarsukov
 */
public class ProductRepository {
  private final Logger logger = App.logger;

  private Queue<Product> collection = new PriorityQueue<>();
  private LocalDateTime lastInitTime;
  private LocalDateTime lastSaveTime;
  private final PersistenceManager persistenceManager;

  public ProductRepository(PersistenceManager persistenceManager) {
    this.lastInitTime = null;
    this.lastSaveTime = null;
    this.persistenceManager = persistenceManager;

    try {
      load();
    } catch (Exception e) {
      logger.fatal("Ошибка загрузки продуктов из базы данных!", e);
      System.exit(3);
    }

    if(!validateAll()) {
      logger.fatal("Невалидные продукты в загруженном файле!");
      System.exit(2);
    }
  }

  public boolean validateAll() {
    for(var product : new ArrayList<>(get())) {
      if (!product.validate()) {
        logger.warn("Продукт с id=" + product.getId() + " имеет невалидные поля.");
        return false;
      }
      if (product.getManufacturer() != null) {
        if(!product.getManufacturer().validate()) {
          logger.warn("Производитель продукта с id=" + product.getId() + " имеет невалидные поля.");
          return false;
        }
      }
    };
    logger.info("! Загруженные продукты валидны.");
    return true;
  }

  /**
   * @return коллекция.
   */
  public Queue<Product> get() {
    return collection;
  }

  /**
   * @return Последнее время инициализации.
   */
  public LocalDateTime getLastInitTime() {
    return lastInitTime;
  }

  /**
   * @return Последнее время сохранения.
   */
  public LocalDateTime getLastSaveTime() {
    return lastSaveTime;
  }

  /**
   * @return Имя типа коллекции.
   */
  public String type() {
    return collection.getClass().getName();
  }

  /**
   * @return Размер коллекции.
   */
  public int size() {
    return collection.size();
  }

  /**
   * @return Первый элемент коллекции (null если коллекция пустая).
   */
  public Product first() {
    if (collection.isEmpty()) return null;
    return sorted().get(0);
  }

  /**
   * @return Отсортированная коллекция.
   */
  public List<Product> sorted() {
    return new ArrayList<>(collection)
      .stream()
      .sorted(new ProductComparator())
      .collect(Collectors.toList());
  }

  /**
   * @param id ID элемента.
   * @return Элемент по его ID или null, если не найдено.
   */
  public Product getById(int id) {
    for (Product element : collection) {
      if (element.getId() == id) return element;
    }
    return null;
  }

  /**
   * @param id ID элемента.
   * @return Проверяет, существует ли элемент с таким ID.
   */
  public boolean checkExist(int id) {
    return getById(id) != null;
  }

  /**
   * Добавляет элемент в коллекцию
   * @param element Элемент для добавления.
   * @return id нового элемента
   */
  public int add(User user, Product element) throws SQLException {
    var newId = persistenceManager.add(user, element);
    logger.info("Новый продукт добавлен в БД.");

    collection.add(element.copy(newId));
    lastSaveTime = LocalDateTime.now();
    logger.info("Продукт добавлен!");

    return newId;
  }

  /**
   * Обновляет элемент в коллекции.
   * @param user Пользователь.
   * @param element Элемент для обновления.
   */
  public void update(User user, Product element) throws SQLException, BadOwnerException {
    var product = getById(element.getId());
    if (product == null) {
      add(user, element);
    } else if (product.getCreatorId() == user.getId()) {
      logger.info("Обновление продукта id#" + product.getId() + " в БД.");

      persistenceManager.update(user, element);
      getById(element.getId()).update(element);
      logger.info("Продукт успешно обновлен!ё");
    } else {
      logger.warn("Другой владелец. Исключение.");
      throw new BadOwnerException();
    }
  }

  /**
   * Удаляет элемент из коллекции.
   * @param id ID элемента для удаления.
   * @return количество удаленных продуктов.
   */
  public int remove(User user, int id) throws SQLException, BadOwnerException {
    if (getById(id).getCreatorId() != user.getId()) {
      logger.warn("Другой владелец. Исключение.");
      throw new BadOwnerException();
    }

    var removedCount = persistenceManager.remove(user, id);
    if (removedCount == 0) {
      logger.warn("Ничего не было удалено.");
      return 0;
    }

    collection.removeIf(product -> product.getId() == id && product.getCreatorId() == user.getId());
    lastSaveTime = LocalDateTime.now();
    return removedCount;
  }

  /**
   * Очищает коллекцию.
   */
  public void clear(User user) throws SQLException {
    persistenceManager.clear(user);
    collection.removeIf(product -> product.getCreatorId() == user.getId());
    lastSaveTime = LocalDateTime.now();
  }

  /**
   * Загружает коллекцию из базы данных.
   */
  private void load() throws SQLException {
    logger.info("Загрузка начата...");
    collection = new PriorityQueue<>();
    collection.addAll(persistenceManager.loadProducts());
    lastInitTime = LocalDateTime.now();
    logger.info("Загрузка завершена!");
  }

  @Override
  public String toString() {
    if (collection.isEmpty()) return "Коллекция пуста!";

    var info = new StringBuilder();
    for (Product product : collection) {
      info.append(product);
      info.append("\n\n");
    }
    return info.substring(0, info.length() - 2);
  }
}
