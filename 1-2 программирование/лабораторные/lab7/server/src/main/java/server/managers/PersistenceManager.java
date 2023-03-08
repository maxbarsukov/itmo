package server.managers;

import common.domain.*;
import common.user.User;
import org.apache.logging.log4j.Logger;
import server.App;

import java.sql.SQLException;
import java.util.*;

public class PersistenceManager {
  private final DatabaseManager databaseManager;
  private final Logger logger = App.logger;

  public PersistenceManager(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  public void clear(User user) throws SQLException {
    logger.info("Удаление продуктов пользователя id#" + user.getId() + " из базы данных.");
    var connection = databaseManager.getConnection();
    var statement = connection.prepareStatement("DELETE FROM products WHERE creator_id = ?");
    statement.setInt(1, user.getId());

    var deletedSize = statement.executeUpdate();
    logger.info("Удалено " + deletedSize + " продуктов.");
    connection.close();
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
