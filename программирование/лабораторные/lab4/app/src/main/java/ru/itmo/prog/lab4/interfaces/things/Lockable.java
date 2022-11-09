package ru.itmo.prog.lab4.interfaces.things;

public interface Lockable {
  class LockingException extends RuntimeException {
    public LockingException(String message) {
      super(message);
    }
  }

  void lock(Opener locker);

  void unlock(Opener locker);
}
