package edu.ub.pis2324.xoping.data.repositories.firestore;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ub.pis2324.xoping.domain.di.repositories.ClientRepository;
import edu.ub.pis2324.xoping.domain.model.entities.Client;
import edu.ub.pis2324.xoping.data.dtos.firestore.ClientFirestoreDto;
import edu.ub.pis2324.xoping.data.dtos.firestore.mappers.DTOToDomainMapper;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import io.reactivex.rxjava3.core.Observable;

/**
 * Cloud Firestore implementation of the data store.
 */
public class ClientFirestoreRepository implements ClientRepository {
  /* Constants */
  private static final String CLIENTS_COLLECTION_NAME = "clients";
  /* Attributes */
  private final FirebaseFirestore db;
  private final DTOToDomainMapper DTOToDomainMapper;

  /**
   * Empty constructor
   */
  public ClientFirestoreRepository() {
    db = FirebaseFirestore.getInstance();
    DTOToDomainMapper = new DTOToDomainMapper();
  }

  /**
   * Add a client to the Firebase CloudFirestore.
   *
   * @param client The client to add.
   * @return A Observable with true if the client was added
   * or an error if it already exists.
   */
  public Observable<Boolean> add(Client client) {
    return Observable.create(emitter -> {
      ClientFirestoreDto clientDto = DTOToDomainMapper.map(client, ClientFirestoreDto.class);

      db.collection(CLIENTS_COLLECTION_NAME)
          .document(client.getId().toString())
          .set(clientDto)
          .addOnFailureListener(exception -> {
            emitter.onError(new XopingThrowable(Error.ADD_UNKNOWN_ERROR));
          })
          .addOnSuccessListener(ignored -> {
            emitter.onNext(true);
            emitter.onComplete();
          });
    });
  }

  /**
   * Get a client by id (blocking version).
   *
   * @param id The client id.
   * @return A Observable with the client or null if it doesn't exist.
   */
  /*
  public Observable<ClientEntity> getById(ClientId id) {
    Task<DocumentSnapshot> task = db.collection(CLIENTS_COLLECTION_NAME)
        .document(id.toString())
        .get();

    try {
      DocumentSnapshot ds = Tasks.await(task);
      if (ds.exists()) {
        ClientDto clientDto = ds.toObject(ClientDto.class);
        ClientEntity client = dtoToDomainEntityMapper.map(clientDto, ClientEntity.class);
        return Observable.just(client);
      } else {
        return Observable.error(new NoSuchElementException());
      }
    } catch (Exception e) {
      return Observable.error(new RuntimeException(e));
    }
  }
  */

  /**
   * Get a client by id.
   *
   * @param id The client id.
   * @return A Observable with the client or null if it doesn't exist.
   */
  public Observable<Client> getById(ClientId id) {
    return Observable.create(emitter -> {
      db.collection(CLIENTS_COLLECTION_NAME)
          .document(id.toString())
          .get()
          .addOnFailureListener(e -> {
            emitter.onError(new XopingThrowable(Error.GETBYID_UNKNOWN_ERROR));
          })
          .addOnSuccessListener(ds -> {
            if (!ds.exists()) {
              emitter.onError(new XopingThrowable(Error.CLIENT_NOT_FOUND));
            } else {
              ClientFirestoreDto clientDto = ds.toObject(ClientFirestoreDto.class);
              Client client = DTOToDomainMapper.map(clientDto, Client.class);
              emitter.onNext(client);
              emitter.onComplete();
            }
          });
    });
  }

  /**
   * Remove a client from the Firebase CloudFirestore.
   *
   * @param id The client id.
   * @param onUpdateListener The updater to apply to the client.
   * @return A Observable with true if the client was removed
   * or an error if it doesn't exist.
   */
  public Observable<Boolean> update(ClientId id, OnUpdateListener onUpdateListener) {
    return Observable.create(emitter -> {
      db.runTransaction(transaction -> {
          try {
            DocumentReference docRef = db
                .collection(CLIENTS_COLLECTION_NAME)
                .document(id.toString());
            DocumentSnapshot ds = transaction.get(docRef);
            if (!ds.exists()) {
//              throw new NoSuchElementException("Document does not exist");
              return false;
            } else {
              ClientFirestoreDto clientDto = ds.toObject(ClientFirestoreDto.class);
              Client client = DTOToDomainMapper.map(clientDto, Client.class);
              onUpdateListener.update(client);
              clientDto = DTOToDomainMapper.map(client, ClientFirestoreDto.class);
              transaction.set(docRef, clientDto);
              return true;
            }
          } catch (Throwable e) {
            throw e;
          }
        })
        .addOnFailureListener(e -> {
          emitter.onError(new XopingThrowable(Error.UPDATE_UNKNOWN_ERROR));
        })
        .addOnSuccessListener(success -> {
          if (!success) {
            emitter.onError(new XopingThrowable(Error.CLIENT_NOT_FOUND));
          } else {
            emitter.onNext(success);
            emitter.onComplete();
          }
        });
    });
  }

  /**
   * Remove a client from the Firebase CloudFirestore.
   * @param id The client id.
   * @return A Observable with true if the client was removed
   *          or an error if it doesn't exist.
   */
  public Observable<Boolean> remove(ClientId id) {
    return Observable.create(emitter -> {
      db.collection(CLIENTS_COLLECTION_NAME)
          .document(id.toString())
          .delete()
          .addOnFailureListener(exception -> {
            emitter.onError(new XopingThrowable(Error.REMOVE_UNKNOWN_ERROR));
          })
          .addOnSuccessListener(ignored -> {
            emitter.onNext(true);
            emitter.onComplete();
          });
    });
  }
}
