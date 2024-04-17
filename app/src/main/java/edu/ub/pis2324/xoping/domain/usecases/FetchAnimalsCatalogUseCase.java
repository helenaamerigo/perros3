package edu.ub.pis2324.xoping.domain.usecases;

import java.util.List;

import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import io.reactivex.rxjava3.core.Observable;

public interface FetchAnimalsCatalogUseCase {
  Observable<List<Animal>> execute();

  enum Error implements XopingError {
    ANIMALS_DATA_ACCESS_ERROR;
  }
}
