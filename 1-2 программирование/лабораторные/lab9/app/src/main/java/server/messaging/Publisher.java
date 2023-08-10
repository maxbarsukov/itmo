package server.messaging;

import common.events.Event;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Stateless
public class Publisher {
  @EJB
  private ConnectionManager connectionManager;

  public void send(Event event) {
    try(Channel channel = connectionManager.getConnection().createChannel()) {
      channel.queueDeclare("products", true, false, false, null);

      var objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
      var data = objectMapper.writer().writeValueAsString(event);

      channel.basicPublish("products_exchange", "products",
        new AMQP.BasicProperties.Builder()
          .contentType("application/octet-stream")
          .userId("lab9rabbitmq")
          .appId("lab9-server")
          .build(),
        data.getBytes(StandardCharsets.UTF_8));
    } catch (IOException | TimeoutException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
