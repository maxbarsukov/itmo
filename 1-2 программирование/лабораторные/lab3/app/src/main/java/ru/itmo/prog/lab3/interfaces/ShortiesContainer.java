package ru.itmo.prog.lab3.interfaces;

import com.google.inject.ImplementedBy;
import ru.itmo.prog.lab3.models.people.Shorty;
import ru.itmo.prog.lab3.models.places.Gazebo;

@ImplementedBy(Gazebo.class)
public interface ShortiesContainer {
  boolean checkShorty(Shorty shorty);

  void addShorty(Shorty shorty);
}
