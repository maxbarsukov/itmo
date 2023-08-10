package broadcaster.consumers;

import common.events.Event;
import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import org.apache.commons.lang3.SerializationUtils;

@RabbitListener
public class ProductListener {
  @Queue("products")
  public void receive(byte[] data) {
    Event event = SerializationUtils.deserialize(data);

    System.out.println("Java received " + data.length + " bytes from RabbitMQ");
    System.out.println("Event type: " + event.getMessageType());
  }
}
