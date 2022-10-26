package ru.itmo.prog.lab3.interfaces;

import ru.itmo.prog.lab3.models.places.Place;

public interface Jumpable {
  public String jumpTo(Place place, double distance);
}
