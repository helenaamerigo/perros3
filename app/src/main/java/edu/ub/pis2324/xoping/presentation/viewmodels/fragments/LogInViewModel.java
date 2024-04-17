package edu.ub.pis2324.xoping.presentation.viewmodels.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.xoping.domain.model.entities.Client;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.presentation.pos.ClientPO;
import edu.ub.pis2324.xoping.domain.usecases.LogInUseCase;
import edu.ub.pis2324.xoping.presentation.pos.mappers.DomainToPOMapper;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import edu.ub.pis2324.xoping.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LogInViewModel extends ViewModel {
  /* Attributes */
  private final LogInUseCase logInUseCase;
  /* LiveData */
  private final StateLiveData<ClientPO> logInState;
  /* RxJava */
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  private final DomainToPOMapper domainToPOMapper;

  /* Constructor */
  public LogInViewModel(LogInUseCase logInUseCase) {
    this.logInUseCase = logInUseCase;
    logInState = new StateLiveData<>();
    domainToPOMapper = new DomainToPOMapper();
  }

  /**
   * Parent class (ViewModel's) lifecycle-related method
   * that is invoked when the activity is purposedly destroyed.
   */
  @Override
  public void onCleared() {
    compositeDisposable.dispose();
  }

  /**
   * Returns the state of the log-in
   * @return the state of the log-in
   */
  public StateLiveData<ClientPO> getLogInState() {
    return logInState;
  }

  /**
   * Logs in the user
   * @param username the username
   * @param password the password
   */
  public void login(String username, String password) {
    logInState.postLoading();

    ClientId clientId = new ClientId(username);

    /* Invoca cas d'ús */
    Disposable d = logInUseCase.execute(clientId, password)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        client -> handleLogInSuccess(client),  // Si no hi ha error, s'executa aquesta funció
        throwable -> handleLogInError(throwable) // Sinó hi ha error, s'executa aquesta
      );

    compositeDisposable.add(d);
  }

  private void handleLogInSuccess(Client client) {
    ClientPO clientPO = domainToPOMapper.map(client, ClientPO.class);
    logInState.postSuccess(clientPO);
  }

  private void handleLogInError(Throwable throwable) {
    String message;
    XopingError xError = ((XopingThrowable) throwable).getError();
    if (xError == LogInUseCase.Error.USERNAME_EMPTY)
      message = "Username is empty";
    else if (xError == LogInUseCase.Error.PASSWORD_EMPTY)
      message = "Password is empty";
    else if (xError == LogInUseCase.Error.CLIENT_NOT_FOUND)
      message = "Client not found";
    else if (xError == LogInUseCase.Error.PASSWORD_INCORRECT)
      message = "Password is incorrect";
    else if (xError == LogInUseCase.Error.CLIENTS_DATA_ACCESS_ERROR)
      message = "Clients' data access error";
    else
      message = "Unknown error";

    logInState.postError(new Throwable(message));
  }

  /**
   * Factory for the ViewModel to be able to pass parameters to the constructor
   */
  public static class Factory extends ViewModelProvider.NewInstanceFactory {
    private final LogInUseCase logInUseCase;

    public Factory(LogInUseCase logInUseCase) {
      this.logInUseCase = logInUseCase;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      return (T) new LogInViewModel(logInUseCase);
    }
  }
}
