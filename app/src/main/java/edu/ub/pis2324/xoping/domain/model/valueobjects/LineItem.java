package edu.ub.pis2324.xoping.domain.model.valueobjects;

public class LineItem<T> {
  public final T item;
  public final Integer quantity;

  public LineItem(T item, Integer quantity) {
    this.item = item;
    this.quantity = quantity;
  }

  @SuppressWarnings("unused")
  public LineItem() {
    this(null, null);
  }

  public T getItem() {
    return item;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public LineItem<T> addQuantity(Integer quantity) {
    return new LineItem<>(item, this.getQuantity() + quantity);
  }
}
