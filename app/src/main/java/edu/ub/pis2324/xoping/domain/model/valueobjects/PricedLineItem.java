package edu.ub.pis2324.xoping.domain.model.valueobjects;

public class PricedLineItem<T> extends LineItem<T> {
  public final Price subtotalPrice;

  public PricedLineItem(T item, Integer quantity, Price subtotalPrice) {
    super(item, quantity);
    this.subtotalPrice = subtotalPrice;
  }

  @SuppressWarnings("unused")
  public PricedLineItem() {
    this(null, null, null);
  }

  public Price getSubtotalPrice() {
    return subtotalPrice;
  }
}
