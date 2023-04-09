package client.auth;

import common.user.User;

public class SessionHandler {
  public static User currentUser = null;
  public static String currentLanguage = "Русский";

  public static User getCurrentUser() {
    return currentUser;
  }

  public static void setCurrentUser(User currentUser) {
    SessionHandler.currentUser = currentUser;
  }

  public static String getCurrentLanguage() {
    return currentLanguage;
  }

  public static void setCurrentLanguage(String currentLanguage) {
    SessionHandler.currentLanguage = currentLanguage;
  }
}
