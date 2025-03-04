package lab1.task1.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrigonometricTest {

  @ParameterizedTest(name = "arcsin({0})")
  @DisplayName("Check corner values")
  @ValueSource(doubles = {
    -999.9,
    -1.0000001,
    -1.0,
    -0.99
    -0.5,
    -0.000001,
    -0.0001,
    -0.0,
    0.0,
    0.0001,
    0.000001,
    0.5,
    0.99,
    1.0,
    1.0000001,
    999.9,
    Double.NaN,
    Double.POSITIVE_INFINITY,
    Double.MIN_VALUE,
  })
  void checkCornerDots(double param) {
    assertAll(
      () -> assertEquals(Math.asin(param), Trigonometric.arcsin(param), 0.00001)
    );
  }

  @ParameterizedTest(name = "arcsin({0}) = {1}")
  @DisplayName("Check between dots [-1; +1]")
  @CsvFileSource(resources = "/table_values.csv", numLinesToSkip = 1, delimiter = ';')
  void checkBetweenDotsMinus1And1(double x, double y) {
    assertAll(
      () -> assertEquals(y, Trigonometric.arcsin(x), 0.0001)
    );
  }

  @Test
  @DisplayName("Fuzzy testing")
  void checkRandomDots() {
    for (int i = 0; i < 1_000_000; i++) {
      double randomValue = ThreadLocalRandom.current().nextDouble(-0.9999, 0.9999);
      assertEquals(Math.asin(randomValue), Trigonometric.arcsin(randomValue), 0.0001);
    }
  }
}
