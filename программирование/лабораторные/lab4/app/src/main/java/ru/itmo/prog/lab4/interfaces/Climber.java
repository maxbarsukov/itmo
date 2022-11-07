package ru.itmo.prog.lab4.interfaces;

import ru.itmo.prog.lab4.models.places.Place;

public interface Climber {
  String climb(Climbable climbable);
  String climb(Climbable climbable, Place place);

  int calculateTimeToClimb();
}
