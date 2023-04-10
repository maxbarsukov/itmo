package client.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class Localizator {
  private ResourceBundle bundle;

  public Localizator(ResourceBundle bundle) {
    this.bundle = bundle;
  }

  public void setBundle(ResourceBundle bundle) {
    this.bundle = bundle;
  }

  public String getKeyString(String key) {
    return bundle.getString(key);
  }

  public String getDate(LocalDate date) {
    if (date == null) return "null";
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(bundle.getLocale());
    return date.format(formatter);
  }

  public String getDate(LocalDateTime date) {
    if (date == null) return "null";
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(bundle.getLocale());
    return date.format(formatter);
  }
}
