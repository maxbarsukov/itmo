package ru.itmo.prog.lab4.interfaces;

import java.util.function.Supplier;

public interface Cannable {
  String can(Supplier<String> function);
}
