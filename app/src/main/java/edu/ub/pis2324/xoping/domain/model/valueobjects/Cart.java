package edu.ub.pis2324.xoping.domain.model.valueobjects;

import java.util.HashMap;
import java.util.Map;

public class Cart {
  private Map<AnimalId, LineItem<AnimalId>> cartLines; // <Product id, quantity>

  public Cart() {
    this.cartLines = new HashMap<>();
  }

  public Map<AnimalId, LineItem<AnimalId>> getCartLines() {
    return new HashMap<>(cartLines);
  }

  public void add(AnimalId productId, Integer quantity) {
    if (cartLines.containsKey(productId)) {
      cartLines.put(productId, cartLines.get(productId).addQuantity(quantity));
    } else {
      cartLines.put(productId, new LineItem<>(productId, quantity));
    }
  }

  public void remove(AnimalId productId) {
    if (cartLines.containsKey(productId)) {
      cartLines.remove(productId);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Cart cart = (Cart) obj;
    return cartLines.equals(cart.cartLines);
  }
}
