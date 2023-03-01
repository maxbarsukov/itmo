package client.network;

import client.App;
import common.network.requests.Request;
import common.network.responses.Response;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.stream.IntStream;

public class UDPClient {
  private final int PACKET_SIZE = 1024;

  private final DatagramChannel client;
  private final InetSocketAddress addr;

  private final Logger logger = App.logger;

  public UDPClient(InetAddress address, int port) throws IOException {
    this.addr = new InetSocketAddress(address, port);
    this.client = DatagramChannel.open().bind(null).connect(addr);
    this.client.configureBlocking(false);
    logger.info("DatagramChannel подключен к " + addr);
  }

  public Response sendAndReceiveCommand(Request request) throws IOException {
    var data = SerializationUtils.serialize(request);
    var responseBytes = sendAndReceiveData(data);

    Response response = SerializationUtils.deserialize(responseBytes);
    logger.info("Получен ответ от сервера:  " + response);
    return response;
  }

  private void sendData(byte[] data) throws IOException {
    byte[][] ret = new byte[(int)Math.ceil(data.length / (double)PACKET_SIZE)][PACKET_SIZE];

    int start = 0;
    for(int i = 0; i < ret.length; i++) {
      ret[i] = Arrays.copyOfRange(data, start, start + PACKET_SIZE);
      start += PACKET_SIZE;
    }

    logger.info("Отправляется " + ret.length + " чанков + 1 пустой...");

    for(var chunk : ret) {
      client.send(ByteBuffer.wrap(chunk), addr);
      logger.info("Чанк размером " + chunk.length + " отправлен на сервер.");
    }

    client.send(ByteBuffer.allocate(PACKET_SIZE), addr);
    logger.info("Отправка данных завершена.");
  }

  private byte[] receiveData() throws IOException {
    var received = false;
    var result = new byte[0];

    while(!received) {
      var data = receiveData(PACKET_SIZE);
      logger.info("Получено \"" + new String(data) + "\"");
      if (IntStream.range(0, data.length).parallel().allMatch(i -> data[i] == 0)) {
        received = true;
        logger.info("Получение данных окончено");
      } else {
        result = Bytes.concat(result, data);
      }
    }

    return result;
  }

  private byte[] receiveData(int bufferSize) throws IOException {
    var buffer = ByteBuffer.allocate(bufferSize);
    SocketAddress address = null;
    while(address == null) {
      address = client.receive(buffer);
    }
    return buffer.array();
  }

  private byte[] sendAndReceiveData(byte[] data) throws IOException {
    sendData(data);
    return receiveData();
  }
}
