package ru.itmo.prog.lab4.models.common;

public class Utils {
  public static boolean randomBoolean(){
    return Math.random() < 0.5;
  }

  public static String capitalize(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }
}
