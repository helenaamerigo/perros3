package edu.ub.pis2324.xoping.data.repositories.firestore;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ub.pis2324.xoping.domain.di.repositories.AnimalRepository;
import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.data.dtos.firestore.AnimalFirestoreDto;
import edu.ub.pis2324.xoping.data.dtos.firestore.mappers.DTOToDomainMapper;
import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import io.reactivex.rxjava3.core.Observable;

/**
 * Cloud Firestore implementation of the data store.
 */
public class AnimalFirestoreRepository implements AnimalRepository {
  /* Constants */
  private static final String ANIMALS_COLLECTION_NAME = "animals";
  /* Attributes */
  private final FirebaseFirestore db;
  private final DTOToDomainMapper DTOToDomainMapper;

  /**
   * Empty constructor
   */
  public AnimalFirestoreRepository() {
    db = FirebaseFirestore.getInstance();
    DTOToDomainMapper = new DTOToDomainMapper();
  }

  @Override
  public Observable<Animal> getById(AnimalId id) {
    return Observable.create(emitter -> {
      db.collection(ANIMALS_COLLECTION_NAME)
        .document(id.toString())
        .get()
        .addOnFailureListener(exception -> {
          emitter.onError(new XopingThrowable(Error.GETBYID_UNKNOWN_ERROR));
        })
        .addOnSuccessListener(ds -> {
          if (ds.exists()) {
            AnimalFirestoreDto animalFirestoreDto = ds.toObject(AnimalFirestoreDto.class);
            Animal animal = DTOToDomainMapper.map(animalFirestoreDto, Animal.class);
            emitter.onNext(animal);
            emitter.onComplete();
          } else {
            emitter.onError(new XopingThrowable(Error.ANIMAL_NOT_FOUND));
          }
        });
    });
  }

  public Observable<List<Animal>> getByIds(List<AnimalId> ids) {
    /* UUIDs not supported by Firestore, so we need to convert them to strings */
    List<String> idsStr = ids
        .stream()
        .map(AnimalId::toString)
        .collect(Collectors.toList());

    return Observable.create(emitter -> {
      db.collection(ANIMALS_COLLECTION_NAME)
          .whereIn(FieldPath.documentId(), idsStr) // idStr: converted UUIDs to Strings
          .get()
          .addOnFailureListener(exception -> {
            emitter.onError(new XopingThrowable(Error.GETBYIDS_UNKNOWN_ERROR));
          })
          .addOnSuccessListener(queryDocumentSnapshots -> {
            List<Animal> animals = new ArrayList<>();
            for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
              if (ds.exists()) {
                AnimalFirestoreDto animalFirestoreDto = ds.toObject(AnimalFirestoreDto.class);
                Animal animal = DTOToDomainMapper.map(animalFirestoreDto, Animal.class);
                animals.add(animal);
              }
            }
            emitter.onNext(animals);
            emitter.onComplete();
          });
    });
  }

  /**
   * Get all products from the Firebase CloudFirestore.
   */
  public Observable<List<Animal>> getAll() {
    return Observable.create(emitter -> {
      db.collection(ANIMALS_COLLECTION_NAME)
          .get()
          .addOnFailureListener(exception -> {
            emitter.onError(new XopingThrowable(Error.GETALL_UNKNOWN_ERROR));
          })
          .addOnSuccessListener(queryDocumentSnapshots -> {
            List<Animal> animals = new ArrayList<>();
            for (DocumentSnapshot ds : queryDocumentSnapshots) {
              AnimalFirestoreDto animalFirestoreDto = ds.toObject(AnimalFirestoreDto.class);
              Animal animal = DTOToDomainMapper.map(animalFirestoreDto, Animal.class);
              animals.add(animal);
            }
            emitter.onNext(animals);
            emitter.onComplete();
          });
    });
  }

  /**
   * Get a list of products from the Firebase CloudFirestore starting with animalName.
   * @param animalName The case insensitive product name.
   */
  public Observable<List<Animal>> getByName(String animalName) {
    return Observable.create(emitter -> {
      db.collection(ANIMALS_COLLECTION_NAME)
          .orderBy("nameLowerCase")
          .startAt(animalName.toLowerCase())
          .endAt(animalName.toLowerCase() + "\uf8ff")
          .get()
          .addOnFailureListener(exception -> {
            emitter.onError(new XopingThrowable(Error.GETBYNAME_UNKNOWN_ERROR));
          })
          .addOnSuccessListener(queryDocumentSnapshots -> {
            List<Animal> animals = new ArrayList<>();
            for (DocumentSnapshot ds : queryDocumentSnapshots) {
              AnimalFirestoreDto animalFirestoreDto = ds.toObject(AnimalFirestoreDto.class);
              Animal animal = DTOToDomainMapper.map(animalFirestoreDto, Animal.class);
              animals.add(animal);
            }
            emitter.onNext(animals);
            emitter.onComplete();
          });
    });  }
}
