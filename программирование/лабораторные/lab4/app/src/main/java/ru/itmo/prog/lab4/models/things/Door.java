package ru.itmo.prog.lab4.models.things;

import ru.itmo.prog.lab4.interfaces.things.Lockable;
import ru.itmo.prog.lab4.interfaces.things.Openable;
import ru.itmo.prog.lab4.interfaces.things.Opener;
import ru.itmo.prog.lab4.models.places.Place;

public class Door implements Openable, Lockable {
  public class Doorhandle extends Thing {
    @Override
    public String dativeCase() {
      return "дверной ручке";
    }

    @Override
    public String genitiveCase() {
      return "дверной ручки";
    }

    @Override
    public String toString() {
      return "Дверная ручка";
    }
  }

  enum State {
    OPENED,
    CLOSED,
    LOCKED,
  }

  private final Place entranceTo;
  private State state;

  public Door(Place entranceTo) {
    this.entranceTo = entranceTo;
    this.state = State.OPENED;
  }

  public Door(Place entranceTo, State state) {
    this.entranceTo = entranceTo;
    this.state = state;
  }

  public boolean isOpened() {
    return state == State.OPENED;
  }

  @Override
  public void lock(Opener locker) {
    if (state == State.LOCKED) {
      throw new LockingException("Дверь уже заперта");
    } else if (state == State.CLOSED) {
      throw new LockingException("Дверь должна быть закрыта");
    } else {
      state = State.LOCKED;
    }
  }

  @Override
  public void unlock(Opener locker) {
    if (state == State.OPENED) {
      throw new LockingException("Дверь уже открыта");
    } else if (state == State.CLOSED) {
      throw new LockingException("Дверь должна быть заперта");
    }

    if (!locker.canOpen(this)) {
      throw new LockingException("Дверь не может быть открыта этим ключём");
    }

    state = State.CLOSED;
  }

  @Override
  public void open() {
    if (state == State.LOCKED) throw new LockingException("Дверь заперта");
    state = State.OPENED;
  }

  @Override
  public void close() {
    if (state == State.LOCKED) throw new LockingException("Дверь заперта");
    state = State.CLOSED;
  }

  public Place getEntranceTo() {
    return entranceTo;
  }
}
