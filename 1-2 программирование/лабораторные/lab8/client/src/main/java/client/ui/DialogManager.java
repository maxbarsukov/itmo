package client.ui;

import client.utility.Localizator;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class DialogManager {
  public static void alert(String title, Localizator localizator) {
    DialogManager.createAlert(
      localizator.getKeyString("Error"), localizator.getKeyString(title), Alert.AlertType.ERROR, false
    );
  }

  public static void info(String title, Localizator localizator) {
    DialogManager.createAlert(localizator.getKeyString("Info"), localizator.getKeyString(title), Alert.AlertType.INFORMATION, false);
  }

  public static void createAlert(String title, String content, Alert.AlertType type, boolean scrollable) {
    var alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    if (!scrollable) {
      alert.setContentText(content);
    } else {
      TextArea area = new TextArea(content);
      area.setPrefWidth(600);
      area.setPrefHeight(280);
      alert.getDialogPane().setContent(area);
    }
    alert.setResizable(false);
    alert.showAndWait();
  }

  public static Optional<String> createDialog(String title, String content) {
    TextInputDialog dialog = new TextInputDialog();

    dialog.setTitle(title);
    dialog.setHeaderText(null);
    dialog.setContentText(content);

    dialog.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
      if (!newValue.matches("\\d{0,10}")) {
        dialog.getEditor().setText(oldValue);
      } else if (newValue.length() == 10 && Long.parseLong(newValue) > Integer.MAX_VALUE)
        dialog.getEditor().setText(oldValue);
    });
    return dialog.showAndWait();
  }
}
