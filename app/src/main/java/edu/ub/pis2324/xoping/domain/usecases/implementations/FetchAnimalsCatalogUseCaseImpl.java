package edu.ub.pis2324.xoping.domain.usecases.implementations;

import java.util.List;

import edu.ub.pis2324.xoping.domain.di.repositories.AnimalRepository;
import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.domain.usecases.FetchAnimalsCatalogUseCase;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowableMapper;
import io.reactivex.rxjava3.core.Observable;

public class FetchAnimalsCatalogUseCaseImpl implements FetchAnimalsCatalogUseCase {
  /* Attributes */
  private AnimalRepository animalRepository;
  private final XopingThrowableMapper throwableMapper;

  /**
   * Constructor
   */
  public FetchAnimalsCatalogUseCaseImpl(AnimalRepository animalRepository) {
    this.animalRepository = animalRepository;

    throwableMapper = new XopingThrowableMapper();
    throwableMapper.add(AnimalRepository.Error.GETALL_UNKNOWN_ERROR, Error.ANIMALS_DATA_ACCESS_ERROR);
  }

  /**
   * Executes the fetch products catalog use case.
   * @return
   */
  @Override
  public Observable<List<Animal>> execute() {
    return animalRepository.getAll()
      .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
  }
}
