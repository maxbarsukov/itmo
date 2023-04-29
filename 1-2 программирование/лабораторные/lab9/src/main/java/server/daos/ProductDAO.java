package server.daos;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import server.exceptions.NotFoundException;
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

  public int clear(int userId) {
    return entityManager.createQuery("delete from Product product where product.creator.id = :creator_id")
      .setParameter("creator_id", userId)
      .executeUpdate();
  }

  public List<Product> getAll() {
    var query = "select product from Product product " +
      "left join fetch product.manufacturer left join fetch product.creator";
    return entityManager.createQuery(query, Product.class).getResultList();
  }

  public Product get(Integer id) {
    var query = "select product from Product product " +
      "left join fetch product.manufacturer left join fetch product.creator " +
      "where product.id = :id";
    try {
      return entityManager.createQuery(query, Product.class)
        .setParameter("id", id)
        .getSingleResult();
    } catch (NoResultException e) {
      throw new NotFoundException();
    }
  }

  public List<Product> findAllByCreatorId(int userId) {
    var query = "select product from Product product " +
      "left join fetch product.manufacturer left join fetch product.creator " +
      "where product.creator.id = :creator_id";
    return entityManager.createQuery(query, Product.class)
      .setParameter("creator_id", userId)
      .getResultList();
  }
}
