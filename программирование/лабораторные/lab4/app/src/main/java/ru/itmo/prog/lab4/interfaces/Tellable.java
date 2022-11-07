package ru.itmo.prog.lab4.interfaces;

import com.google.inject.ImplementedBy;
import ru.itmo.prog.lab4.models.scene.Story;

@ImplementedBy(Story.class)
public interface Tellable {
  public void tell();
}
