package server.utils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class KeywordKeyGenerator {
  private final Key secretKeySpec;

  public KeywordKeyGenerator(String keyword) {
    this(keyword, "HmacSHA512");
  }

  public KeywordKeyGenerator(String keyword, String algorithm) {
    secretKeySpec = new SecretKeySpec(keyword.getBytes(), 0, keyword.getBytes().length, algorithm);
  }

  public Key generate() {
    return secretKeySpec;
  }
}
