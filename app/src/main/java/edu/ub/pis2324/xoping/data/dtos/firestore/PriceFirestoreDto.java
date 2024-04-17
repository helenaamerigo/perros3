package edu.ub.pis2324.xoping.data.dtos.firestore;

/**
 * DTO for a price.
 */
public class PriceFirestoreDto {
  /* Attributes */
  private Double amount;
  private String currency;

//  /**
//   * Constructor.
//   * @param amount The price's amount.
//   * @param currency The price's currency.
//   */
//  public PriceDto(Double amount, String currency) {
//    this.amount = amount;
//    this.currency = currency;
//  }

  /**
   * Empty constructor required for Firestore.
   */
  @SuppressWarnings("unused")
  public PriceFirestoreDto() { }

  /* Getters */
  public Double getAmount() { return amount; }
  public String getCurrency() { return currency; }
//
//  @Override
//  public String toString() {
//    return String.format("%.2f %s", amount, currency);
//  }
}
