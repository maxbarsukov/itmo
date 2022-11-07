package ru.itmo.prog.lab4.lib.events;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

class WeakHandler<H extends EventBusHandler<?>> extends WeakReference<H> {
  private final int hash;
  private final Class<?> handlerTypeClass;

  WeakHandler(H handler, ReferenceQueue<? super H> q) {
    super(handler, q);
    hash = handler.hashCode();
    handlerTypeClass = handler.getLinkedClass();
  }

  public Class<?> getHandlerTypeClass() {
    return handlerTypeClass;
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof WeakHandler)) {
      return false;
    }

    Object t = this.get();
    Object u = ((WeakHandler<?>)obj).get();
    if (t == u) {
      return true;
    }
    if (t == null || u == null) {
      return false;
    }
    return t.equals(u);
  }
}
