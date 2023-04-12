package client.controllers;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.script.ScriptExecutor;
import client.ui.DialogManager;
import client.utility.Localizator;
import client.utility.ProductPresenter;
import common.domain.Product;
import common.exceptions.*;
import common.network.requests.*;
import common.network.responses.*;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.util.*;

public class MainController {
  private Localizator localizator;
  private UDPClient client;

  private Runnable authCallback;
  private volatile boolean isRefreshing = false;

  private List<Product> collection;

  private final HashMap<String, Locale> localeMap = new HashMap<>() {{
    put("Русский", new Locale("ru", "RU"));
    put("English(IN)", new Locale("en", "IN"));
    put("Íslenska", new Locale("is", "IS"));
    put("Svenska", new Locale("sv", "SE"));
  }};

  private HashMap<String, Color> colorMap;
  private HashMap<Integer, Label> infoMap;
  private Random random;

  private EditController editController;
  private Stage stage;

  @FXML
  private ComboBox<String> languageComboBox;
  @FXML
  private Label userLabel;

  @FXML
  private Button helpButton;
  @FXML
  private Button infoButton;
  @FXML
  private Button addButton;
  @FXML
  private Button updateButton;
  @FXML
  private Button removeByIdButton;
  @FXML
  private Button clearButton;
  @FXML
  private Button executeScriptButton;
  @FXML
  private Button headButton;
  @FXML
  private Button addIfMaxButton;
  @FXML
  private Button addIfMinButton;
  @FXML
  private Button sumOfPriceButton;
  @FXML
  private Button filterByPriceButton;
  @FXML
  private Button filterContainsPartNumberButton;
  @FXML
  private Button exitButton;
  @FXML
  private Button logoutButton;

  @FXML
  private Tab tableTab;
  @FXML
  private TableView<Product> tableTable;

  @FXML
  private TableColumn<Product, String> ownerColumn;
  @FXML
  private TableColumn<Product, Integer> idColumn;
  @FXML
  private TableColumn<Product, String> nameColumn;
  @FXML
  private TableColumn<Product, Integer> xColumn;
  @FXML
  private TableColumn<Product, Long> yColumn;
  @FXML
  private TableColumn<Product, String> dateColumn;
  @FXML
  private TableColumn<Product, Long> priceColumn;
  @FXML
  private TableColumn<Product, String> partNumberColumn;
  @FXML
  private TableColumn<Product, String> unitOfMeasureColumn;

  @FXML
  private TableColumn<Product, Integer> manufacturerIdColumn;
  @FXML
  private TableColumn<Product, String> manufacturerNameColumn;
  @FXML
  private TableColumn<Product, Long> manufacturerEmployeesCountColumn;
  @FXML
  private TableColumn<Product, String> manufacturerTypeColumn;
  @FXML
  private TableColumn<Product, String> manufacturerStreetColumn;
  @FXML
  private TableColumn<Product, String> manufacturerZipCodeColumn;

  @FXML
  private Tab visualTab;
  @FXML
  private AnchorPane visualPane;

  @FXML
  public void initialize() {
    colorMap = new HashMap<>();
    infoMap = new HashMap<>();
    random = new Random();

    languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    languageComboBox.setStyle("-fx-font: 13px \"Sergoe UI\";");
    languageComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
      localizator.setBundle(ResourceBundle.getBundle("locales/gui", localeMap.get(newValue)));
      changeLanguage();
    });

    ownerColumn.setCellValueFactory(product -> new SimpleStringProperty(product.getValue().getCreator().toString()));
    idColumn.setCellValueFactory(product -> new SimpleIntegerProperty(product.getValue().getId()).asObject());
    nameColumn.setCellValueFactory(product -> new SimpleStringProperty(product.getValue().getName()));
    xColumn.setCellValueFactory(product -> new SimpleIntegerProperty(product.getValue().getCoordinates().getX()).asObject());
    yColumn.setCellValueFactory(product -> new SimpleLongProperty(product.getValue().getCoordinates().getY()).asObject());
    dateColumn.setCellValueFactory(product -> new SimpleStringProperty(localizator.getDate(product.getValue().getCreationDate())));
    priceColumn.setCellValueFactory(product -> new SimpleLongProperty(product.getValue().getPrice()).asObject());
    partNumberColumn.setCellValueFactory(product -> new SimpleStringProperty(product.getValue().getPartNumber()));
    unitOfMeasureColumn.setCellValueFactory(
      product -> new SimpleStringProperty(
        product.getValue().getUnitOfMeasure() != null ? product.getValue().getUnitOfMeasure().toString() : null
      )
    );

    manufacturerIdColumn.setCellValueFactory(product -> {
      if (product.getValue().getManufacturer() != null) {
        return new SimpleIntegerProperty(product.getValue().getManufacturer().getId()).asObject();
      }
      return null;
    });

    manufacturerNameColumn.setCellValueFactory(product -> {
      if (product.getValue().getManufacturer() != null) {
        return new SimpleStringProperty(product.getValue().getManufacturer().getName());
      }
      return null;
    });

    manufacturerEmployeesCountColumn.setCellValueFactory(product -> {
      if (product.getValue().getManufacturer() != null) {
        return new SimpleLongProperty(product.getValue().getManufacturer().getEmployeesCount()).asObject();
      }
      return null;
    });

    manufacturerTypeColumn.setCellValueFactory(product -> {
      if (product.getValue().getManufacturer() != null) {
        return new SimpleStringProperty(product.getValue().getManufacturer().getType().toString());
      }
      return null;
    });

    manufacturerStreetColumn.setCellValueFactory(product -> {
      if (product.getValue().getManufacturer() != null) {
        return new SimpleStringProperty(product.getValue().getManufacturer().getPostalAddress().getStreet());
      }
      return null;
    });

    manufacturerZipCodeColumn.setCellValueFactory(product -> {
      if (product.getValue().getManufacturer() != null) {
        return new SimpleStringProperty(product.getValue().getManufacturer().getPostalAddress().getZipCode());
      }
      return null;
    });

    tableTable.setRowFactory(tableView -> {
      var row = new TableRow<Product>();
      row.setOnMouseClicked(mouseEvent -> {
        if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
          doubleClickUpdate(row.getItem());
        }
      });
      return row;
    });

    visualTab.setOnSelectionChanged(event -> visualise(false));
  }

  @FXML
  public void exit() {
    System.exit(0);
  }

  @FXML
  public void logout() {
    SessionHandler.setCurrentUser(null);
    SessionHandler.setCurrentLanguage("Русский");
    setRefreshing(false);
    authCallback.run();
  }

  @FXML
  public void help() {
    try {
      var response = (HelpResponse) client.sendAndReceiveCommand(new HelpRequest(SessionHandler.getCurrentUser()));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      DialogManager.createAlert(localizator.getKeyString("Help"), localizator.getKeyString("HelpResult"), Alert.AlertType.INFORMATION, true);
    } catch (APIException | ErrorResponseException | IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void info() {
    try {
      var response = (InfoResponse) client.sendAndReceiveCommand(new InfoRequest(SessionHandler.getCurrentUser()));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      var formatted = MessageFormat.format(
        localizator.getKeyString("InfoResult"),
        response.type,
        response.size,
        localizator.getDate(response.lastSaveTime),
        localizator.getDate(response.lastInitTime)
      );
      DialogManager.createAlert(localizator.getKeyString("Info"), formatted, Alert.AlertType.INFORMATION, true);
    } catch (APIException | ErrorResponseException | IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void add() {
    editController.clear();
    editController.show();
    var product = editController.getProduct();
    if (product != null) {
      product = product.copy(product.getId(), SessionHandler.getCurrentUser());

      try {
        var response = (AddResponse) client.sendAndReceiveCommand(new AddRequest(product, SessionHandler.getCurrentUser()));
        if (response.getError() != null && !response.getError().isEmpty()) {
          throw new APIException(response.getError());
        }

        loadCollection();
        DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddResult"), Alert.AlertType.INFORMATION, false);
      } catch (APIException | ErrorResponseException e) {
        DialogManager.alert("AddErr", localizator);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      }
    }
  }

  @FXML
  public void update() {
    Optional<String> input = DialogManager.createDialog(localizator.getKeyString("Update"), "ID:");
    if (input.isPresent() && !input.get().equals("")) {
      try {
        var id = Integer.parseInt(input.orElse(""));
        var product = collection.stream()
          .filter(p -> p.getId() == id)
          .findAny()
          .orElse(null);
        if (product == null) throw new NotFoundException();
        if (product.getCreatorId() != SessionHandler.getCurrentUser().getId()) throw new BadOwnerException();
        doubleClickUpdate(product, false);
      } catch (NumberFormatException e) {
        DialogManager.alert("NumberFormatException", localizator);
      } catch (BadOwnerException e) {
        DialogManager.alert("BadOwnerError", localizator);
      } catch (NotFoundException e) {
        DialogManager.alert("NotFoundException", localizator);
      }
    }
  }

  @FXML
  public void removeById() {
    Optional<String> input = DialogManager.createDialog(localizator.getKeyString("RemoveByID"), "ID: ");
    if (input.isPresent() && !input.get().equals("")) {
      try {
        var id = Integer.parseInt(input.orElse(""));
        var product = collection.stream()
          .filter(p -> p.getId() == id)
          .findAny()
          .orElse(null);
        if (product == null) throw new NotFoundException();
        if (product.getCreatorId() != SessionHandler.getCurrentUser().getId()) throw new BadOwnerException();

        var response = (RemoveByIdResponse) client.sendAndReceiveCommand(new RemoveByIdRequest(id, SessionHandler.getCurrentUser()));
        if (response.getError() != null && !response.getError().isEmpty()) {
          throw new APIException(response.getError());
        }

        loadCollection();
        DialogManager.createAlert(
          localizator.getKeyString("RemoveByID"), localizator.getKeyString("RemoveByIDSuc"), Alert.AlertType.INFORMATION, false
        );
      } catch (APIException | ErrorResponseException e) {
        DialogManager.alert("RemoveByIDErr", localizator);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      } catch (NumberFormatException e) {
        DialogManager.alert("NumberFormatException", localizator);
      } catch (BadOwnerException e) {
        DialogManager.alert("BadOwnerError", localizator);
      } catch (NotFoundException e) {
        DialogManager.alert("NotFoundException", localizator);
      }
    }
  }

  @FXML
  public void clear() {
    try {
      var response = (ClearResponse) client.sendAndReceiveCommand(new ClearRequest(SessionHandler.getCurrentUser()));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      loadCollection();
      DialogManager.createAlert(
        localizator.getKeyString("Clear"), localizator.getKeyString("ClearSuc"), Alert.AlertType.INFORMATION, false
      );
    } catch (APIException | ErrorResponseException e) {
      DialogManager.alert("ClearErr", localizator);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void executeScript() {
    var chooser = new FileChooser();
    chooser.setInitialDirectory(new File("."));
    var file = chooser.showOpenDialog(stage);
    if (file != null) {
      var result = (new ScriptExecutor(this, localizator)).run(file.getAbsolutePath());
      if (result == ScriptExecutor.ExitCode.ERROR) {
        DialogManager.alert("ScriptExecutionErr", localizator);
      } else {
        DialogManager.info("ScriptExecutionSuc", localizator);
      }
    }
  }

  @FXML
  public void head() {
    try {
      var response = (HeadResponse) client.sendAndReceiveCommand(new HeadRequest(SessionHandler.getCurrentUser()));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      DialogManager.createAlert(
        localizator.getKeyString("Head"),
        localizator.getKeyString("HeadResult") + (new ProductPresenter(localizator)).describe(response.product),
        Alert.AlertType.INFORMATION,
        false
      );
    } catch (APIException | ErrorResponseException e) {
      DialogManager.alert("HeadError", localizator);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void addIfMax() {
    editController.clear();
    editController.show();
    var product = editController.getProduct();
    if (product != null) {
      product = product.copy(product.getId(), SessionHandler.getCurrentUser());

      try {
        var response = (AddIfMaxResponse) client.sendAndReceiveCommand(new AddIfMaxRequest(product, SessionHandler.getCurrentUser()));
        if (response.getError() != null && !response.getError().isEmpty()) {
          throw new APIException(response.getError());
        }

        loadCollection();
        if (response.isAdded) {
          DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddResult"), Alert.AlertType.INFORMATION, false);
        } else {
          DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddNotMax"), Alert.AlertType.INFORMATION, false);
        }
      } catch (APIException | ErrorResponseException e) {
        DialogManager.alert("AddErr", localizator);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      }
    }
  }

  @FXML
  public void addIfMin() {
    editController.clear();
    editController.show();
    var product = editController.getProduct();
    if (product != null) {
      product = product.copy(product.getId(), SessionHandler.getCurrentUser());

      try {
        var response = (AddIfMinResponse) client.sendAndReceiveCommand(new AddIfMinRequest(product, SessionHandler.getCurrentUser()));
        if (response.getError() != null && !response.getError().isEmpty()) {
          throw new APIException(response.getError());
        }

        loadCollection();
        if (response.isAdded) {
          DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddResult"), Alert.AlertType.INFORMATION, false);
        } else {
          DialogManager.createAlert(localizator.getKeyString("Add"), localizator.getKeyString("AddNotMin"), Alert.AlertType.INFORMATION, false);
        }
      } catch (APIException | ErrorResponseException e) {
        DialogManager.alert("AddErr", localizator);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      }
    }
  }

  @FXML
  public void sumOfPrice() {
    try {
      var response = (SumOfPriceResponse) client.sendAndReceiveCommand(new SumOfPriceRequest(SessionHandler.getCurrentUser()));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      DialogManager.createAlert(
        localizator.getKeyString("SumOfPrice"),
        MessageFormat.format(localizator.getKeyString("SumOfPriceResult"), String.valueOf(response.sum)),
        Alert.AlertType.INFORMATION,
        false
      );
    } catch (APIException | ErrorResponseException e) {
      DialogManager.alert("SumOfPriceError", localizator);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void filterByPrice() {
    var dialogPrice = new TextInputDialog();
    dialogPrice.setTitle(localizator.getKeyString("FilterByPrice"));
    dialogPrice.setHeaderText(null);
    dialogPrice.setContentText(localizator.getKeyString("DialogPrice"));
    var price = dialogPrice.showAndWait();

    if (price.isPresent() && !price.get().trim().equals("")) {
      try {
        var response = (FilterByPriceResponse) client.sendAndReceiveCommand(new FilterByPriceRequest(Long.parseLong(price.orElse("")), SessionHandler.getCurrentUser()));
        if (response.getError() != null && !response.getError().isEmpty()) {
          throw new APIException(response.getError());
        }

        var result = new StringBuilder();
        response.filteredProducts.forEach(product -> {
          result.append(new ProductPresenter(localizator).describe(product)).append("\n\n");
        });

        DialogManager.createAlert(
          localizator.getKeyString("FilterByPrice"),
          MessageFormat.format(localizator.getKeyString("FilterByPriceResult"), String.valueOf(response.filteredProducts.size())) + result,
          Alert.AlertType.INFORMATION,
          true
        );
      } catch (APIException | ErrorResponseException e) {
        DialogManager.alert("FilterByPriceError", localizator);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      } catch (NumberFormatException e) {
        DialogManager.alert("NumberFormatException", localizator);
      }
    }
  }

  @FXML
  public void filterContainsPartNumber() {
    var dialogPartNumber = new TextInputDialog();
    dialogPartNumber.setTitle(localizator.getKeyString("FilterContainsPartNumber"));
    dialogPartNumber.setHeaderText(null);
    dialogPartNumber.setContentText(localizator.getKeyString("PartNumber") + ": ");

    var partNumber = dialogPartNumber.showAndWait();
    if (partNumber.isPresent() && !partNumber.get().trim().equals("")) {
      try {
        var response = (FilterContainsPartNumberResponse) client.sendAndReceiveCommand(
          new FilterContainsPartNumberRequest(partNumber.get().trim(), SessionHandler.getCurrentUser())
        );
        if (response.getError() != null && !response.getError().isEmpty()) {
          throw new APIException(response.getError());
        }

        var result = new StringBuilder();
        response.filteredProducts.forEach(product -> {
          result.append(new ProductPresenter(localizator).describe(product)).append("\n\n");
        });

        DialogManager.createAlert(
          localizator.getKeyString("FilterContainsPartNumber"),
          MessageFormat.format(
            localizator.getKeyString("FilterContainsPartNumberResult"),
            String.valueOf(response.filteredProducts.size())
          ) + result,
          Alert.AlertType.INFORMATION,
          true
        );
      } catch (APIException | ErrorResponseException e) {
        DialogManager.alert("FilterContainsPartNumberError", localizator);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      }
    }
  }

  public void refresh() {
    Thread refresher = new Thread(() -> {
      while (isRefreshing()) {
        Platform.runLater(this::loadCollection);
        try {
          Thread.sleep(10_000);
        } catch (InterruptedException ignored) {
          Thread.currentThread().interrupt();
          System.out.println("Thread was interrupted, Failed to complete operation");
        }
      }
    });
    refresher.start();
  }

  public void visualise(boolean refresh) {
    visualPane.getChildren().clear();
    infoMap.clear();

    for (var product : tableTable.getItems()) {
      var creatorName = product.getCreator().getName();

      if (!colorMap.containsKey(creatorName)) {
        var r = random.nextDouble();
        var g = random.nextDouble();
        var b = random.nextDouble();
        if (Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g) < 0.6) {
          r += (1 - r) / 1.4;
          g += (1 - g) / 1.4;
          b += (1 - b) / 1.4;
        }
        colorMap.put(creatorName, Color.color(r, g, b));
      }

      var size = Math.min(125, Math.max(75, product.getPrice() * 2) / 2);

      var circle = new Circle(size, colorMap.get(creatorName));
      double x = Math.abs(product.getCoordinates().getX());
      while (x >= 720) {
        x = x / 10;
      }
      double y = Math.abs(product.getCoordinates().getY());
      while (y >= 370) {
        y = y / 3;
      }
      if (y < 100) y += 125;

      var id = new Text('#' + String.valueOf(product.getId()));
      var info = new Label(new ProductPresenter(localizator).describe(product));

      info.setVisible(false);
      circle.setOnMouseClicked(mouseEvent -> {
        if (mouseEvent.getClickCount() == 2) {
          doubleClickUpdate(product);
        }
      });

      circle.setOnMouseEntered(mouseEvent -> {
        id.setVisible(false);
        info.setVisible(true);
        circle.setFill(colorMap.get(creatorName).brighter());
      });

      circle.setOnMouseExited(mouseEvent -> {
        id.setVisible(true);
        info.setVisible(false);
        circle.setFill(colorMap.get(creatorName));
      });

      id.setFont(Font.font("Segoe UI", size / 1.4));
      info.setStyle("-fx-background-color: white; -fx-border-color: #c0c0c0; -fx-border-width: 2");
      info.setFont(Font.font("Segoe UI", 15));

      visualPane.getChildren().add(circle);
      visualPane.getChildren().add(id);

      infoMap.put(product.getId(), info);
      if (!refresh) {
        var path = new Path();
        path.getElements().add(new MoveTo(-500, -150));
        path.getElements().add(new HLineTo(x));
        path.getElements().add(new VLineTo(y));
        id.translateXProperty().bind(circle.translateXProperty().subtract(id.getLayoutBounds().getWidth() / 2));
        id.translateYProperty().bind(circle.translateYProperty().add(id.getLayoutBounds().getHeight() / 4));
        info.translateXProperty().bind(circle.translateXProperty().add(circle.getRadius()));
        info.translateYProperty().bind(circle.translateYProperty().subtract(120));
        var transition = new PathTransition();
        transition.setDuration(Duration.millis(750));
        transition.setNode(circle);
        transition.setPath(path);
        transition.setOrientation(PathTransition.OrientationType.NONE);
        transition.play();
      } else {
        circle.setCenterX(x);
        circle.setCenterY(y);
        info.translateXProperty().bind(circle.centerXProperty().add(circle.getRadius()));
        info.translateYProperty().bind(circle.centerYProperty().subtract(120));
        id.translateXProperty().bind(circle.centerXProperty().subtract(id.getLayoutBounds().getWidth() / 2));
        id.translateYProperty().bind(circle.centerYProperty().add(id.getLayoutBounds().getHeight() / 4));
        var darker = new FillTransition(Duration.millis(750), circle);
        darker.setFromValue(colorMap.get(creatorName));
        darker.setToValue(colorMap.get(creatorName).darker().darker());
        var brighter = new FillTransition(Duration.millis(750), circle);
        brighter.setFromValue(colorMap.get(creatorName).darker().darker());
        brighter.setToValue(colorMap.get(creatorName));
        var transition = new SequentialTransition(darker, brighter);
        transition.play();
      }
    }

    for (var id : infoMap.keySet()) {
      visualPane.getChildren().add(infoMap.get(id));
    }
  }

  private void loadCollection() {
    try {
      var response = (ShowResponse) client.sendAndReceiveCommand(new ShowRequest(SessionHandler.getCurrentUser()));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      setCollection(response.products);
      visualise(true);
    } catch (SocketTimeoutException e) {
      DialogManager.alert("RefreshLost", localizator);
    } catch (APIException | ErrorResponseException e) {
      DialogManager.alert("RefreshFailed", localizator);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  private void doubleClickUpdate(Product product) {
    doubleClickUpdate(product, true);
  }

  private void doubleClickUpdate(Product product, boolean ignoreAnotherUser) {
    if (ignoreAnotherUser && product.getCreatorId() != SessionHandler.getCurrentUser().getId()) return;

    editController.fill(product);
    editController.show();

    var updatedProduct = editController.getProduct();
    if (updatedProduct != null) {
      updatedProduct = updatedProduct.copy(product.getId(), SessionHandler.getCurrentUser());

      if (product.getManufacturer() != null && updatedProduct.getManufacturer() != null) {
        updatedProduct.getManufacturer().setId(product.getManufacturer().getId());
      }

      try {
        if (!updatedProduct.validate()) throw new InvalidFormException();

        var response = (UpdateResponse) client.sendAndReceiveCommand(new UpdateRequest(product.getId(), updatedProduct, SessionHandler.getCurrentUser()));
        if (response.getError() != null && !response.getError().isEmpty()) {
          if (response.getError().contains("BAD_OWNER")) {
            throw new BadOwnerException();
          }
          throw new APIException(response.getError());
        }

        loadCollection();
        DialogManager.createAlert(
          localizator.getKeyString("Update"), localizator.getKeyString("UpdateSuc"), Alert.AlertType.INFORMATION, false
        );
      } catch (APIException | ErrorResponseException e) {
        DialogManager.createAlert(localizator.getKeyString("Error"), localizator.getKeyString("UpdateErr"), Alert.AlertType.ERROR, false);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      } catch (InvalidFormException e) {
        DialogManager.createAlert(
          localizator.getKeyString("Update"), localizator.getKeyString("InvalidProduct"), Alert.AlertType.INFORMATION, false
        );
      } catch (BadOwnerException e) {
        DialogManager.alert("BadOwnerError", localizator);
      }
    }
  }

  public void changeLanguage() {
    userLabel.setText(localizator.getKeyString("UserLabel") + " " + SessionHandler.getCurrentUser().getName());

    exitButton.setText(localizator.getKeyString("Exit"));
    logoutButton.setText(localizator.getKeyString("LogOut"));
    helpButton.setText(localizator.getKeyString("Help"));
    infoButton.setText(localizator.getKeyString("Info"));
    addButton.setText(localizator.getKeyString("Add"));
    updateButton.setText(localizator.getKeyString("Update"));
    removeByIdButton.setText(localizator.getKeyString("RemoveByID"));
    clearButton.setText(localizator.getKeyString("Clear"));
    executeScriptButton.setText(localizator.getKeyString("ExecuteScript"));
    headButton.setText(localizator.getKeyString("Head"));
    addIfMaxButton.setText(localizator.getKeyString("AddIfMax"));
    addIfMinButton.setText(localizator.getKeyString("AddIfMin"));
    sumOfPriceButton.setText(localizator.getKeyString("SumOfPrice"));
    filterByPriceButton.setText(localizator.getKeyString("FilterByPrice"));
    filterContainsPartNumberButton.setText(localizator.getKeyString("FilterContainsPartNumber"));

    tableTab.setText(localizator.getKeyString("TableTab"));
    visualTab.setText(localizator.getKeyString("VisualTab"));

    ownerColumn.setText(localizator.getKeyString("Owner"));
    nameColumn.setText(localizator.getKeyString("Name"));
    dateColumn.setText(localizator.getKeyString("CreationDate"));
    priceColumn.setText(localizator.getKeyString("Price"));
    partNumberColumn.setText(localizator.getKeyString("PartNumber"));
    unitOfMeasureColumn.setText(localizator.getKeyString("UnitOfMeasure"));

    manufacturerIdColumn.setText(localizator.getKeyString("ManufacturerId"));
    manufacturerNameColumn.setText(localizator.getKeyString("ManufacturerName"));
    manufacturerEmployeesCountColumn.setText(localizator.getKeyString("ManufacturerEmployeesCount"));
    manufacturerTypeColumn.setText(localizator.getKeyString("ManufacturerType"));
    manufacturerStreetColumn.setText(localizator.getKeyString("ManufacturerStreet"));
    manufacturerZipCodeColumn.setText(localizator.getKeyString("ManufacturerZipCode"));

    editController.changeLanguage();

    loadCollection();
  }

  public void setCollection(List<Product> collection) {
    this.collection = collection;
    tableTable.setItems(FXCollections.observableArrayList(collection));
  }

  public void setAuthCallback(Runnable authCallback) {
    this.authCallback = authCallback;
  }

  public void setContext(UDPClient client, Localizator localizator, Stage stage) {
    this.client = client;
    this.localizator = localizator;
    this.stage = stage;

    languageComboBox.setValue(SessionHandler.getCurrentLanguage());
    localizator.setBundle(ResourceBundle.getBundle("locales/gui", localeMap.get(SessionHandler.getCurrentLanguage())));
    changeLanguage();

    userLabel.setText(localizator.getKeyString("UserLabel") + " " + SessionHandler.getCurrentUser().getName());
  }

  public boolean isRefreshing() {
    return isRefreshing;
  }

  public void setRefreshing(boolean refreshing) {
    isRefreshing = refreshing;
  }

  public void setEditController(EditController editController) {
    this.editController = editController;
    editController.changeLanguage();
  }
}
