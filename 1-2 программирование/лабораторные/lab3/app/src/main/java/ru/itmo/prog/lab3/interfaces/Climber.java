package ru.itmo.prog.lab3.interfaces;

import ru.itmo.prog.lab3.models.places.Place;

public interface Climber {
  String climb(Climbable climbable);
  String climb(Climbable climbable, Place place);

  int calculateTimeToClimb();
}
