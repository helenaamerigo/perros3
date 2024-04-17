package edu.ub.pis2324.xoping.domain.usecases;

import edu.ub.pis2324.xoping.domain.model.entities.Client;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import io.reactivex.rxjava3.core.Observable;

public interface FetchClientUseCase {
  Observable<Client> execute(ClientId clientId);

  enum Error implements XopingError {
    CLIENT_NOT_FOUND,
    CLIENTS_DATA_ACCESS_ERROR;
  }
}
