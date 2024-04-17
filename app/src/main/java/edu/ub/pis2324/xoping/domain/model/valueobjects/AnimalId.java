package edu.ub.pis2324.xoping.domain.model.valueobjects;

import java.io.Serializable;
import java.util.Objects;

public class AnimalId implements Serializable {
  private String id;

  public AnimalId(String id) {
    this.id = id;
  }

  @SuppressWarnings("unused")
  public AnimalId() {}

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    AnimalId animalId = (AnimalId) obj;
    return Objects.equals(id, animalId.id); // Use Objects.equals for null safety
  }

  @Override
  public int hashCode() {
    return Objects.hash(id); // Use Objects.hash to generate hash code based on id
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
