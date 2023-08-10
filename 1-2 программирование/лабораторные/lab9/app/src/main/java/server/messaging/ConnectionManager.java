package server.messaging;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import jakarta.ejb.Stateless;

@Stateless
public class ConnectionManager {
  public Connection getConnection() {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUri("amqp://lab9rabbitmq:lab9rabbitmq@localhost:5672/");
      factory.setVirtualHost("/");
      return factory.newConnection();
    } catch (Exception e) {
      System.err.println(e.toString());
      e.printStackTrace();
    }
    return null;
  }
}
