package edu.ub.pis2324.xoping.domain.usecases;

import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import io.reactivex.rxjava3.core.Observable;

public interface SignUpUseCase {
  Observable<Boolean> execute(
      ClientId clientId,
      String email,
      String password,
      String passwordConfirmation);

  enum Error implements XopingError {
    USERNAME_EMPTY,
    EMAIL_EMPTY,
    PASSWORD_EMPTY,
    PASSWORD_CONFIRMATION_EMPTY,
    PASSWORD_AND_CONFIRMATION_MISMATCH,
    CLIENT_ALREADY_EXISTS,
    CLIENTS_DATA_ACCESS_ERROR;
  }
}
