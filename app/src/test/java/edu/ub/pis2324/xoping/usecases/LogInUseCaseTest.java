package edu.ub.pis2324.xoping.usecases;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.usecases.FetchClientUseCase;
import edu.ub.pis2324.xoping.domain.usecases.LogInUseCase;
import edu.ub.pis2324.xoping.domain.usecases.implementations.LogInUseCaseImpl;
import edu.ub.pis2324.xoping.domain.model.entities.Client;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class LogInUseCaseTest {
  private LogInUseCase logInUseCase;
  private FetchClientUseCase fetchClientUseCase;

  @Before
  public void setUp() {
    fetchClientUseCase = Mockito.mock(FetchClientUseCase.class);
    logInUseCase = new LogInUseCaseImpl(fetchClientUseCase);
  }

  @Test
  public void givenValidLogInCredentials_whenLogIn_thenClientModelIsReturned() {
    // Given
    ClientId anyId = new ClientId("anyId");
    String anyEmail = "any@e.mail";
    String anyPassword = "anyPassword";
    Client mockClient = new Client(anyId, anyEmail, anyPassword, null);
    when(fetchClientUseCase.execute(mockClient.getId())).thenReturn(Observable.just(mockClient));

    // When
    TestObserver<Client> testObserver = TestObserver.create();
    logInUseCase.execute(mockClient.getId(), mockClient.getPassword())
        .subscribe(testObserver);
    testObserver.awaitDone(5000, TimeUnit.MILLISECONDS);

    // Then
    Client client = new Client(mockClient);
    testObserver.assertValue(client);
  }

  @Test
  public void givenLogInIdIsEmpty_whenLogIn_thenErrorIsThrown() {
    // Given
    ClientId emptyId = new ClientId("");
    String anyPassword = "anyPassword";

    // When
    TestObserver<Client> testObserver = TestObserver.create();
    logInUseCase.execute(emptyId, anyPassword)
        .subscribe(testObserver);
    testObserver.awaitDone(5000, TimeUnit.MILLISECONDS);

    // Then
    testObserver.assertFailure(Throwable.class);
    testObserver.assertError(throwable -> {
      return (throwable instanceof XopingThrowable)
          && ((XopingThrowable) throwable).getError() == LogInUseCase.Error.USERNAME_EMPTY;
    });
  }

  @Test
  public void givenLogInPasswordIsEmpty_whenLogIn_thenErrorIsThrown() {
    // Given
    ClientId validId = new ClientId("validId");
    String emptyPassword = "";

    // When
    TestObserver<Client> testObserver = TestObserver.create();
    logInUseCase.execute(validId, emptyPassword)
        .subscribe(testObserver);
    testObserver.awaitDone(5000, TimeUnit.MILLISECONDS);

    // Then
    testObserver.assertFailure(Throwable.class);
    testObserver.assertError(throwable -> {
      return (throwable instanceof XopingThrowable)
          && ((XopingThrowable) throwable).getError() == LogInUseCase.Error.PASSWORD_EMPTY;
    });
  }

  @Test
  public void givenLogInPasswordIsIncorrect_whenLogIn_thenErrorIsThrown() {
    // Given
    ClientId validId = new ClientId("validId");
    String anyEmail = "any@e.mail";
    String correctPassword = "correctPassword";
    Client mockClient = new Client(validId, anyEmail, correctPassword, null);
    when(fetchClientUseCase.execute(mockClient.getId())).thenReturn(Observable.just(mockClient));

    String incorrectPassword = "incorrectPassword";

    // When
    TestObserver<Client> testObserver = TestObserver.create();
    logInUseCase.execute(mockClient.getId(), incorrectPassword)
      .subscribe(testObserver);
    testObserver.awaitDone(5000, TimeUnit.MILLISECONDS);

    // Then
    testObserver.assertFailure(Throwable.class);
    testObserver.assertError(throwable -> {
      return (throwable instanceof XopingThrowable)
          && ((XopingThrowable) throwable).getError() == LogInUseCase.Error.PASSWORD_INCORRECT;
    });
  }
}
