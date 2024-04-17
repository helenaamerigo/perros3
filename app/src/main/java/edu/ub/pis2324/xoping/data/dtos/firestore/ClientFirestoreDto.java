package edu.ub.pis2324.xoping.data.dtos.firestore;

import com.google.firebase.firestore.DocumentId;

import edu.ub.pis2324.xoping.domain.model.valueobjects.Cart;

/**
 * Domain entity holding the data and the behavior of a client.
 */
public class ClientFirestoreDto {
  /* Attributes */
  @DocumentId
  private String id;
  private String email;
  private String password;
  private String photoUrl;
  private CartFirestoreDto cart; // map of <product.id, quantity>

  /**
   * Empty constructor.
   */
  @SuppressWarnings("unused")
  public ClientFirestoreDto() { }

  /**
   * Gets the id of the client.
   * @return The id of the client.
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the username of the client.
   * @return The username of the client.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Gets the password of the client.
   * @return The password of the client.
   */
  public String getPassword() {
    return password;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public CartFirestoreDto getCart() {
    return cart;
  }
}
