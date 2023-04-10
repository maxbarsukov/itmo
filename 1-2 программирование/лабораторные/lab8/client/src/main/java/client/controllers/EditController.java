package client.controllers;

import client.utility.Localizator;
import common.domain.OrganizationType;
import common.domain.Product;
import common.domain.UnitOfMeasure;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EditController {
  private Stage stage;
  private Product product;
  private Localizator localizator;

  @FXML
  private Label titleLabel;
  @FXML
  private Label nameLabel;
  @FXML
  private Label priceLabel;
  @FXML
  private Label partNumberLabel;
  @FXML
  private Label unitOfMeasureLabel;
  @FXML
  private Label hasManufacturer;
  @FXML
  private Label mNameLabel;
  @FXML
  private Label mEmployeesCountLabel;
  @FXML
  private Label mTypeLabel;
  @FXML
  private Label mStreetLabel;
  @FXML
  private Label mZipCodeLabel;

  @FXML
  private TextField nameField;
  @FXML
  private TextField xField;
  @FXML
  private TextField yField;
  @FXML
  private TextField priceField;
  @FXML
  private TextField partNumberField;
  @FXML
  private TextField mNameField;
  @FXML
  private TextField mEmployeesCountField;
  @FXML
  private TextField mStreetField;
  @FXML
  private TextField mZipCodeField;

  @FXML
  private ChoiceBox<String> unitOfMeasureBox;
  @FXML
  private ChoiceBox<String> hasManufacturerBox;
  @FXML
  private ChoiceBox<String> mTypeBox;

  @FXML
  private Button cancelButton;

  @FXML
  void initialize() {
    cancelButton.setOnAction(event -> stage.close());
    var orgTypes = FXCollections.observableArrayList(
      Arrays.stream(OrganizationType.values()).map(Enum::toString).collect(Collectors.toList())
    );
    mTypeBox.setItems(orgTypes);
    mTypeBox.setStyle("-fx-font: 12px \"Sergoe UI\";");

    var unitOfMeasures = FXCollections.observableArrayList(
      Arrays.stream(UnitOfMeasure.values()).map(Enum::toString).collect(Collectors.toList())
    );
    unitOfMeasureBox.setItems(unitOfMeasures);
    unitOfMeasureBox.setStyle("-fx-font: 12px \"Sergoe UI\";");

    var hasManufacturer = FXCollections.observableArrayList("TRUE", "FALSE");
    hasManufacturerBox.setItems(hasManufacturer);
    hasManufacturerBox.setValue("FALSE");
    hasManufacturerBox.setStyle("-fx-font: 12px \"Sergoe UI\";");


    Arrays.asList(mNameField, mEmployeesCountField, mStreetField, mZipCodeField, mTypeBox).forEach(field -> {
      field.disableProperty().bind(
        hasManufacturerBox.getSelectionModel().selectedItemProperty().isEqualTo("FALSE")
      );
    });

//    healthField.textProperty().addListener((observableValue, oldValue, newValue) -> {
//      if (!newValue.matches("\\d{0,10}")) {
//        healthField.setText(oldValue);
//      } else if (newValue.length() == 10 && Long.parseLong(newValue) > Integer.MAX_VALUE || newValue.length() == 1 && Integer.parseInt(newValue) == 0) {
//        healthField.setText(oldValue);
//      }
//    });
//    xField.textProperty().addListener((observableValue, oldValue, newValue) -> {
//      if (!newValue.matches("[-\\d]{0,11}")) {
//        xField.setText(oldValue);
//      } else {
//        if (newValue.matches(".+-.*")) {
//          Platform.runLater(() -> xField.clear());
//        } else if (newValue.length() == 10 && Long.parseLong(newValue) > Integer.MAX_VALUE || newValue.length() == 11 && Long.parseLong(newValue) < Integer.MIN_VALUE) {
//          xField.setText(oldValue);
//        }
//      }
//    });
//    yField.textProperty().addListener((observableValue, oldValue, newValue) -> {
//      if (!newValue.matches("[-\\d]{0,11}")) {
//        yField.setText(oldValue);
//      } else {
//        if (newValue.matches(".+-.*")) {
//          Platform.runLater(() -> yField.clear());
//        } else if (newValue.length() >= 3 && Long.parseLong(newValue) > 941 || newValue.length() == 11 && Long.parseLong(newValue) < Integer.MIN_VALUE) {
//          yField.setText(oldValue);
//        }
//      }
//    });
  }

  @FXML
  public void ok() {}

  public Product getProduct() {
    var tmpProduct = product;
    product = null;
    return tmpProduct;
  }

  public void clear() {
    nameField.clear();
    xField.clear();
    yField.clear();
    priceField.clear();
    partNumberField.clear();
    unitOfMeasureBox.valueProperty().setValue(null);
    hasManufacturerBox.valueProperty().setValue("FALSE");

    mNameField.clear();
    mEmployeesCountField.clear();
    mTypeBox.valueProperty().setValue(null);
    mStreetField.clear();
    mZipCodeField.clear();
  }

  public void fill(Product product) {
    nameField.setText(product.getName());
    xField.setText(Integer.toString(product.getCoordinates().getX()));
    yField.setText(Long.toString(product.getCoordinates().getY()));
    priceField.setText(Long.toString(product.getPrice()));
    partNumberField.setText(product.getPartNumber());
    unitOfMeasureBox.setValue(product.getUnitOfMeasure() == null ? null : product.getUnitOfMeasure().toString());
    hasManufacturerBox.setValue(product.getManufacturer() == null ? "FALSE" : "TRUE");

    if (product.getManufacturer() != null) {
      var manufacturer = product.getManufacturer();
      mNameField.setText(manufacturer.getName());
      mEmployeesCountField.setText(Long.toString(manufacturer.getEmployeesCount()));
      mTypeBox.setValue(manufacturer.getType().toString());
      mStreetField.setText(manufacturer.getPostalAddress().getStreet());
      mZipCodeField.setText(manufacturer.getPostalAddress().getZipCode());
    }
  }

  public void changeLanguage() {
    titleLabel.setText(localizator.getKeyString("EditTitle"));
    nameLabel.setText(localizator.getKeyString("Name"));
    priceLabel.setText(localizator.getKeyString("Price"));
    partNumberLabel.setText(localizator.getKeyString("PartNumber"));
    unitOfMeasureLabel.setText(localizator.getKeyString("UnitOfMeasure"));
    hasManufacturer.setText(localizator.getKeyString("HasManufacturer"));
    mNameLabel.setText(localizator.getKeyString("ManufacturerName"));
    mEmployeesCountLabel.setText(localizator.getKeyString("ManufacturerEmployeesCount"));
    mTypeLabel.setText(localizator.getKeyString("ManufacturerType"));
    mStreetLabel.setText(localizator.getKeyString("ManufacturerStreet"));
    mZipCodeLabel.setText(localizator.getKeyString("ManufacturerZipCode"));

    cancelButton.setText(localizator.getKeyString("CancelButton"));
  }

  public void show() {
    stage.showAndWait();
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setLocalizator(Localizator localizator) {
    this.localizator = localizator;
  }
}
