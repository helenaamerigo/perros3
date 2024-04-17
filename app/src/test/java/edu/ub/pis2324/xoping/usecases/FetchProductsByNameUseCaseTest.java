package edu.ub.pis2324.xoping.usecases;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import edu.ub.pis2324.xoping.domain.di.repositories.AnimalRepository;
import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;
import edu.ub.pis2324.xoping.domain.usecases.FetchProductsByNameUseCase;
import edu.ub.pis2324.xoping.domain.usecases.implementations.FetchProductsByNameUseCaseImpl;
import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.domain.model.valueobjects.Price;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class FetchProductsByNameUseCaseTest {

  private FetchProductsByNameUseCase fetchProductsByNameUseCase;
  private AnimalRepository productRepository;

  @Before
  public void setUp() {
    productRepository = Mockito.mock(AnimalRepository.class);
    fetchProductsByNameUseCase = new FetchProductsByNameUseCaseImpl(productRepository);
  }

  @Test
  public void givenProductsDoNotExistWithStartingWithName_whenFetchingProductsByName_thenProductsStartingByNameAreReturned() {
    // Given
    String name = "Apple";

    List<Animal> productsList = new ArrayList<>();
    productsList.add(new Animal(new AnimalId(UUID.randomUUID().toString()), "Apple iPhone 14", "apple iphone 14", "An older apple phone", new Price(1199.99, "€"), null));
    productsList.add(new Animal(new AnimalId(UUID.randomUUID().toString()), "Apple iPhone 15", "apple iphone 15", "A newer apple phone", new Price(1299.99, "€"), null));

    when(productRepository.getByName(name)).thenReturn(Observable.just(productsList));

    // When
    TestObserver<List<Animal>> testObserver = TestObserver.create();
    fetchProductsByNameUseCase.execute(name)
        .subscribe(testObserver);
    testObserver.awaitDone(5000, TimeUnit.MILLISECONDS);

    // Then
    testObserver.assertValue(productsList);
  }
}
