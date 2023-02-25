package client;

import org.apache.commons.lang3.SerializationUtils;

import common.network.requests.*;
import common.network.responses.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author maxbarsukov
 */
public class App {
  private static DatagramChannel client;
  private static InetSocketAddress addr;

  static {
    try {
      addr = new InetSocketAddress(23586);
      client = DatagramChannel.open().bind(null).connect(addr);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String extractMessage(ByteBuffer buffer) {
    buffer.flip();
    var data = new byte[buffer.remaining()];
    buffer.get(data);
    return new String(data);
  }

  private static void sendDataSize(byte[] data) throws IOException {
    var size = data.length;
    var buffer = ByteBuffer.wrap(ByteBuffer.allocate(4).putInt(size).array());
    client.send(buffer, addr);
  }

  private static byte[] sendAndReceiveData(byte[] data) throws IOException {
    sendDataSize(data);

    var buffer = ByteBuffer.wrap(data);
    client.send(buffer, addr);
    var dataSizeBuffer = ByteBuffer.allocate(1024);
    client.receive(dataSizeBuffer);
    var dataSize = Integer.parseInt(extractMessage(dataSizeBuffer));
    var newBuffer = ByteBuffer.allocate(dataSize);
    client.receive(newBuffer);
    return newBuffer.array();
  }

  private static Response sendAndReceiveCommand(Request data) throws IOException {
    var d = SerializationUtils.serialize(data);
    var responseBytes = sendAndReceiveData(d);

    // System.out.println("Response: " + new String(responseBytes, StandardCharsets.UTF_8));
    Response response = SerializationUtils.deserialize(responseBytes);
    return response;
  }

  public static void main(String[] args) throws IOException {
    client.configureBlocking(true);
    Response r = sendAndReceiveCommand(new HelpRequest());
    System.out.println(((HelpResponse) r).helpMessage);
  }
}
