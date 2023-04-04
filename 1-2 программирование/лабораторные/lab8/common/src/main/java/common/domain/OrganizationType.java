package common.domain;

import java.io.Serializable;

/**
 * Перечисление типов организации.
 * @author maxbarsukov
 */
public enum OrganizationType implements Serializable {
  COMMERCIAL,
  GOVERNMENT,
  TRUST,
  PRIVATE_LIMITED_COMPANY;

  /**
   * @return Строка со всеми элементами enum'а через строку.
   */
  public static String names() {
    StringBuilder nameList = new StringBuilder();
    for (var weaponType : values()) {
      nameList.append(weaponType.name()).append(", ");
    }
    return nameList.substring(0, nameList.length()-2);
  }
}
