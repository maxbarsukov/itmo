package ru.itmo.prog.lab3.interfaces;

import ru.itmo.prog.lab3.models.people.Shorty;

public interface ShortiesContainer {
  boolean checkShorty(Shorty shorty);

  void addShorty(Shorty shorty);
}
