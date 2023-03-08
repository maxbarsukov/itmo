package common.utility;

import java.io.Serializable;

public abstract class Element implements Comparable<Element>, Validatable, Serializable {
  abstract public int getId();
}
