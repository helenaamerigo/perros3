package edu.ub.pis2324.xoping.domain.usecases.implementations;

import edu.ub.pis2324.xoping.domain.di.repositories.ClientRepository;
import edu.ub.pis2324.xoping.domain.model.entities.Client;
import edu.ub.pis2324.xoping.domain.model.valueobjects.ClientId;
import edu.ub.pis2324.xoping.domain.model.valueobjects.AnimalId;
import edu.ub.pis2324.xoping.domain.usecases.AddProductToCartUseCase;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowableMapper;
import io.reactivex.rxjava3.core.Observable;

public class AddProductToCartUseCaseImpl implements AddProductToCartUseCase {

  private ClientRepository clientRepository;
  private final XopingThrowableMapper throwableMapper;

  public AddProductToCartUseCaseImpl(
      ClientRepository clientRepository
  ) {
    this.clientRepository = clientRepository;

    throwableMapper = new XopingThrowableMapper();
    throwableMapper.add(ClientRepository.Error.UPDATE_UNKNOWN_ERROR, Error.CLIENTS_DATA_ACCESS_ERROR);
  }

  public Observable<Boolean> execute(ClientId clientId, AnimalId productId, Integer quantity) {
    /*
     The update method runs Firestore transactionally (a bit advanced topic).
     It is a way to ensure that the client's cart is updated correctly even if there are
     concurrent updates.
     */
    return clientRepository.update(
        clientId, new ClientRepository.OnUpdateListener() { // Listener implementation
          @Override
          public void update(Client client) {
            client.addProductToCart(productId, quantity);
          }
        })
        .onErrorResumeNext(throwable -> Observable.error(throwableMapper.map(throwable)));
  }
}
