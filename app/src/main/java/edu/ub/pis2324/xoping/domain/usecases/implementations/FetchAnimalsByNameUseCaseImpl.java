package edu.ub.pis2324.xoping.domain.usecases.implementations;

import java.util.List;

import edu.ub.pis2324.xoping.domain.di.repositories.AnimalRepository;
import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.domain.usecases.FetchAnimalsByNameUseCase;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowableMapper;
import io.reactivex.rxjava3.core.Observable;

public class FetchAnimalsByNameUseCaseImpl implements FetchAnimalsByNameUseCase {
  /* Attributes */
  private AnimalRepository animalRepository;
  private final XopingThrowableMapper throwableMapper;

  /**
   * Constructor
   */
  public FetchAnimalsByNameUseCaseImpl(AnimalRepository animalRepository) {
    this.animalRepository = animalRepository;

    throwableMapper = new XopingThrowableMapper();
    throwableMapper.add(AnimalRepository.Error.GETBYNAME_UNKNOWN_ERROR, Error.ANIMALS_DATA_ACCESS_ERROR);
  }

  /**
   * Executes the fetch products by name use case.
   * @param name The name of the product to search for.
   * @return
   */
  @Override
  public Observable<List<Animal>> execute(String name) {
    return animalRepository.getByName(name)
        .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
  }
}
