package server.repositories;

import com.google.common.collect.Iterables;

import common.domain.Organization;
import common.domain.Product;

import server.App;
import server.managers.DumpManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Оперирует коллекцией.
 * @author maxbarsukov
 */
public class ProductRepository {
  private Queue<Product> collection = new PriorityQueue<>();
  private LocalDateTime lastInitTime;
  private LocalDateTime lastSaveTime;
  private final DumpManager dumpManager;

  public ProductRepository(DumpManager dumpManager) {
    this.lastInitTime = null;
    this.lastSaveTime = null;
    this.dumpManager = dumpManager;

    loadCollection();
    updateOrganizations();
  }

  public void validateAll() {
    (new ArrayList<>(this.getCollection())).forEach(product -> {
      if (!product.validate()) {
        App.logger.warn("Продукт с id=" + product.getId() + " имеет невалидные поля.");
      }
      if (product.getManufacturer() != null) {
        if(!product.getManufacturer().validate()) {
          App.logger.warn("Производитель продукта с id=" + product.getId() + " имеет невалидные поля.");
        }
      }
    });
    App.logger.info("! Загруженные продукты валидны.");
  }

  /**
   * @return коллекция.
   */
  public Queue<Product> getCollection() {
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
  public String collectionType() {
    return collection.getClass().getName();
  }

  /**
   * @return Размер коллекции.
   */
  public int collectionSize() {
    return collection.size();
  }

  /**
   * @return Первый элемент коллекции (null если коллекция пустая).
   */
  public Product getFirst() {
    if (collection.isEmpty()) return null;
    return collection.peek();
  }

  /**
   * @return Последний элемент коллекции (null если коллекция пустая).
   */
  public Product getLast() {
    if (collection.isEmpty()) return null;
    return Iterables.getLast(collection);
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
    for (Product element : collection) {
      if (element.getId() == id) return true;
    }
    return false;
  }

  /**
   * @param elementToFind элемент, который нужно найти по значению.
   * @return Найденный элемент (null если нен найден).
   */
  public Product getByValue(Product elementToFind) {
    for (Product element : collection) {
      if (element.equals(elementToFind)) return element;
    }
    return null;
  }

  /**
   * Добавляет элемент в коллекцию
   * @param element Элемент для добавления.
   * @return id нового элемента
   */
  public int addToCollection(Product element) {
    var maxId = collection.stream().filter(Objects::nonNull)
      .map(Product::getId)
      .mapToInt(Integer::intValue).max().orElse(0);
    var newId = maxId + 1;

    var nextOrgId = collection.stream()
      .map(Product::getManufacturer)
      .filter(Objects::nonNull)
      .map(Organization::getId)
      .mapToInt(Integer::intValue).max().orElse(0) + 1;

    element.getManufacturer().setId(nextOrgId);
    collection.add(element.copy(newId));
    return newId;
  }

  /**
   * Удаляет элемент из коллекции.
   * @param element Элемент для удаления.
   */
  public void removeFromCollection(Product element) {
    collection.remove(element);
  }

  /**
   * Очищает коллекцию.
   */
  public void clearCollection() {
    collection.clear();
  }

  /**
   * Сохраняет коллекцию в файл
   */
  public void saveCollection() {
    dumpManager.writeCollection(collection);
    lastSaveTime = LocalDateTime.now();
  }

  /**
   * Загружает коллекцию из файла.
   */
  private void loadCollection() {
    collection = (PriorityQueue<Product>) dumpManager.readCollection();
    lastInitTime = LocalDateTime.now();
  }

  private void updateOrganizations() {
    collection.stream()
      .map(Product::getManufacturer)
      .filter(Objects::nonNull)
      .forEach(organization -> {
        Organization.put(organization.getId(), organization);
      });
  }

  @Override
  public String toString() {
    if (collection.isEmpty()) return "Коллекция пуста!";
    var last = getLast();

    StringBuilder info = new StringBuilder();
    for (Product product : collection) {
      info.append(product);
      if (product != last) info.append("\n\n");
    }
    return info.toString();
  }
}
