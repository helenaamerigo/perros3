package edu.ub.pis2324.xoping.presentation.pos;


import java.util.List;

import edu.ub.pis2324.xoping.domain.model.valueobjects.Price;
import edu.ub.pis2324.xoping.domain.model.valueobjects.PricedLineItem;

public class CartPO {
  private final List<PricedLineItem<AnimalPO>> pricedCartLines;
  private final Price priceTotal;

  public CartPO(List<PricedLineItem<AnimalPO>> pricedCartLines, Price priceTotal) {
    this.pricedCartLines = pricedCartLines;
    this.priceTotal = priceTotal;
  }

  public List<PricedLineItem<AnimalPO>> getCartLineModels() {
    return pricedCartLines;
  }

  public Price getPriceTotal() {
    return priceTotal;
  }
}
