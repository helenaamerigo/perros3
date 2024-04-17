package edu.ub.pis2324.xoping.domain.model.valueobjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * DTO for a price.
 */
public class Price implements Parcelable {
  private static final String CURRENCY = "€";

  /* Attributes */
  private Double amount;
  private String currency;

  /**
   * Constructor.
   * @param amount The price's amount.
   * @param currency The price's currency.
   */
  public Price(Double amount, String currency) {
    this.amount = amount;
    this.currency = currency;
  }

  public Price(Double amount) {
    this.amount = amount;
    this.currency = CURRENCY;
  }

  /**
   * Empty constructor required for Firestore.
   */
  @SuppressWarnings("unused")
  public Price() { }

  /* Getters */
  public Double getAmount() { return amount; }
  public String getCurrency() { return currency; }

  public Price add(Price price) {
    return new Price(amount + price.amount, currency);
  }
  public Price multiply(int quantity) {
    return new Price(amount * quantity, currency);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Price price = (Price) obj;
    return amount.equals(price.amount)
        && currency.equals(price.currency);
  }

  @Override
  public String toString() {
    return String.format("%.2f %s", amount, currency);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.amount);
    dest.writeString(this.currency);
  }

  public void readFromParcel(Parcel source) {
    this.amount = (Double) source.readValue(Double.class.getClassLoader());
    this.currency = source.readString();
  }

  protected Price(Parcel in) {
    this.amount = (Double) in.readValue(Double.class.getClassLoader());
    this.currency = in.readString();
  }

  public static final Parcelable.Creator<Price> CREATOR = new Parcelable.Creator<Price>() {
    @Override
    public Price createFromParcel(Parcel source) {
      return new Price(source);
    }

    @Override
    public Price[] newArray(int size) {
      return new Price[size];
    }
  };
}
