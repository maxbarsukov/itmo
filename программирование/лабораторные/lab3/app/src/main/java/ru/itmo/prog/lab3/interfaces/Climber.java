package ru.itmo.prog.lab3.interfaces;

import ru.itmo.prog.lab3.models.things.Place;

public interface Climber {
  String climb(Climbable climbable);
  String climb(Climbable climbable, Place palce);

  int calculateTimeToClimb();
}
