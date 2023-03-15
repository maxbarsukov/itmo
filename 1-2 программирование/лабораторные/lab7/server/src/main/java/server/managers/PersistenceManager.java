package server.managers;

import common.domain.*;
import common.user.User;
import org.apache.logging.log4j.Logger;

import org.hibernate.SessionFactory;
import server.App;
import server.dao.OrganizationDAO;
import server.dao.ProductDAO;
import server.dao.UserDAO;

import java.util.*;

public class PersistenceManager {
  private final SessionFactory sessionFactory;
  private final Logger logger = App.logger;

  public PersistenceManager(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public int add(User user, Product product) {
    logger.info("Добавление нового продукта " + product.getName());
    OrganizationDAO newOrg = null;
    if (product.getManufacturer() != null) {
      newOrg = addOrganization(user, product.getManufacturer());
    }

    var dao = new ProductDAO(product);
    dao.setManufacturer(newOrg);
    dao.setCreator(new UserDAO(user));

    var session = sessionFactory.getCurrentSession();
    session.beginTransaction();
    session.persist(dao);
    session.getTransaction().commit();
    session.close();

    logger.info("Добавление продукта успешно выполнено.");

    var newId = dao.getId();
    logger.info("Новый id продукта это " + newId);
    return newId;
  }

  public OrganizationDAO addOrganization(User user, Organization organization) {
    logger.info("Добавление новой организации " + organization.getName());

    var dao = new OrganizationDAO(organization);
    dao.setCreator(new UserDAO(user));

    var session = sessionFactory.getCurrentSession();
    session.beginTransaction();
    session.persist(dao);
    session.getTransaction().commit();

    logger.info("Добавление организации успешно выполнено.");

    logger.info("Новый id организации это " + dao.getId());
    return dao;
  }

  public void update(User user, Product product) {
    logger.info("Обновление продукта id#" + product.getId());
    var session = sessionFactory.getCurrentSession();

    session.beginTransaction();
    var productDAO = session.get(ProductDAO.class, product.getId());

    if (product.getManufacturer() != null) {
      updateOrganization(user, product.getManufacturer());
    } else {
      productDAO.setManufacturer(null);
    }
    productDAO.update(product);
    session.update(productDAO);

    session.getTransaction().commit();
    session.close();
    logger.info("Обновление продукта выполнено!");
  }

  public void updateOrganization(User user, Organization organization) {
    logger.info("Обновление организации id#" + organization.getId());

    var session = sessionFactory.getCurrentSession();
    session.beginTransaction();
    var organizationDAO = session.get(OrganizationDAO.class, organization.getId());
    organizationDAO.update(organization);

    session.update(organizationDAO);
    session.getTransaction().commit();
    session.close();
    logger.info("Обновление организации выполнено!");
  }

  public void clear(User user) {
    logger.info("Очищение продуктов пользователя id#" + user.getId() + " из базы данных.");

    var session = sessionFactory.getCurrentSession();
    session.beginTransaction();
    var query = session.createQuery("DELETE FROM products WHERE creator.id = :creator");
    query.setParameter("creator", user.getId());
    var deletedSize = query.executeUpdate();
    session.getTransaction().commit();
    session.close();
    logger.info("Удалено " + deletedSize + " продуктов.");
  }

  public int remove(User user, int id) {
    logger.info("Удаление продукта №" + id + " пользователя id#" + user.getId() + ".");

    var session = sessionFactory.getCurrentSession();
    session.beginTransaction();

    var query = session.createQuery("DELETE FROM products WHERE creator.id = :creator AND id = :id");
    query.setParameter("creator", user.getId());
    query.setParameter("id", id);

    var deletedSize = query.executeUpdate();
    session.getTransaction().commit();
    session.close();
    logger.info("Удалено " + deletedSize + " продуктов.");

    return deletedSize;
  }

  public List<ProductDAO> loadProducts() {
    var session = sessionFactory.getCurrentSession();
    session.beginTransaction();

    var cq = session.getCriteriaBuilder().createQuery(ProductDAO.class);
    var rootEntry = cq.from(ProductDAO.class);
    var all = cq.select(rootEntry);

    var result = session.createQuery(all).getResultList();
    session.getTransaction().commit();
    session.close();
    return result;
  }
}
