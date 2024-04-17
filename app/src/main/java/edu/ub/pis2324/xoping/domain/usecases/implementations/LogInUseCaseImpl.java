package edu.ub.pis2324.xoping.domain.usecases.implementations;

import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.usecases.FetchClientUseCase;
import edu.ub.pis2324.xoping.domain.model.entities.Client;
import edu.ub.pis2324.xoping.domain.usecases.LogInUseCase;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowableMapper;
import io.reactivex.rxjava3.core.Observable;

/**
 * Implementation of the log-in use case.
 */
public class LogInUseCaseImpl implements LogInUseCase {
  /* Attributes */
  private FetchClientUseCase fetchClientUseCase; // Re-usage of use cases!
  private final XopingThrowableMapper throwableMapper;

  /**
   * Constructor
   */
  public LogInUseCaseImpl(FetchClientUseCase fetchClientUseCase) {
    this.fetchClientUseCase = fetchClientUseCase;

    throwableMapper = new XopingThrowableMapper();
    throwableMapper.add(FetchClientUseCase.Error.CLIENT_NOT_FOUND, Error.CLIENT_NOT_FOUND);
    throwableMapper.add(FetchClientUseCase.Error.CLIENTS_DATA_ACCESS_ERROR, Error.CLIENTS_DATA_ACCESS_ERROR);
  }

  /**
   * Executes the log-in use case.
   * @param clientId
   * @param enteredPassword
   * @return
   */
  @Override
  public Observable<Client> execute(ClientId clientId, String enteredPassword) {
    return checkUsernameNotEmpty(clientId)
      .concatMap(ignored -> checkPasswordNotEmpty(enteredPassword))
      .concatMap(ignored -> fetchClientUseCase.execute(clientId))
      .concatMap(fetchedClient -> checkPasswordIsCorrect(fetchedClient, enteredPassword)
        .concatMap(ignored -> Observable.just(fetchedClient))
      )
      .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
  }

  private Observable<Boolean> checkUsernameNotEmpty(ClientId id) {
    if (!id.toString().isEmpty())
      return Observable.just(true);
    else
      return Observable.error(new XopingThrowable(Error.USERNAME_EMPTY));
  }

  private Observable<Boolean> checkPasswordNotEmpty(String enteredPassword) {
    if (!enteredPassword.isEmpty())
      return Observable.just(true);
    else
      return Observable.error(new XopingThrowable(Error.PASSWORD_EMPTY));
  }

  private Observable<Boolean> checkPasswordIsCorrect(Client client, String enteredPassword) {
    if (enteredPassword.equals(client.getPassword()))
      return Observable.just(true);
    else
      return Observable.error(new XopingThrowable(Error.PASSWORD_INCORRECT));
  }
}
