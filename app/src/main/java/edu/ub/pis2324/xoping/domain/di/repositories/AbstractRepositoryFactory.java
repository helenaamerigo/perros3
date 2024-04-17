package edu.ub.pis2324.xoping.domain.di.repositories;

public interface AbstractRepositoryFactory {
  ClientRepository createClientRepository();
  AnimalRepository createProductRepository();
}
