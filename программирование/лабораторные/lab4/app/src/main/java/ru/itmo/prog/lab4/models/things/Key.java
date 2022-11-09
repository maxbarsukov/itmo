package ru.itmo.prog.lab4.models.things;

import ru.itmo.prog.lab4.interfaces.things.Lockable;
import ru.itmo.prog.lab4.interfaces.things.Opener;

import java.util.ArrayList;
import java.util.List;

public class Key implements Opener {
  private String description;
  private List<Lockable> lockables;

  public Key(String description, Lockable lockable) {
    this.description = description;
    this.lockables = new ArrayList<Lockable>() {{ add(lockable); }};
  }

  public Key(String description, List<Lockable> lockables) {
    this.description = description;
    this.lockables = lockables;
  }

  public boolean canOpen(Lockable lockable) {
    return lockables.contains(lockable);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Lockable> getLockables() {
    return lockables;
  }

  public void setLockables(List<Lockable> lockables) {
    this.lockables = lockables;
  }
}
