package lab1.task3.models;

public class Person extends SceneObject {

  private boolean isHypnotized;

  public Person(String name) {
    super(name);
    this.isHypnotized = false;
  }

  public boolean isHypnotized() {
    return isHypnotized;
  }

  public void setHypnotized(boolean hypnotized) {
    isHypnotized = hypnotized;
  }
}
