package server.managers;

import common.domain.*;
import common.user.User;
import org.apache.logging.log4j.Logger;
import server.App;

import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

public class PersistenceManager {
  private final DatabaseManager databaseManager;
  private final Logger logger = App.logger;

  public PersistenceManager(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  public int add(User user, Product product) throws SQLException {
    logger.info("Добавление нового продукта " + product.getName());
    Integer newOrgId = null;
    if (product.getManufacturer() != null) {
      newOrgId = addOrganization(user, product.getManufacturer());
    }

    var connection = databaseManager.getConnection();
    var statement = connection.prepareStatement(
      "INSERT INTO products(name, x, y, price, part_number, unit_of_measure, manufacturer_id, creator_id)" +
        "VALUES (?, ?, ?, ?, ?, ?::unit_of_measure, ?," +
        "(SELECT id FROM users WHERE users.name=?)) RETURNING id"
    );

    statement.setString(1, product.getName());
    statement.setInt(2, product.getCoordinates().getX());
    statement.setLong(3, product.getCoordinates().getY());
    statement.setLong(4, product.getPrice());
    statement.setString(5, product.getPartNumber());
    statement.setString(6, product.getUnitOfMeasure().toString());

    if (newOrgId == null) {
      statement.setNull(7, Types.INTEGER);
    } else {
      statement.setInt(7, newOrgId);
    }
    statement.setString(8, user.getName());

    var result = statement.executeQuery();
    connection.close();

    logger.info("Добавление продукта успешно выполнено.");
    result.next();

    var newId = result.getInt(1);
    logger.info("Новый id продукта это " + newId);
    return newId;
  }

  public int addOrganization(User user, Organization organization) throws SQLException {
    logger.info("Добавление новой организации " + organization.getName());

    var connection = databaseManager.getConnection();
    var statement = connection.prepareStatement(
      "INSERT INTO organizations(name, employees_count, type, street, zip_code, creator_id) " +
        "VALUES (?, ?, ?::organization_type, ?, ?," +
        "(SELECT id FROM users WHERE users.name=?)) RETURNING id"
    );

    statement.setString(1, organization.getName());
    statement.setLong(2, organization.getEmployeesCount());
    statement.setString(3, organization.getType().toString());
    statement.setString(4, organization.getPostalAddress().getStreet());
    statement.setString(5, organization.getPostalAddress().getZipCode());
    statement.setString(6, user.getName());

    var result = statement.executeQuery();
    connection.close();

    logger.info("Добавление организации успешно выполнено.");
    result.next();

    var newId = result.getInt(1);
    logger.info("Новый id организации это " + newId);
    return newId;
  }

  public void update(User user, Product product) throws SQLException {
    logger.info("Обновление продукта id#" + product.getId());

    if (product.getManufacturer() != null) {
      updateOrganization(user, product.getManufacturer());
    }

    var connection = databaseManager.getConnection();
    var statement = connection.prepareStatement(
      "UPDATE products SET name = ?, x = ?, y = ?, price = ?," +
        "part_number = ?, unit_of_measure = ?::unit_of_measure, manufacturer_id = ? " +
        "WHERE id = ? AND creator_id = ?"
    );
    statement.setString(1, product.getName());
    statement.setInt(2, product.getCoordinates().getX());
    statement.setLong(3, product.getCoordinates().getY());
    statement.setLong(4, product.getPrice());
    statement.setString(5, product.getPartNumber());
    statement.setString(6, product.getUnitOfMeasure().toString());

    if (product.getManufacturer() == null) {
      statement.setNull(7, Types.INTEGER);
    } else {
      statement.setInt(7, product.getManufacturer().getId());
    }
    statement.setInt(8, product.getId());
    statement.setInt(9, user.getId());

    statement.execute();
    connection.close();
    logger.info("Обновление продукта выполнено!");
  }

  public void updateOrganization(User user, Organization organization) throws SQLException {
    logger.info("Обновление организации id#" + organization.getId());

    var connection = databaseManager.getConnection();
    var statement = connection.prepareStatement(
      "UPDATE organizations SET name = ?, employees_count = ?," +
        "type = ?::organization_type, street = ?, zip_code = ? " +
        "WHERE id = ? AND creator_id = ?"
    );

    statement.setString(1, organization.getName());
    statement.setLong(2, organization.getEmployeesCount());
    statement.setString(3, organization.getType().toString());
    statement.setString(4, organization.getPostalAddress().getStreet());
    statement.setString(5, organization.getPostalAddress().getZipCode());

    statement.setInt(6, organization.getId());
    statement.setInt(7, user.getId());

    statement.execute();
    connection.close();
    logger.info("Обновление организации выполнено!");
  }

  public void clear(User user) throws SQLException {
    logger.info("Очищение продуктов пользователя id#" + user.getId() + " из базы данных.");
    var connection = databaseManager.getConnection();

    var statement = connection.prepareStatement(
      "DELETE FROM products WHERE creator_id = ?"
    );
    statement.setInt(1, user.getId());

    var deletedSize = statement.executeUpdate();
    logger.info("Удалено " + deletedSize + " продуктов.");
    connection.close();
  }

  public int remove(User user, int id) throws SQLException {
    logger.info("Удаление продукта №" + id + " пользователя id#" + user.getId() + ".");
    var connection = databaseManager.getConnection();

    var statement = connection.prepareStatement(
      "DELETE FROM products WHERE creator_id = ? AND id = ?"
    );
    statement.setInt(1, user.getId());
    statement.setInt(2, id);

    var deletedSize = statement.executeUpdate();
    logger.info("Удалено " + deletedSize + " продуктов.");
    connection.close();

    return deletedSize;
  }

  public Map<Integer, Organization> loadOrganizations() throws SQLException {
    var connection = databaseManager.getConnection();
    var statement = connection.prepareStatement("SELECT * FROM organizations");
    var result = statement.executeQuery();

    var organizations = new HashMap<Integer, Organization>();
    while(result.next()) {
      var id = result.getInt("id");
      var organization = new Organization(
        id,
        result.getString("name"),
        result.getLong("employees_count"),
        OrganizationType.valueOf(result.getString("type")),
        new Address(
          result.getString("street"),
          result.getString("zip_code")
        )
      );
      organization.setCreatorId(result.getInt("creator_id"));
      organizations.put(id, organization);
    }

    connection.close();
    return organizations;
  }

  public List<Product> loadProducts() throws SQLException {
    var organizations = loadOrganizations();

    var connection = databaseManager.getConnection();
    var statement = connection.prepareStatement("SELECT * FROM products");
    var result = statement.executeQuery();

    List<Product> products = new LinkedList<>();
    while(result.next()) {
      var manufacturerId = result.getInt("manufacturer_id");
      Organization organization = null;
      if (!result.wasNull()) {
        organization = organizations.get(manufacturerId);
      }

      var product = new Product(
        result.getInt("id"),
        result.getString("name"),
        new Coordinates(
          result.getInt("x"),
          result.getLong("y")
        ),
        result.getDate("creation_date").toLocalDate(),
        result.getLong("price"),
        result.getString("part_number"),
        UnitOfMeasure.valueOf(result.getString("unit_of_measure")),
        organization
      );
      product.setCreatorId(result.getInt("creator_id"));
      products.add(product);
    }

    connection.close();
    return products;
  }
}
