package server.network;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import server.App;
import server.handlers.CommandHandler;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPDatagramServer extends UDPServer {
  private final DatagramSocket datagramSocket;

  private final Logger logger = App.logger;

  public UDPDatagramServer(int port, CommandHandler commandHandler) throws SocketException {
    super(new InetSocketAddress(port), commandHandler);
    this.datagramSocket = new DatagramSocket(getAddr());
    this.datagramSocket.setReuseAddress(true);
  }

  @Override
  public Pair<Byte[], SocketAddress> receiveData(int bufferSize) throws IOException {
    var data = new byte[bufferSize];

    var dp = new DatagramPacket(data, data.length);
    datagramSocket.receive(dp);

    logger.info("Получено \"" + new String(data) + "\" от " + dp.getAddress());
    return new ImmutablePair<>(ArrayUtils.toObject(data), dp.getSocketAddress());
  }

  @Override
  public Pair<Integer, SocketAddress> receiveDataSize() throws IOException {
    var data = new byte[256];
    var dp = new DatagramPacket(data, data.length);
    datagramSocket.receive(dp);

    var dataStr = new String(dp.getData(), StandardCharsets.UTF_8);
    var dataInt = Integer.parseInt(dataStr);
    return new ImmutablePair<>(dataInt, dp.getSocketAddress());
  }

  @Override
  public void sendData(byte[] data, SocketAddress addr) throws IOException {
    var dp = new DatagramPacket(data, data.length, addr);
    datagramSocket.send(dp);
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
