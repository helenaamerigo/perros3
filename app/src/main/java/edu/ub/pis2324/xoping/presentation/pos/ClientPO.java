package edu.ub.pis2324.xoping.presentation.pos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;

/**
 * Data class holding the information of a client
 * outside the application layer.
 */
public class ClientPO implements Parcelable {
  /* Attributes */
  private ClientId id;
  private String email;
  private String photoUrl;

  /* Constructors */
  public ClientPO(ClientId id, String email, String photoUrl) {
    this.id = id;
    this.email = email;
    this.photoUrl = photoUrl;
  }

  @SuppressWarnings("unused")
  public ClientPO() {
  }

  /* Setters */
  public ClientId getId() { return id; }
  public String getEmail() { return email; }
  public String getPhotoUrl() { return photoUrl; }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeSerializable(this.id);
    dest.writeString(this.email);
    dest.writeString(this.photoUrl);
  }

  public void readFromParcel(Parcel source) {
    this.id = (ClientId) source.readSerializable();
    this.email = source.readString();
    this.photoUrl = source.readString();
  }

  protected ClientPO(Parcel in) {
    this.id = (ClientId) in.readSerializable();
    this.email = in.readString();
    this.photoUrl = in.readString();
  }

  public static final Parcelable.Creator<ClientPO> CREATOR = new Parcelable.Creator<ClientPO>() {
    @Override
    public ClientPO createFromParcel(Parcel source) {
      return new ClientPO(source);
    }

    @Override
    public ClientPO[] newArray(int size) {
      return new ClientPO[size];
    }
  };
}
