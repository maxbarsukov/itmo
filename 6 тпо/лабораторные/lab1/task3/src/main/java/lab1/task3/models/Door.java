package lab1.task3.models;

import lab1.task3.interfaces.Openable;

public class Door extends SceneObject implements Openable {

  private boolean isOpen;

  public Door(String name) {
    super(name);
    this.isOpen = false;
  }

  @Override
  public void open(Person person) {
    if (!isOpen()) {
      setOpen(true);
      System.out.println(person.getName() + " открыл " + getName() + ".");
    } else {
      System.out.println(person.getName() + " пытается открыть " + getName() + ", но " + getName() + " уже открыт.");
    }
  }

  public void setOpen(boolean open) {
    isOpen = open;
  }

  @Override
  public boolean isOpen() {
    return isOpen;
  }
}
