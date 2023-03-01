package server.network;

import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import server.App;
import server.handlers.CommandHandler;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.IntStream;

public class UDPDatagramServer extends UDPServer {
  private final int PACKET_SIZE = 1024;
  private final DatagramSocket datagramSocket;

  private final Logger logger = App.logger;

  public UDPDatagramServer(InetAddress address, int port, CommandHandler commandHandler) throws SocketException {
    super(new InetSocketAddress(address, port), commandHandler);
    this.datagramSocket = new DatagramSocket(getAddr());
    this.datagramSocket.setReuseAddress(true);
  }

  @Override
  public Pair<Byte[], SocketAddress> receiveData() throws IOException {
    var received = false;
    var result = new byte[0];
    SocketAddress addr = null;

    while(!received) {
      var data = new byte[PACKET_SIZE];

      var dp = new DatagramPacket(data, data.length);
      datagramSocket.receive(dp);

      addr = dp.getSocketAddress();
      logger.info("Получено \"" + new String(data) + "\" от " + dp.getAddress());

      if (IntStream.range(0, data.length).parallel().allMatch(i -> data[i] == 0)) {
        received = true;
        logger.info("Получение данных от " + dp.getAddress() + " окончено");
      } else {
        result = Bytes.concat(result, data);
      }
    }
    return new ImmutablePair<>(ArrayUtils.toObject(result), addr);
  }

  @Override
  public void sendData(byte[] data, SocketAddress addr) throws IOException {
    byte[][] ret = new byte[(int)Math.ceil(data.length / (double)PACKET_SIZE)][PACKET_SIZE];

    int start = 0;
    for(int i = 0; i < ret.length; i++) {
      ret[i] = Arrays.copyOfRange(data, start, start + PACKET_SIZE);
      start += PACKET_SIZE;
    }

    for(var chunk : ret) {
      var dp = new DatagramPacket(chunk, PACKET_SIZE, addr);
      datagramSocket.send(dp);
      logger.info("Чанк размером " + chunk.length + " отправлен");
    }

    var dp = new DatagramPacket(ByteBuffer.allocate(PACKET_SIZE).array(), PACKET_SIZE, addr);
    datagramSocket.send(dp);
    logger.info("Последний пустой чанк отправлен");
  }

  @Override
  public void connectToClient(SocketAddress addr) throws SocketException {
    datagramSocket.connect(addr);
  }

  @Override
  public void disconnectFromClient() {
    datagramSocket.disconnect();
  }

  @Override
  public void close() {
    datagramSocket.close();
  }
}
