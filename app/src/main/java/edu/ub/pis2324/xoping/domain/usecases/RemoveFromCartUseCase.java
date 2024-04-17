package edu.ub.pis2324.xoping.domain.usecases;

import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;
import edu.ub.pis2324.xoping.domain.responses.FetchCartResponse;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import io.reactivex.rxjava3.core.Observable;

public interface RemoveFromCartUseCase {
  Observable<FetchCartResponse> execute(ClientId clientId, AnimalId productId);

  enum Error implements XopingError {
    CLIENT_NOT_FOUND,
    CLIENTS_DATA_ACCESS_ERROR,
    PRODUCTS_DATA_ACCESS_ERROR;
  }
}
