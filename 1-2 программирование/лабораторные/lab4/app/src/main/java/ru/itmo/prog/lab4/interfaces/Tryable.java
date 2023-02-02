package ru.itmo.prog.lab4.interfaces;

import java.util.function.Supplier;

public interface Tryable {
  String tryTo(Supplier<String> function);
}
