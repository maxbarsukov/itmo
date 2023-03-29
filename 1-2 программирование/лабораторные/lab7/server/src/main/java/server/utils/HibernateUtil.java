package server.utils;

import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import server.App;
import server.dao.OrganizationDAO;
import server.dao.ProductDAO;
import server.dao.UserDAO;

public class HibernateUtil {
  private static final Logger logger = App.logger;

  //Property based configuration
  private static SessionFactory sessionFactory;

  private static SessionFactory buildSessionFactory(String url, String user, String password) {
    try {
      //Create Properties, can be read from property files too
      Properties props = new Properties();
      props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
      props.put("hibernate.connection.driver_class", "org.postgresql.Driver");

      props.put("hibernate.connection.username", user);
      props.put("hibernate.connection.password", password);
      props.put("hibernate.connection.url", url);

      props.put("hibernate.connection.pool_size", "100");
      props.put("hibernate.current_session_context_class", "thread");
      props.put("hibernate.connection.autocommit", "true");
      props.put("hibernate.show_sql", "true");
      props.put("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
      props.put("hibernate.hbm2ddl.auto", "validate");

      Configuration configuration = new Configuration();
      configuration.setProperties(props);

      configuration.addPackage("server.dao");
      configuration.addAnnotatedClass(UserDAO.class);
      configuration.addAnnotatedClass(OrganizationDAO.class);
      configuration.addAnnotatedClass(ProductDAO.class);

      ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
      logger.info("Hibernate Java Config serviceRegistry created");

      return configuration.buildSessionFactory(serviceRegistry);
    }
    catch (Throwable ex) {
      logger.error("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static SessionFactory getSessionFactory(String url, String user, String password) {
    if(sessionFactory == null) sessionFactory = buildSessionFactory(url, user, password);
    return sessionFactory;
  }
}
