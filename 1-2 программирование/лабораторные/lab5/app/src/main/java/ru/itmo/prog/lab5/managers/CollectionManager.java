package ru.itmo.prog.lab5.managers;

import com.google.common.collect.Iterables;
import ru.itmo.prog.lab5.models.Organization;
import ru.itmo.prog.lab5.models.Product;
import ru.itmo.prog.lab5.utility.console.Console;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Оперирует коллекцией.
 * @author maxbarsukov
 */
public class CollectionManager {
  private Queue<Product> collection = new PriorityQueue<Product>();
  private LocalDateTime lastInitTime;
  private LocalDateTime lastSaveTime;
  private final DumpManager dumpManager;

  public CollectionManager(DumpManager dumpManager) {
    this.lastInitTime = null;
    this.lastSaveTime = null;
    this.dumpManager = dumpManager;

    loadCollection();
  }

  public void validateAll(Console console) {
    Organization.allOrganizations().values().forEach(organization -> {
      if (!organization.validate()) {
        console.printError("Организация с id=" + organization.getId() + " имеет невалидные поля.");
      }
    });

    (new ArrayList<>(this.getCollection())).forEach(product -> {
      if (!product.validate()) {
        console.printError("Продукт с id=" + product.getId() + " имеет невалидные поля.");
      }
    });
    console.println("! Загруженные продукты валидны.");
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
   */
  public void addToCollection(Product element) {
    collection.add(element);
    Product.touchNextId();
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
