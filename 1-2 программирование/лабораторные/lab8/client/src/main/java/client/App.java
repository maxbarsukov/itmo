package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Главный класс клиентского приложения.
 * @author maxbarsukov
 */
public class App extends Application {
  private static final int PORT = 23586;
  public static final Logger logger = LogManager.getLogger("ClientLogger");

  private Stage mainStage;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    var label = new Label("Hello, JavaFX " + System.getProperty("javafx.version") +
      ", running on Java " + System.getProperty("java.version") + ".");
    var scene = new Scene(new StackPane(label), 640, 480);
    stage.setScene(scene);
    stage.show();
  }
}
