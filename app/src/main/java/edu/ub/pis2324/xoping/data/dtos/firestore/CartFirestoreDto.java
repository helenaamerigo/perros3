package edu.ub.pis2324.xoping.data.dtos.firestore;

import java.util.Map;

import edu.ub.pis2324.xoping.domain.model.valueobjects.LineItem;
import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;

/**
 * Domain entity holding the data and the behavior of a client.
 */
public class CartFirestoreDto {
  /* Attributes */
  private Map<String, LineItem<AnimalId>> cartLines; // <productId, quantity>

  /**
   * Empty constructor.
   */
  @SuppressWarnings("unused")
  public CartFirestoreDto() { }

  public Map<String, LineItem<AnimalId>> getCartLines() {
    return cartLines;
  }
}
