package ru.itmo.prog.lab4.models.common;

import ru.itmo.prog.lab4.models.people.Person;

public enum Action {
  LOOKED_INSIDE("заглянул внутрь", "заглянула внутрь"),
  REACHED("достиг", "достигла"),
  WAS("был", "была"),
  CLIMBED_UP("вскарабкался по", "вскарабкалась по"),
  LOOK_AROUND("оглядеться по сторонам"),
  KNEW("он знал", "она знала"),
  CAN("может"),
  TRIED("попробовал"),
  DISCERN("разглядеть"),
  LOOKED_AROUND("огляделся по сторонам", "огляделась по сторонам"),
  PLUMMET("стремительно падать"),
  SUDDENLY_STARTED("вдруг начал", "вдруг начала"),
  STRICTLY_SAY("строго сказал", "строго сказала"),
  GO_TO_EXIT("бросились к выходу");

  private final String maleText;
  private final String femaleText;

  Action(String text) {
    this.maleText = text;
    this.femaleText = text;
  }

  Action(String maleText, String femaleText) {
    this.maleText = maleText;
    this.femaleText = femaleText;
  }

  public String getDefault() {
    return maleText;
  }

  public String getDescription(Person person) {
    return person.isMale() ? maleText : femaleText;
  }
}
