package client.controllers;

import client.auth.SessionHandler;
import client.network.UDPClient;
import client.ui.DialogManager;
import client.utility.Localizator;
import common.exceptions.*;
import common.network.requests.AuthenticateRequest;
import common.network.requests.RegisterRequest;
import common.network.responses.AuthenticateResponse;
import common.network.responses.RegisterResponse;
import common.user.User;

import org.apache.commons.validator.routines.EmailValidator;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.*;

public class AuthController {
  private Runnable callback;
  private Localizator localizator;
  private UDPClient client;
  private final HashMap<String, Locale> localeMap = new HashMap<>() {{
    put("Русский", new Locale("ru", "RU"));
    put("English(IN)", new Locale("en", "IN"));
    put("Íslenska", new Locale("is", "IS"));
    put("Svenska", new Locale("sv", "SE"));
  }};

  @FXML
  private Label titleLabel;
  @FXML
  private TextField loginField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button okButton;
  @FXML
  private CheckBox signUpButton;
  @FXML
  private ComboBox<String> languageComboBox;

  @FXML
  void initialize() {
    languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));

    languageComboBox.setValue(SessionHandler.getCurrentLanguage());
    languageComboBox.setStyle("-fx-font: 13px \"Sergoe UI\";");

    languageComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
      localizator.setBundle(ResourceBundle.getBundle("locales/gui", localeMap.get(newValue)));
      SessionHandler.setCurrentLanguage(newValue);
      changeLanguage();
    });
    loginField.textProperty().addListener((observableValue, oldValue, newValue) -> {
      if (!newValue.matches(".{0,40}")) {
        loginField.setText(oldValue);
      }
    });
    passwordField.textProperty().addListener((observableValue, oldValue, newValue) -> {
      if (!newValue.matches("\\S*")) {
        passwordField.setText(oldValue);
      }
    });
  }

  @FXML
  void ok() {
    if (signUpButton.isSelected()) {
      register();
    } else {
      authenticate();
    }
  }

  public void register() {
    try {
      if (loginField.getText().length() < 1  || loginField.getText().length() > 40 || passwordField.getText().length() < 1) {
        throw new InvalidFormException();
      }

      if (!validateRegister()) return;

      var user = new User(-1, loginField.getText(), passwordField.getText());
      var response = (RegisterResponse) client.sendAndReceiveCommand(new RegisterRequest(user));
      if (response.getError() != null && !response.getError().isEmpty()) {
        if (response.getError().contains("ConstraintViolationException")) {
          throw new UserAlreadyExistsException();
        }
        throw new APIException(response.getError());
      }

      SessionHandler.setCurrentUser(response.user);
      SessionHandler.setCurrentLanguage(languageComboBox.getValue());
      DialogManager.info("RegisterSuccess", localizator);

      callback.run();

    } catch (InvalidFormException exception) {
      DialogManager.alert("InvalidCredentials", localizator);
    } catch (UserAlreadyExistsException exception) {
      DialogManager.alert("UserAlreadyExists", localizator);
    } catch(IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    } catch (APIException | ErrorResponseException e) {
      DialogManager.createAlert(
        localizator.getKeyString("Error"), e.getMessage(), Alert.AlertType.ERROR, false
      );
    }
  }

  private boolean validateRegister() {
    var emailValid = EmailValidator.getInstance(true).isValid(loginField.getText());

    var passwordErrors = validatePassword(passwordField.getText());
    if (!passwordErrors.isEmpty()) {
      String message;
      if (!emailValid) {
        message = localizator.getKeyString("EmailInvalid") + "\n\n" + localizator.getKeyString("PasswordBad");
      } else {
        message = localizator.getKeyString("PasswordBad");
      }
      message += "\n- " + String.join("\n- ", passwordErrors);
      DialogManager.createAlert(localizator.getKeyString("Error"), message, Alert.AlertType.ERROR, false);
      return false;
    } else if (!emailValid) {
      DialogManager.alert("EmailInvalid", localizator);
      return false;
    }
    return true;
  }

  private List<String> validatePassword(String password) {
    var errors = new ArrayList<String>();
    if (password.length() < 8) {
      errors.add(localizator.getKeyString("PasswordMin6"));
    }

    int upChars = 0, lowChars = 0, digits = 0, special = 0;
    for(var i = 0; i < password.length(); i++) {
      var ch = password.charAt(i);
      if (Character.isUpperCase(ch)) {
        upChars++;
      } else if(Character.isLowerCase(ch)) {
        lowChars++;
      } else if(Character.isDigit(ch)) {
        digits++;
      } else {
        special++;
      }
    }

    if (upChars == 0) errors.add(localizator.getKeyString("PasswordContainUp"));
    if (lowChars == 0) errors.add(localizator.getKeyString("PasswordContainLow"));
    if (digits == 0) errors.add(localizator.getKeyString("PasswordContainDigit"));
    if (special == 0) errors.add(localizator.getKeyString("PasswordContainSpecial"));

    return errors;
  }

  public void authenticate() {
    try {
      if (loginField.getText().length() < 1  || loginField.getText().length() > 40 || passwordField.getText().length() < 1) {
        throw new InvalidFormException();
      }

      var user = new User(-1, loginField.getText(), passwordField.getText());
      var response = (AuthenticateResponse) client.sendAndReceiveCommand(new AuthenticateRequest(user));
      if (response.getError() != null && !response.getError().isEmpty()) {
        throw new APIException(response.getError());
      }

      SessionHandler.setCurrentUser(response.user);
      SessionHandler.setCurrentLanguage(languageComboBox.getValue());
      callback.run();

    } catch (InvalidFormException exception) {
      DialogManager.alert("InvalidCredentials", localizator);
    } catch(IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    } catch (APIException | ErrorResponseException e) {
      DialogManager.alert("SignInError", localizator);
    }
  }

  public void changeLanguage() {
    titleLabel.setText(localizator.getKeyString("AuthTitle"));
    loginField.setPromptText(localizator.getKeyString("LoginField"));
    passwordField.setPromptText(localizator.getKeyString("PasswordField"));
    signUpButton.setText(localizator.getKeyString("SignUpButton"));
  }

  public void setCallback(Runnable callback) {
    this.callback = callback;
  }

  public void setClient(UDPClient client) {
    this.client = client;
  }

  public void setLocalizator(Localizator localizator) {
    this.localizator = localizator;
  }
}
