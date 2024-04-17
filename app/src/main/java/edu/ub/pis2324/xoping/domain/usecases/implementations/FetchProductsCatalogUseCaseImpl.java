package edu.ub.pis2324.xoping.domain.usecases.implementations;

import java.util.List;

import edu.ub.pis2324.xoping.domain.di.repositories.AnimalRepository;
import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowableMapper;
import io.reactivex.rxjava3.core.Observable;

public class FetchProductsCatalogUseCaseImpl implements edu.ub.pis2324.xoping.domain.usecases.FetchProductsCatalogUseCase {
  /* Attributes */
  private AnimalRepository productRepository;
  private final XopingThrowableMapper throwableMapper;

  /**
   * Constructor
   */
  public FetchProductsCatalogUseCaseImpl(AnimalRepository productRepository) {
    this.productRepository = productRepository;

    throwableMapper = new XopingThrowableMapper();
    throwableMapper.add(AnimalRepository.Error.GETALL_UNKNOWN_ERROR, Error.PRODUCTS_DATA_ACCESS_ERROR);
  }

  /**
   * Executes the fetch products catalog use case.
   * @return
   */
  @Override
  public Observable<List<Animal>> execute() {
    return productRepository.getAll()
      .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
  }
}
