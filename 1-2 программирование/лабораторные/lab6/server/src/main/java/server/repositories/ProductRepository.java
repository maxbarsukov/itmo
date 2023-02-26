package server.repositories;

import com.google.common.collect.Iterables;

import common.domain.Organization;
import common.domain.Product;

import common.utility.ProductComparator;
import server.App;
import server.managers.DumpManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    load();
    updateOrganizations();
  }

  public boolean validateAll() {
    for(var product : new ArrayList<>(get())) {
      if (!product.validate()) {
        App.logger.warn("Продукт с id=" + product.getId() + " имеет невалидные поля.");
        return false;
      }
      if (product.getManufacturer() != null) {
        if(!product.getManufacturer().validate()) {
          App.logger.warn("Производитель продукта с id=" + product.getId() + " имеет невалидные поля.");
          return false;
        }
      }
    };
    App.logger.info("! Загруженные продукты валидны.");
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
   * @return Последний элемент коллекции (null если коллекция пустая).
   */
  public Product last() {
    if (collection.isEmpty()) return null;
    return Iterables.getLast(sorted());
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
  public int add(Product element) {
    var maxId = collection.stream().filter(Objects::nonNull)
      .map(Product::getId)
      .mapToInt(Integer::intValue).max().orElse(0);
    var newId = maxId + 1;

    var nextOrgId = collection.stream()
      .map(Product::getManufacturer)
      .filter(Objects::nonNull)
      .map(Organization::getId)
      .mapToInt(Integer::intValue).max().orElse(0) + 1;

    if (element.getManufacturer() != null) {
      element.getManufacturer().setId(nextOrgId);
    }
    collection.add(element.copy(newId));
    return newId;
  }

  /**
   * Удаляет элемент из коллекции.
   * @param id ID элемента для удаления.
   */
  public void remove(int id) {
    collection.removeIf(product -> product.getId() == id);
  }

  /**
   * Очищает коллекцию.
   */
  public void clear() {
    collection.clear();
  }

  /**
   * Сохраняет коллекцию в файл
   */
  public void save() {
    dumpManager.writeCollection(collection);
    lastSaveTime = LocalDateTime.now();
  }

  /**
   * Загружает коллекцию из файла.
   */
  private void load() {
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

    var info = new StringBuilder();
    for (Product product : collection) {
      info.append(product);
      info.append("\n\n");
    }
    return info.substring(0, info.length() - 2);
  }
}
