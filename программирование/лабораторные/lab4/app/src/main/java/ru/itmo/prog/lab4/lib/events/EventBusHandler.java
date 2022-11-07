package ru.itmo.prog.lab4.lib.events;

import ru.itmo.prog.lab4.interfaces.events.Event;

import java.lang.reflect.ParameterizedType;

public abstract class EventBusHandler<E extends Event> {
  public Class<E> eventClass;

  private Class<E> getGenericTypeClass() {
    if (eventClass == null) {
      eventClass = (Class<E>)((ParameterizedType)getClass()
        .getGenericSuperclass())
        .getActualTypeArguments()[0];
    }
    return eventClass;
  }

  protected Class<E> getLinkedClass() {
    return getGenericTypeClass();
  }

  public boolean canHandle(Class<? extends Event> klass) {
    return false;
  }

  public void handleEvent(Event event) throws EventException {
    this.handle(getGenericTypeClass().cast(event));
  }

  public abstract void handle(E event) throws EventException;
}
