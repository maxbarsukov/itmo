package ru.itmo.prog.lab4.models.events;

import ru.itmo.prog.lab4.interfaces.events.Event;
import ru.itmo.prog.lab4.lib.events.EventException;
import ru.itmo.prog.lab4.lib.events.EventHandler;

public class NoOneExpectedEvent implements Event {
  public static class Handler extends EventHandler<NoOneExpectedEvent> {
    @Override
    public void handle(NoOneExpectedEvent event) throws EventException {
      throw new EventException("то, чего никто не ожидал");
      // return GOOD_END; // нет, никогда
    }
  }

  public static final String ACTION = "произошло";
  public static final String GOOD_END = "то, что все и ожидали, ну и слава богу";

  /**
   * Всегда неожиданно и неотвратимо
   * @return String
   */
  public String execute() {
    return "в следующий момент произошло ";
  }
}
