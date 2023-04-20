package server.daos;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import server.models.Product;
import server.models.User;

import java.util.List;

@Stateless
@Transactional
public class ProductDAO {
  @PersistenceContext
  private EntityManager entityManager;

  public void save(Product product) {
    entityManager.persist(product);
    entityManager.flush();
  }

  public void saveWithCreator(Product product, User user) {
    product.setCreator(user);
    entityManager.persist(product);
    entityManager.flush();
  }

  public void clear(User user) {
    entityManager.createQuery("delete from Product product where product.creator = :creator")
      .setParameter("creator", user)
      .executeUpdate();
  }

  public List<Product> findAllByCreator(User user) {
    String query = "select product from Product product where product.creator = :creator";
    return entityManager.createQuery(query, Product.class)
      .setParameter("creator", user)
      .getResultList();
  }
}
