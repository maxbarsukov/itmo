package ru.itmo.prog.lab4.interfaces;

public interface Hearer {
  boolean hear(Hearable content);

  boolean canHear(Hearable content);
}
