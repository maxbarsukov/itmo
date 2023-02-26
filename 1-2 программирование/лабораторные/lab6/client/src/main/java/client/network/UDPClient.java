package client.network;

import client.App;
import common.network.requests.Request;
import common.network.responses.Response;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPClient {
  private final DatagramChannel client;
  private final InetSocketAddress addr;

  private final Logger logger = App.logger;

  public UDPClient(InetAddress address, int port) throws IOException {
    this.addr = new InetSocketAddress(address, port);
    this.client = DatagramChannel.open().bind(null).connect(addr);
    logger.info("DatagramChannel подключен к " + addr);
  }

  public Response sendAndReceiveCommand(Request request) throws IOException {
    var data = SerializationUtils.serialize(request);
    var responseBytes = sendAndReceiveData(data);

    Response response = SerializationUtils.deserialize(responseBytes);
    logger.info("Получен ответ от сервера:  " + response);
    return response;
  }

  private String extractMessage(ByteBuffer buffer) {
    buffer.flip();
    var data = new byte[buffer.remaining()];
    buffer.get(data);
    return new String(data);
  }

  private void sendDataSize(byte[] data) throws IOException {
    var size = data.length;
    var buffer = ByteBuffer.wrap(ByteBuffer.allocate(4).putInt(size).array());
    client.send(buffer, addr);
    logger.info("Размер данных отправлен на сервер: " + size);
  }

  private byte[] sendAndReceiveData(byte[] data) throws IOException {
    sendDataSize(data);
    var buffer = ByteBuffer.wrap(data);
    client.send(buffer, addr);
    logger.info("Ожидание размера получаемых данных.");

    var dataSizeBuffer = ByteBuffer.allocate(1024);
    client.receive(dataSizeBuffer);
    var dataSize = Integer.parseInt(extractMessage(dataSizeBuffer));
    logger.info("Размер данных ответа получен с сервера: " + dataSize);

    var newBuffer = ByteBuffer.allocate(dataSize);
    client.receive(newBuffer);
    logger.info("Данные с сервера получены.");

    return newBuffer.array();
  }
}
