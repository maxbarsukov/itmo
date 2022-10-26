package ru.itmo.prog.lab3.interfaces;

import com.google.inject.ImplementedBy;
import ru.itmo.prog.lab3.models.scene.Story;

@ImplementedBy(Story.class)
public interface Tellable {
  public void tell();
}
