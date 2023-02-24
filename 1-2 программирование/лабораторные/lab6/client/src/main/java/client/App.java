package client;

import org.apache.commons.lang3.SerializationUtils;

import common.network.requests.Request;
import common.network.requests.HelpRequest;
import common.network.responses.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

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
    return new String(data, StandardCharsets.UTF_8);
  }

  private static void sendDataSize(byte[] data) throws IOException {
    var size = data.length;
    var d = String.valueOf(size).getBytes(StandardCharsets.UTF_8);
    var buffer = ByteBuffer.wrap(d);
    client.send(buffer, addr);
  }

  private static byte[] sendAndReceiveData(byte[] data) throws IOException {
    System.out.println("HERE 1");
    sendDataSize(data);
    System.out.println("HERE 1");

    var buffer = ByteBuffer.wrap(data);
    client.send(buffer, addr);
    var dataSizeBuffer = ByteBuffer.allocate(1024);
    client.receive(dataSizeBuffer);
    var dataSize = Integer.parseInt(extractMessage(dataSizeBuffer));
    var newBuffer = ByteBuffer.allocate(dataSize);
    client.receive(newBuffer);
    return extractMessage(newBuffer).getBytes(StandardCharsets.UTF_8);
  }

  private static Response sendAndReceiveCommand(Request data) throws IOException {
    var d = SerializationUtils.serialize(data);
    var responseBytes = sendAndReceiveData(d);

    System.out.println("Response: " + new String(responseBytes, StandardCharsets.UTF_8));
    Object response = SerializationUtils.deserialize(responseBytes);
    return (Response) response;
  }

  public static void main(String[] args) throws IOException {
    client.configureBlocking(true);
    var r = sendAndReceiveCommand(new HelpRequest());
    System.out.println("HELLO");
  }
}
