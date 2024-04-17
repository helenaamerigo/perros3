package edu.ub.pis2324.xoping.presentation.viewmodels.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.usecases.SignUpUseCase;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import edu.ub.pis2324.xoping.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpViewModel extends ViewModel {
  /* Attributes */
  private final SignUpUseCase signUpUseCase;
  /* LiveData */
  private final StateLiveData<Void> signUpState;
  /* RxJava */
  CompositeDisposable compositeDisposable;

  /* Constructor */
  public SignUpViewModel(SignUpUseCase signUpUseCase) {
    super();
    this.signUpUseCase = signUpUseCase;
    this.signUpState = new StateLiveData<>();
    compositeDisposable = new CompositeDisposable();
  }

  /**
   * Parent class (ViewModel's) lifecycle-related method
   * that is invoked when the activity is purposedly destroyed.
   */
  @Override
  protected void onCleared() {
    super.onCleared();
    compositeDisposable.dispose();
  }

  /**
   * Returns the state of the sign-up
   * @return the state of the sign-up
   */
  public StateLiveData<Void> getSignUpState() {
    return signUpState;
  }

  /**
   * Signs up the user
   * @param username the username
   * @param password the password
   * @param passwordConfirmation the password confirmation
   */
  public void signUp(String username, String email, String password, String passwordConfirmation) {
    ClientId clientId = new ClientId(username);
    Disposable d = signUpUseCase.execute(clientId, email, password, passwordConfirmation)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        ignored -> signUpState.postComplete(),
        throwable -> handleSignUpError(throwable)
      );

    compositeDisposable.add(d);
  }

  private void handleSignUpError(Throwable throwable) {
    if (throwable instanceof XopingThrowable)
      handleXopingError((XopingThrowable) throwable);
    else
      signUpState.postError(new Throwable("Unknown error"));
  }

  private void handleXopingError(XopingThrowable xopingThrowable) {
    String message;
    XopingError xError = xopingThrowable.getError();
    if (xError == SignUpUseCase.Error.USERNAME_EMPTY)
      message = "Username is empty";
    else if (xError == SignUpUseCase.Error.EMAIL_EMPTY)
      message = "Email is empty";
    else if (xError == SignUpUseCase.Error.PASSWORD_EMPTY)
      message = "Password is empty";
    else if (xError == SignUpUseCase.Error.PASSWORD_CONFIRMATION_EMPTY)
      message = "Password confirmation is empty";
    else if (xError == SignUpUseCase.Error.PASSWORD_AND_CONFIRMATION_MISMATCH)
      message = "Password and confirmation do not match";
    else if (xError == SignUpUseCase.Error.CLIENT_ALREADY_EXISTS)
      message = "Client already exists";
    else if (xError == SignUpUseCase.Error.CLIENTS_DATA_ACCESS_ERROR)
      message = "Clients' data access error";
    else
      message = "Unknown error";

    signUpState.postError(new Throwable(message));
  }

  /**
   * Factory for the ViewModel to be able to pass parameters to the constructor
   */
  public static class Factory extends ViewModelProvider.NewInstanceFactory {
    private final SignUpUseCase signUpUseCase;

    public Factory(SignUpUseCase signUpUseCase) {
      this.signUpUseCase = signUpUseCase;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      return (T) new SignUpViewModel(signUpUseCase);
    }
  }
}
