package client.controllers;

import client.auth.SessionHandler;
import client.ui.DialogManager;
import client.utility.Localizator;
import common.domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
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

    xField.textProperty().addListener((observableValue, oldValue, newValue) -> {
      if (!newValue.matches("[-\\d]{0,11}")) {
        xField.setText(oldValue);
      } else {
        if (newValue.matches(".+-.*")) {
          Platform.runLater(() -> xField.clear());
        } else if (
          newValue.length() == 10 && Long.parseLong(newValue) > Integer.MAX_VALUE
            || newValue.length() == 11 && Long.parseLong(newValue) < Integer.MIN_VALUE
        ) {
          xField.setText(oldValue);
        }
      }
    });

    yField.textProperty().addListener((observableValue, oldValue, newValue) -> {
      if (!newValue.matches("[-\\d]{0,20}")) {
        yField.setText(oldValue);
      } else {
        if (newValue.matches(".+-.*")) {
          Platform.runLater(() -> yField.clear());
        } else if (!newValue.isEmpty() && (
          newValue.length() == 19 && new BigInteger(newValue).compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
            || newValue.length() == 20 && new BigInteger(newValue).compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0
        )) {
          yField.setText(oldValue);
        }
      }
    });

    Arrays.asList(priceField, mEmployeesCountField).forEach(field -> {
      field.textProperty().addListener((observableValue, oldValue, newValue) -> {
        if (!field.isDisabled()) {
          if (!newValue.matches("\\d{0,19}")) {
            field.setText(oldValue);
          } else {
            if (!newValue.isEmpty() && (
              new BigInteger(newValue).compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
                || new BigInteger(newValue).compareTo(new BigInteger(String.valueOf(0))) <= 0
            )) {
              field.setText(oldValue);
            }
          }

        }
      });
    });
  }

  @FXML
  public void ok() {
    nameField.setText(nameField.getText().trim());
    partNumberField.setText(partNumberField.getText().trim());
    mNameField.setText(mNameField.getText().trim());
    mStreetField.setText(mStreetField.getText().trim());
    mZipCodeField.setText(mZipCodeField.getText().trim());

    var errors = new ArrayList<String>();

    Organization organization = null;
    if (hasManufacturerBox.getValue().equals("TRUE")) {
      if (mNameField.getText().isEmpty()) errors.add(
        "- " + localizator.getKeyString("ManufacturerName") + " " + localizator.getKeyString("CannotBeEmpty")
      );
      if (mStreetField.getText().isEmpty()) errors.add(
        "- " + localizator.getKeyString("ManufacturerStreet") + " " + localizator.getKeyString("CannotBeEmpty")
      );

      String zipCode = mZipCodeField.getText();
      if (mZipCodeField.getText().isEmpty()) {
        zipCode = null;
      } else if (zipCode.length() < 6) {
        errors.add("- " + localizator.getKeyString("ZipCodeLength"));
      }

      OrganizationType organizationType = null;
      if (mTypeBox.getValue() != null) {
        organizationType = OrganizationType.valueOf(mTypeBox.getValue());
      } else {
        errors.add("- " + localizator.getKeyString("ManufacturerType") + " " + localizator.getKeyString("CannotBeEmpty"));
      }

      organization = new Organization(
        -1,
        mNameField.getText(),
        Long.parseLong(mEmployeesCountField.getText()),
        organizationType,
        new Address(
          mStreetField.getText(),
          zipCode
        )
      );
    }

    if (nameField.getText().isEmpty()) errors.add(
      "- " + localizator.getKeyString("Name") + " " + localizator.getKeyString("CannotBeEmpty")
    );

    var partNumber = partNumberField.getText();
    if (partNumberField.getText().isEmpty()) partNumber = null;

    UnitOfMeasure unitOfMeasure = null;
    if (unitOfMeasureBox.getValue() != null) unitOfMeasure = UnitOfMeasure.valueOf(unitOfMeasureBox.getValue());

    if (!errors.isEmpty()) {
      DialogManager.createAlert(localizator.getKeyString("Error"), String.join("\n", errors), Alert.AlertType.ERROR, false);
    } else {
      var newProduct = new Product(
        -1,
        nameField.getText(),
        new Coordinates(Integer.parseInt(xField.getText()), Long.parseLong(yField.getText())),
        LocalDate.now(),
        Long.parseLong(priceField.getText()),
        partNumber,
        unitOfMeasure,
        organization,
        SessionHandler.getCurrentUser()
      );
      if (!newProduct.validate()) {
        DialogManager.alert("InvalidProduct", localizator);
      } else {
        product = newProduct;
        stage.close();
      }
    }
  }

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
    } else {
      mNameField.clear();
      mEmployeesCountField.clear();
      mTypeBox.valueProperty().setValue(null);
      mStreetField.clear();
      mZipCodeField.clear();
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
    if (!stage.isShowing()) stage.showAndWait();
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setLocalizator(Localizator localizator) {
    this.localizator = localizator;
  }
}
