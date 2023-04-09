package client;

import client.auth.SessionHandler;
import client.controllers.AuthController;
import client.network.UDPClient;
import client.utility.Localizator;
import common.domain.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
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
    System.out.println(SessionHandler.currentUser);
//    FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
//    Parent mainRoot = mainLoader.load();
//    MainController mainController = mainLoader.getController();
//    mainController.setLocalizator(localizator);
//    mainController.setStage(mainStage);
//    mainController.setLogin(login);
//    mainController.setCollection(collection);
//    mainController.setCommandManager(commandManager);
//    FXMLLoader editLoader = new FXMLLoader(getClass().getResource("/edit.fxml"));
//    Parent editRoot = editLoader.load();
//    Scene editScene = new Scene(editRoot);
//    Stage editStage = new Stage();
//    editStage.setScene(editScene);
//    editStage.setResizable(false);
//    editStage.setTitle("Products");
//    EditController editController = editLoader.getController();
//    editController.setLocalizator(localizator);
//    editController.setStage(editStage);
//    mainController.setEditController(editController);
//    mainController.setPrevLang(language);
//    mainStage.setScene(new Scene(mainRoot));
//    mainController.refresh();
//    mainStage.show();
  }
}
