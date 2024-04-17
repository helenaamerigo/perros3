package edu.ub.pis2324.xoping.domain.services.implementations;


import java.util.NoSuchElementException;

import edu.ub.pis2324.xoping.domain.di.repositories.ClientRepository;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.services.CheckClientExistsService;
import edu.ub.pis2324.xoping.domain.usecases.AddProductToCartUseCase;
import edu.ub.pis2324.xoping.domain.usecases.RemoveFromCartUseCase;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowableMapper;
import io.reactivex.rxjava3.core.Observable;

public class CheckClientExistsServiceImpl implements CheckClientExistsService {
  /* Attributes */
  private ClientRepository clientRepository;
  private final XopingThrowableMapper throwableMapper;

  /**
   * Constructor
   */
  public CheckClientExistsServiceImpl(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;

    throwableMapper = new XopingThrowableMapper();
    throwableMapper.add(ClientRepository.Error.GETBYID_UNKNOWN_ERROR, Error.CLIENTS_DATA_ACCESS_ERROR);
  }

  /**
   * Executes the check client exists use case.
   * @param clientId
   * @return
   */
  @Override
  public Observable<Boolean> execute(ClientId clientId) {
    return clientRepository.getById(clientId)
      .concatMap(ignored -> {
        // El client ja existeix
        return Observable.just(true);
      })
      .onErrorResumeNext(throwable -> {
        if (throwable instanceof XopingThrowable) {
          XopingError xError = ((XopingThrowable) throwable).getError();
          if (xError == ClientRepository.Error.CLIENT_NOT_FOUND)
            return Observable.just(false);
          else
            return Observable.error(throwableMapper.map(throwable));
        } else {
          return Observable.error(throwable); // Error no controlat
        }
      });
  }
}
