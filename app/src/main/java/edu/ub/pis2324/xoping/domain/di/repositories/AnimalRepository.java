package edu.ub.pis2324.xoping.domain.di.repositories;

import java.util.List;

import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import io.reactivex.rxjava3.core.Observable;

public interface AnimalRepository {
  Observable<Animal> getById(AnimalId id);
  Observable<List<Animal>> getByIds(List<AnimalId> ids);
  Observable<List<Animal>> getAll();
  Observable<List<Animal>> getByName(String productName);

  enum Error implements XopingError {
    GETBYID_UNKNOWN_ERROR,
    ANIMAL_NOT_FOUND,
    GETBYIDS_UNKNOWN_ERROR,
    GETALL_UNKNOWN_ERROR,
    GETBYNAME_UNKNOWN_ERROR;
  }
}
