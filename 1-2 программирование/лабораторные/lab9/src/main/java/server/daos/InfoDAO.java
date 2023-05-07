package server.daos;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Stateless
@Transactional
public class InfoDAO {
  @PersistenceContext(unitName = "default")
  private EntityManager entityManager;

  public DatabaseMetaData info() {
    try {
      var connection = entityManager.unwrap(Connection.class);
      return connection.getMetaData();
    } catch (SQLException e) {
      System.err.println(e.getLocalizedMessage());
      throw new NotFoundException("INFO_NOT_FOUND");
    }
  }
}
