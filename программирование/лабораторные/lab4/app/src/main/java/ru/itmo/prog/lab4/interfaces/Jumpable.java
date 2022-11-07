package ru.itmo.prog.lab4.interfaces;

import ru.itmo.prog.lab4.models.places.Place;

public interface Jumpable {
  public String jumpTo(Place place, double distance);
}
