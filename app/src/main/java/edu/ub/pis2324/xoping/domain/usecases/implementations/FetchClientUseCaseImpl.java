package edu.ub.pis2324.xoping.domain.usecases.implementations;

import edu.ub.pis2324.xoping.domain.di.repositories.ClientRepository;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.usecases.FetchClientUseCase;
import edu.ub.pis2324.xoping.domain.model.entities.Client;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowableMapper;
import io.reactivex.rxjava3.core.Observable;

public class FetchClientUseCaseImpl implements FetchClientUseCase {
  /* Attributes */
  private ClientRepository clientRepository;
  private XopingThrowableMapper throwableMapper;

  /**
   * Constructor
   */
  public FetchClientUseCaseImpl(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;

    throwableMapper = new XopingThrowableMapper();
    throwableMapper.add(ClientRepository.Error.CLIENT_NOT_FOUND, Error.CLIENT_NOT_FOUND);
    throwableMapper.add(ClientRepository.Error.GETBYID_UNKNOWN_ERROR, Error.CLIENTS_DATA_ACCESS_ERROR);
  }

  /**
   * Executes the fetch client use case.
   * @param clientId
   * @return
   */
  @Override
  public Observable<Client> execute(ClientId clientId) {
    return clientRepository.getById(clientId)
      .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
  }
}
