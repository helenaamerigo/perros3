package edu.ub.pis2324.xoping.domain.di.repositories;

import edu.ub.pis2324.xoping.domain.model.entities.Client;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import io.reactivex.rxjava3.core.Observable;

public interface ClientRepository {
  interface OnUpdateListener {
    void update(Client client);
  }

  Observable<Boolean> add(Client client);
  Observable<Client> getById(ClientId id);
  Observable<Boolean> update(ClientId id, OnUpdateListener onUpdateListener);
  Observable<Boolean> remove(ClientId id);

  enum Error implements XopingError {
    CLIENT_NOT_FOUND, // Produït per getById, update o remove
    ADD_UNKNOWN_ERROR, // Produït per add
    GETBYID_UNKNOWN_ERROR, // Produït per getById
    UPDATE_UNKNOWN_ERROR, // Produït per update
    REMOVE_UNKNOWN_ERROR; // Produït per getById
  }
}
