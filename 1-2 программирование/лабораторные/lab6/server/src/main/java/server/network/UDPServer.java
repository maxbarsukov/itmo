package server.network;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Logger;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.SerializationUtils;
import server.App;

import common.network.*;
import common.network.responses.NoSuchCommandResponse;
import server.handlers.CommandHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * UDP обработчик запросов
 * @author maxbarsukov
 */
abstract class UDPServer {
  private InetSocketAddress addr;
  private CommandHandler commandHandler;
  private final Logger logger = App.logger;

  private boolean running = true;

  /**
   * Получает данные с клиента.
   * Возвращает пару из данных и адреса клиента
   */
  public abstract Pair<Byte[], SocketAddress> receiveData(int bufferSize) throws IOException;

  /**
   * Получает размер данных от клиента.
   * Возвращает пару из размера данных и адреса клиента
   */
  public abstract Pair<Integer, SocketAddress> receiveDataSize() throws IOException;

  /**
   * Отправляет данные клиенту
   */
  public abstract void sendData(byte[] data, SocketAddress addr) throws IOException;

  public abstract void connectToClient(SocketAddress addr) throws SocketException;

  public abstract void disconnectFromClient();
  public abstract void close();

  public void run() {
    logger.info("Сервер запущен по адресу " + addr);

    while (running) {
      Pair<Integer, SocketAddress> pair;
      try {
        pair = receiveDataSize();
      } catch (Exception e) {
        logger.error("Ошибка получения размера данных : " + e.toString(), e);
        disconnectFromClient();
        continue;
      }

      var dataSize = pair.getKey();
      var clientAddr = pair.getValue();

      logger.info("Размер пакета " + dataSize + ". Клиент " + clientAddr);

      Pair<Byte[], SocketAddress> dataPair;
      try {
        dataPair = receiveData(dataSize);
      } catch (Exception e) {
        logger.error("Ошибка получения данных : " + e.toString(), e);
        disconnectFromClient();
        continue;
      }

      var dataFromClient = dataPair.getKey();

      try {
        connectToClient(clientAddr);
      } catch (Exception e) {
        logger.error("Ошибка соединения с клиентом : " + e.toString(), e);
      }

      logger.info("Соединено с" + " ${clientAddr}");

      Request request = SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
      logger.info("Обработка " + request + " из " + clientAddr);

      Response response = null;
      try {
        response = commandHandler.handle(request);
      } catch (Exception e) {
        logger.error("Ошибка выполнения команды : " + e.toString(), e);
      }
      if (response == null) response = new NoSuchCommandResponse(request.getName());

      var data = SerializationUtils.serialize(response);
      logger.info("Ответ: " + new String(data));

      try {
        sendData(Integer.toString(data.length).getBytes(StandardCharsets.UTF_8), clientAddr);
        logger.info("Размер ответа отправлен на клиент. Размер сообщения: " + data.length);
        sendData(data, clientAddr);
        logger.info("Отправлен ответ клиенту " + clientAddr);
      } catch (Exception e) {
        logger.error("Ошибка ввода-вывода : " + e.toString(), e);
      }
      disconnectFromClient();
      logger.info("Отключение от клиента " + clientAddr);
    }

    close();
  }

  public void stop() {
    running = false;
  }

  private String extractMessage(ByteBuffer buffer) {
    buffer.flip();
    var data = new byte[buffer.remaining()];
    buffer.get(data);

    return new String(data, StandardCharsets.UTF_8);
  }
}
