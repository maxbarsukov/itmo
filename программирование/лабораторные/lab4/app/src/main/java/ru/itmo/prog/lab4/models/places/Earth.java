package ru.itmo.prog.lab4.models.places;

public class Earth extends Place {
  private Earth(String name) {
    super(name);
  }

  public static class EarthHolder {
    public static final Earth HOLDER_INSTANCE = new Earth("Земля");
  }

  public static Earth getInstance() {
    return EarthHolder.HOLDER_INSTANCE;
  }

  @Override
  public String dativeCase() {
    return "землю";
  }

  @Override
  public String genitiveCase() {
    return "земли";
  }
}
