package edu.ub.pis2324.xoping.data.dtos.firestore;

import com.google.firebase.firestore.DocumentId;

import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;

/**
 * DTO for a product.
 */
public class AnimalFirestoreDto {
  /* Attributes */
  private AnimalId id;
  private String nom;
  private String edat;
  private String descripcio;
  private String pelatge;
  private String imageUrl;
  private float mida;
  private float pes;
  private String raza;
  private boolean gos;

  /**
   * Empty constructor required for Firestore.
   */
  @SuppressWarnings("unused")
  public AnimalFirestoreDto() { }


  /* Getters */

  public AnimalId getId() { return id; }
  public String getNom() { return nom; }
  public String getEdat() { return edat; }
  public String getDescripcio() { return descripcio; }
  public String getImageUrl() { return imageUrl; }
  public String getPelatge() { return pelatge; }
  public float getMida() { return mida; }
  public float getPes() { return pes; }
  public String getRaza() { return raza; }
  public boolean isGos() { return gos; }


}
