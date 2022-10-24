package ru.itmo.prog.lab3.models;

public enum JumpDistance {
  DEFAULT(3),
  BIG(10);

  private final int distance;

  JumpDistance(int distance) {
    this.distance = distance;
  }

  public int getDistance() {
    return distance;
  }
}
