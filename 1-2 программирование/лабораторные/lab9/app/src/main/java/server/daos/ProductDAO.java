package server.daos;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import server.exceptions.BadOwnerException;
import server.exceptions.NotFoundException;
import server.models.Product;

import java.util.List;
import java.util.Optional;

@Stateless
@Transactional
public class ProductDAO {
  @PersistenceContext
  private EntityManager entityManager;

  public Product save(Product product) {
    entityManager.persist(product);
    entityManager.flush();
    return product;
  }

  public Product update(Product oldProduct, Product newProduct, Integer currentUserId) {
    if (oldProduct.getCreator().getId() != currentUserId) {
      throw new BadOwnerException();
    }

    newProduct.setId(oldProduct.getId());
    entityManager.merge(newProduct);
    entityManager.flush();
    return newProduct;
  }

  public List<Integer> clear(int userId) {
    List<Integer> ids = entityManager.createQuery("select product.id from Product product where product.creator.id = :creator_id", Integer.class)
      .setParameter("creator_id", userId)
      .getResultList();

    entityManager.createQuery("delete from Product product where product.creator.id = :creator_id")
      .setParameter("creator_id", userId)
      .executeUpdate();

    return ids;
  }

  public int delete(int productId, int userId) {
    var product = get(productId);
    if (product.getCreator().getId() != userId) {
      throw new BadOwnerException();
    }

    return entityManager.createQuery("delete from Product product " +
        "where product.id = :product_id and product.creator.id = :creator_id")
      .setParameter("creator_id", userId)
      .setParameter("product_id", productId)
      .executeUpdate();
  }

  public List<Product> getAll() {
    var query = "select product from Product product " +
      "left join fetch product.manufacturer left join fetch product.creator";
    return entityManager.createQuery(query, Product.class).getResultList();
  }

  public Optional<Product> findById(Integer id) {
    Product product = entityManager.find(Product.class, id);
    return Optional.ofNullable(product);
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

  public long count() {
    final TypedQuery<Number> query = entityManager.createQuery("SELECT count(p) FROM Product p", Number.class);
    return query.getSingleResult().longValue();
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
