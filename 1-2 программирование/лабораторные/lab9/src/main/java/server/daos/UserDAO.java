package server.daos;

import jakarta.ejb.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import server.models.User;
import java.util.Optional;

@Stateless
@Transactional
public class UserDAO {
  @PersistenceContext(unitName = "default")
  private EntityManager entityManager;

  public void save(User user) {
    entityManager.persist(user);
    entityManager.flush();
  }

  public Optional<User> findByName(String name) {
    return entityManager.createQuery("SELECT t FROM User t where t.name = :name")
      .setParameter("name", name)
      .getResultList()
      .stream()
      .findFirst();
  }

  public Optional<User> findById(int id) {
    User user = entityManager.find(User.class, id);
    return Optional.ofNullable(user);
  }

  public boolean checkIfUserExists(int id) {
    return findById(id).isPresent();
  }
}
