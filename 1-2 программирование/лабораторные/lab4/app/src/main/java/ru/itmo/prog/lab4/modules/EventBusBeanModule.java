package ru.itmo.prog.lab4.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ru.itmo.prog.lab4.lib.events.*;
import ru.itmo.prog.lab4.lib.events.interfaces.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class EventBusBeanModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(EventBus.class)
      .annotatedWith(Names.named("bus"))
      .to(EventBusImpl.class);

    var instances = findAllClasses("ru.itmo.prog.lab4.models.events")
      .stream()
      .filter(aClass -> aClass.getName().endsWith("$Handler"))
      .filter(
        aClass -> Arrays.stream(aClass.getDeclaredConstructors())
          .anyMatch(constructor -> constructor.getParameterCount() == 0)
      )
      .map(aClass -> {
        try {
          return Optional.of(aClass.getConstructor().newInstance());
        } catch (Exception e) {
          return Optional.empty();
        }
      });

    bind(new TypeLiteral<List<EventHandler<?>>>() {})
      .annotatedWith(Names.named("handlers"))
      .toInstance(new ArrayList<>() {{
        instances
          .flatMap(Optional::stream)
          .collect(Collectors.toList())
          .forEach(instance -> add((EventHandler<?>) instance));
      }});
  }

  public Set<Class<?>> findAllClasses(String packageName) {
    var stream = ClassLoader
      .getSystemClassLoader()
      .getResourceAsStream(packageName.replaceAll("[.]", "/"));

    return new BufferedReader(new InputStreamReader(stream)).lines()
      .filter(line -> line.endsWith(".class"))
      .map(line -> getClass(line, packageName))
      .collect(Collectors.toSet());
  }

  private Class<?> getClass(String className, String packageName) {
    try {
      return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
    } catch (ClassNotFoundException e) {
      System.err.println("EventBusBeam: Class not found, " + e.getMessage());
    }

    return null;
  }
}
