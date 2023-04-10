package client;

import client.controllers.*;
import client.network.UDPClient;
import client.utility.Localizator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Главный класс клиентского приложения.
 * @author maxbarsukov
 */
public class App extends Application {
  private static final int PORT = 23586;
  public static final Logger logger = LogManager.getLogger("ClientLogger");
  public static UDPClient client;

  private Stage mainStage;
  private Localizator localizator;

  public static void main(String[] args) {
    try {
      client = new UDPClient(InetAddress.getLocalHost(), PORT);
      launch(args);
    } catch (IOException e) {
      logger.info("Невозможно подключиться к серверу.", e);
      System.err.println("Невозможно подключиться к серверу!");
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    localizator = new Localizator(ResourceBundle.getBundle("locales/gui", new Locale("ru", "RU")));
    mainStage = stage;

    var authLoader = new FXMLLoader(getClass().getResource("/auth.fxml"));
    Parent authRoot = authLoader.load();
    AuthController authController = authLoader.getController();
    authController.setCallback(this::startMain);
    authController.setClient(client);
    authController.setLocalizator(localizator);

    mainStage.setScene(new Scene(authRoot));
    mainStage.setTitle("Products");
    mainStage.setResizable(false);
    mainStage.show();
  }

  public void startMain() {
    var mainLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
    var mainRoot = loadFxml(mainLoader);

    var editLoader = new FXMLLoader(getClass().getResource("/edit.fxml"));
    var editRoot = loadFxml(editLoader);

    var editScene = new Scene(editRoot);
    var editStage = new Stage();
    editStage.setScene(editScene);
    editStage.setResizable(false);
    editStage.setTitle("Products");
    EditController editController = editLoader.getController();

    editController.setStage(editStage);
    editController.setLocalizator(localizator);

    MainController mainController = mainLoader.getController();
    mainController.setEditController(editController);
    mainController.setContext(client, localizator, mainStage);

    mainStage.setScene(new Scene(mainRoot));
    mainController.refresh();
    mainStage.show();
  }

  private Parent loadFxml(FXMLLoader loader) {
    Parent parent = null;
    try {
      parent = loader.load();
    } catch (IOException e) {
      logger.error("Can't load " + loader.toString(), e);
      System.exit(1);
    }
    return parent;
  }
}
