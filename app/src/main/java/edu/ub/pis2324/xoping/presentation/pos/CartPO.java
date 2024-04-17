package edu.ub.pis2324.xoping.presentation.pos;


import java.util.List;

import edu.ub.pis2324.xoping.domain.model.valueobjects.Price;
import edu.ub.pis2324.xoping.domain.model.valueobjects.PricedLineItem;

public class CartPO {
  private final List<PricedLineItem<ProductPO>> pricedCartLines;
  private final Price priceTotal;

  public CartPO(List<PricedLineItem<ProductPO>> pricedCartLines, Price priceTotal) {
    this.pricedCartLines = pricedCartLines;
    this.priceTotal = priceTotal;
  }

  public List<PricedLineItem<ProductPO>> getCartLineModels() {
    return pricedCartLines;
  }

  public Price getPriceTotal() {
    return priceTotal;
  }
}
