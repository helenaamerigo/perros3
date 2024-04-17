package edu.ub.pis2324.xoping.presentation.pos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;

/**
 * Data class holding the information of a client
 * outside the application layer.
 */
public class ProductPO implements Parcelable {
  /* Attributes */
  private AnimalId id;
  private String name;
  private String description;
  private String price;
  private String imageUrl;

  public ProductPO(AnimalId id, String name, String description, String price, String imageUrl) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public ProductPO() {
  }

  public AnimalId getId() { return id; }
  public String getName() { return name; }
  public String getDescription() { return description; }
  public String getPrice() { return price; }
  public String getImageUrl() { return imageUrl; }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ProductPO that = (ProductPO) o;

    if (!Objects.equals(id, that.id)) return false;
    if (!Objects.equals(name, that.name)) return false;
    if (!Objects.equals(description, that.description)) return false;
    if (!Objects.equals(price, that.price)) return false;
    return Objects.equals(imageUrl, that.imageUrl);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeSerializable(this.id);
    dest.writeString(this.name);
    dest.writeString(this.description);
    dest.writeString(this.price);
    dest.writeString(this.imageUrl);
  }

  public void readFromParcel(Parcel source) {
    this.id = (AnimalId) source.readSerializable();
    this.name = source.readString();
    this.description = source.readString();
    this.price = source.readString();
    this.imageUrl = source.readString();
  }

  protected ProductPO(Parcel in) {
    this.id = (AnimalId) in.readSerializable();
    this.name = in.readString();
    this.description = in.readString();
    this.price = in.readString();
    this.imageUrl = in.readString();
  }

  public static final Parcelable.Creator<ProductPO> CREATOR = new Parcelable.Creator<ProductPO>() {
    @Override
    public ProductPO createFromParcel(Parcel source) {
      return new ProductPO(source);
    }

    @Override
    public ProductPO[] newArray(int size) {
      return new ProductPO[size];
    }
  };
}
