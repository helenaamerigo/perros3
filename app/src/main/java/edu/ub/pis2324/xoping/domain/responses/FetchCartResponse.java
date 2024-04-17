package edu.ub.pis2324.xoping.domain.responses;

import java.util.List;

import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.domain.model.valueobjects.Price;
import edu.ub.pis2324.xoping.domain.model.valueobjects.PricedLineItem;

public class FetchCartResponse {
  private final List<PricedLineItem<Animal>> pricedLineItems;
  private final Price priceTotal;

  public FetchCartResponse(
      List<PricedLineItem<Animal>> pricedLineItems,
      Price priceTotal
  ) {
    this.pricedLineItems = pricedLineItems;
    this.priceTotal = priceTotal;
  }

  public List<PricedLineItem<Animal>> getPricedLineItems() {
    return pricedLineItems;
  }

  public Price getPriceTotal() {
    return priceTotal;
  }
}
