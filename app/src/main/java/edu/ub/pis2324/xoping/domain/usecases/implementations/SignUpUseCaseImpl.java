package edu.ub.pis2324.xoping.domain.usecases.implementations;

import edu.ub.pis2324.xoping.domain.di.repositories.ClientRepository;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.services.CheckClientExistsService;
import edu.ub.pis2324.xoping.domain.usecases.SignUpUseCase;
import edu.ub.pis2324.xoping.domain.model.entities.Client;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowableMapper;
import io.reactivex.rxjava3.core.Observable;

/**
 * Implementation of the log-in use case (EXERCICI 3)
 */
public class SignUpUseCaseImpl implements SignUpUseCase {
  /* Attributes */
  private CheckClientExistsService checkClientExistsService; // Re-usage of use cases
  private ClientRepository clientRepository;
  private XopingThrowableMapper throwableMapper;

  /**
   * Creates a log-in use case.
   */
  public SignUpUseCaseImpl(
      CheckClientExistsService checkClientExistsService,
      ClientRepository clientRepository
  ) {
    this.checkClientExistsService = checkClientExistsService;
    this.clientRepository = clientRepository;

    throwableMapper = new XopingThrowableMapper();
    throwableMapper.add(ClientRepository.Error.ADD_UNKNOWN_ERROR, Error.CLIENTS_DATA_ACCESS_ERROR);
  }

  /**
   * Executes the log-in use case.
   * @return
   */
  @Override
  public Observable<Boolean> execute(
      ClientId clientId,
      String email,
      String password,
      String passwordConfirmation)
  {
    return checkUsernameNotEmpty(clientId)
        .concatMap(ignored -> checkEmailNotEmpty(email))
        .concatMap(ignored -> checkPasswordNotEmpty(password))
        .concatMap(ignored -> checkPasswordConfirmationNotEmpty(passwordConfirmation))
        .concatMap(ignored -> checkPasswordAndConfirmationMatch(password, passwordConfirmation))
        .concatMap(ignored -> checkClientExistsService.execute(clientId))
        .concatMap(clientExists -> {
          if (clientExists) {
            return Observable.error(new XopingThrowable(Error.CLIENT_ALREADY_EXISTS));
          } else {
            Client client = new Client(clientId, email, password, null);
            return clientRepository.add(client);
          }
        })
        .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
  }

  private Observable<Boolean> checkUsernameNotEmpty(ClientId clientId) {
    return clientId.toString().isEmpty()
        ? Observable.error(new XopingThrowable(Error.USERNAME_EMPTY))
        : Observable.just(true);
  }

  private Observable<Boolean> checkEmailNotEmpty(String email) {
    return email.isEmpty()
        ? Observable.error(new XopingThrowable(Error.EMAIL_EMPTY))
        : Observable.just(true);
  }

  private Observable<Boolean> checkPasswordNotEmpty(String password) {
    return password.isEmpty()
        ? Observable.error(new XopingThrowable(Error.PASSWORD_EMPTY))
        : Observable.just(true);
  }

  private Observable<Boolean> checkPasswordConfirmationNotEmpty(String passwordConfirmation) {
    return passwordConfirmation.isEmpty()
        ? Observable.error(new XopingThrowable(Error.PASSWORD_CONFIRMATION_EMPTY))
        : Observable.just(true);
  }

  private Observable<Boolean> checkPasswordAndConfirmationMatch(
      String password,
      String passwordConfirmation
  ) {
    return !password.equals(passwordConfirmation)
        ? Observable.error(new XopingThrowable(Error.PASSWORD_AND_CONFIRMATION_MISMATCH))
        : Observable.just(true);
  }
}
