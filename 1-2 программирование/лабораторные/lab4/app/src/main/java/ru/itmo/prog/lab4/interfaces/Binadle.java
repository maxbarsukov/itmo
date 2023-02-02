package ru.itmo.prog.lab4.interfaces;

public interface Binadle<F, G> {
  void bind(F first, G second);
  void unbind();
}
