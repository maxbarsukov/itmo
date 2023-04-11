package client.controllers;

import client.script.ScriptExecutor;
import client.utility.Localizator;
import client.utility.console.Console;
import common.domain.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;

public class ConsoleController {
  public class OutputConsole extends OutputStream {
    private TextArea console;

    public OutputConsole(TextArea console) {
      this.console = console;
    }

    public void appendText(String valueOf) {
      console.appendText(valueOf);
    }

    public void write(int b) throws IOException {
      appendText(String.valueOf((char) b));
    }
  }

  private Stage stage;
  private Console printer;
  private Localizator localizator;
  private String scriptPath;
  private ScriptExecutor scriptExecutor;

  @FXML
  private TextArea console;

  @FXML
  private Button cancelButton;
  @FXML
  private Button runButton;

  @FXML
  public void initialize() {
    cancelButton.setOnAction(event -> stage.close());
  }

  @FXML
  public void run() throws InterruptedException {
    runButton.setDisable(true);
    printer.print(localizator.getKeyString("ScriptLoaded") + " " + scriptPath);

    printer.print("Hi!");

    runButton.setDisable(false);
  }

  public OutputConsole getConsole() {
    return new OutputConsole(console);
  }

  public void changeLanguage() {
    runButton.setText(localizator.getKeyString("RunButton"));
    cancelButton.setText(localizator.getKeyString("CancelButton"));
  }

  public void show() {
    if (!stage.isShowing()) stage.showAndWait();
  }

  public void setScriptPath(String scriptPath) {
    this.scriptPath = scriptPath;
  }

  public void setScriptExecutor(ScriptExecutor scriptExecutor) {
    this.scriptExecutor = scriptExecutor;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setPrinter(Console printer) {
    this.printer = printer;
  }

  public void setLocalizator(Localizator localizator) {
    this.localizator = localizator;
  }
}
