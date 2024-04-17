package edu.ub.pis2324.xoping.domain.model.entities;

import java.util.Map;

import edu.ub.pis2324.xoping.domain.model.valueobjects.Cart;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.model.valueobjects.LineItem;
import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;

/**
 * Domain entity holding the data and the behavior of a client.
 */
public class Client {
  /* Attributes */
  private ClientId id;
  private String email;
  private String password;
  private String photoUrl;
  private Cart cart; // map of <product.id, quantity>

  /**
   * Constructor.
   * @param id The id of the client.
   * @param email The email of the client.
   * @param password The password of the client.
   * @param photoUrl The photo URL of the client.
   */
  public Client(ClientId id, String email, String password, String photoUrl) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.photoUrl = photoUrl;
    this.cart = new Cart();
  }

  /**
   * Empty constructor.
   */
  @SuppressWarnings("unused")
  public Client(Client client) {
    this.id = client.id;
    this.email = client.email;
    this.password = client.password;
    this.photoUrl = client.photoUrl;
    this.cart = client.cart;
  }

  /**
   * Empty constructor.
   */
  @SuppressWarnings("unused")
  public Client() {
    this(null, null, null, null);
  }

  /**
   * Gets the id of the client.
   * @return The id of the client.
   */
  public ClientId getId() {
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

  public Cart getCart() {
    return cart;
  }

  /**
   * Adds a purchase to the client.
   * @param productId The id of the product to be purchased.
   */
  public void addProductToCart(AnimalId productId, Integer addedQuantity) {
    cart.add(productId, addedQuantity);
  }

  /**
   * Removes a product from the client's cart.
   * @param productId The id of the product to be removed.
   * @return True if the product was removed, false otherwise.
   */
  public void removeProductFromCart(AnimalId productId) {
    cart.remove(productId);
  }

  /**
   * Gets the products in the client's cart.
   * @return The products in the client's cart.
   */
  public Map<AnimalId, LineItem<AnimalId>> getCartLines() {
    return cart.getCartLines();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Client client = (Client) obj;
    return id.equals(client.id);
  }
}
