package edu.ub.pis2324.xoping.data.repositories.firestore;

import edu.ub.pis2324.xoping.domain.di.repositories.ClientRepository;
import edu.ub.pis2324.xoping.domain.di.repositories.AnimalRepository;
import edu.ub.pis2324.xoping.domain.di.repositories.AbstractRepositoryFactory;

public class FirestoreRepositoryFactory implements AbstractRepositoryFactory {
  @Override
  public ClientRepository createClientRepository() {
    return new ClientFirestoreRepository();
  }

  @Override
  public AnimalRepository createProductRepository() {
    return new AnimalFirestoreRepository();
  }
}
