package edu.ub.pis2324.xoping.usecases;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.ub.pis2324.xoping.domain.di.repositories.AnimalRepository;
import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;
import edu.ub.pis2324.xoping.domain.usecases.FetchProductsByNameUseCase;
import edu.ub.pis2324.xoping.domain.usecases.implementations.FetchProductsByNameUseCaseImpl;
import edu.ub.pis2324.xoping.data.repositories.firestore.AnimalFirestoreRepository;
import io.reactivex.rxjava3.observers.TestObserver;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class FetchAnimalsByNameUseCaseTest {

  private FetchProductsByNameUseCase fetchAnimalsByNameUseCase;
  private AnimalRepository productRepository;

  @Before
  public void setUp() {
    productRepository = new AnimalFirestoreRepository();
    fetchAnimalsByNameUseCase = new FetchProductsByNameUseCaseImpl(productRepository);
  }

  @Test
  public void givenAnimalsExistWithName_whenFetchingProductsByName_thenAnimalModelsStartingByNameAreReturned() {
    // Given
    String name = "Byson";

    // When
    TestObserver<List<Animal>> testObserver = TestObserver.create();
    fetchAnimalsByNameUseCase.execute(name)
        .subscribe(testObserver);

    // Then
    List<Animal> productsList = new ArrayList<>();
    productsList.add(new Animal(
        new AnimalId( "05fadf8c-4802-4620-9540-f7e33d5dfdc1"),
        "Byson Vacuum Cleaner",
        "byson vacuum cleaner",
        "Introducing the Byson Vacuum Cleaner, a powerhouse for your cleaning needs. With advanced suction technology, it effortlessly tackles dust and debris, leaving your space immaculately clean. Make cleaning a breeze with the reliable performance of Byson.",
        "https://i.ibb.co/qJPLqfj/P002.jpg"
    ));

    testObserver.awaitDone(5000, TimeUnit.MILLISECONDS);

    testObserver.assertValue(productModelsListObserved -> {
      return productModelsListObserved.equals(productsList);
    });
  }

  @Test
  public void givenAnimalsDoNotExistWithStartingWithName_whenFetchingAnimalsByName_thenEmptyListOfAnimalModelsAreReturned() {
    // Given
    String name = "UnexistingNamePrefix";

    // When
    TestObserver<List<Animal>> testObserver = TestObserver.create();
    fetchAnimalsByNameUseCase.execute(name)
        .subscribe(testObserver);
    testObserver.awaitDone(5000, TimeUnit.MILLISECONDS);

    // Then
    List<Animal> emptyAnimalsList = new ArrayList<>();

    testObserver.assertValue(productModelsListObserved -> {
      return productModelsListObserved.equals(emptyAnimalsList);
    });
  }
}
