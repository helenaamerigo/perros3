package edu.ub.pis2324.xoping.usecases;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import edu.ub.pis2324.xoping.domain.di.repositories.ClientRepository;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.usecases.FetchClientUseCase;
import edu.ub.pis2324.xoping.domain.usecases.implementations.FetchClientUseCaseImpl;
import edu.ub.pis2324.xoping.domain.model.entities.Client;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class FetchClientUseCaseTest {

  private FetchClientUseCase fetchClientUseCase;
  private ClientRepository clientRepository;

  @Before
  public void setUp() {
    clientRepository = Mockito.mock(ClientRepository.class);
    fetchClientUseCase = new FetchClientUseCaseImpl(clientRepository);
  }

  @Test
  public void givenValidClientId_whenFetchingClient_thenClientIsReturned() {
    // Given
    ClientId clientId = new ClientId("admin");
    Client sampleClient = new Client(clientId, "admin@xoping.com", "admin", "null");
    when(clientRepository.getById(any())).thenReturn(Observable.just(sampleClient));

    // When
    TestObserver<Client> testObserver = TestObserver.create();
    fetchClientUseCase.execute(clientId)
        .subscribe(testObserver);
    testObserver.awaitDone(5000, TimeUnit.MILLISECONDS);

    // Then
    testObserver.assertValue(sampleClient);
  }

  @Test
  public void givenInvalidClientId_whenFetchingClient_thenErrorIsReturned() {
    // Given
    ClientId clientId = new ClientId("fasdf23;fSZZZ!2312312312jk");
    when(clientRepository.getById(any())).thenReturn(Observable.error(new NoSuchElementException("Client does not exist")));

    // When
    TestObserver<Client> testObserver = TestObserver.create();
    fetchClientUseCase.execute(clientId)
        .subscribe(testObserver);

    // Then
    testObserver.assertFailure(Throwable.class);
    testObserver.assertError(throwable -> "Client does not exist".equals(throwable.getMessage()));
  }
}
