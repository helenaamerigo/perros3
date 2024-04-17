package edu.ub.pis2324.xoping;

import edu.ub.pis2324.xoping.data.repositories.firestore.FirestoreRepositoryFactory;
import edu.ub.pis2324.xoping.domain.di.repositories.ClientRepository;
import edu.ub.pis2324.xoping.domain.di.repositories.AnimalRepository;
import edu.ub.pis2324.xoping.domain.di.repositories.AbstractRepositoryFactory;
import edu.ub.pis2324.xoping.domain.usecases.AddProductToCartUseCase;
import edu.ub.pis2324.xoping.domain.services.CheckClientExistsService;
import edu.ub.pis2324.xoping.domain.usecases.FetchClientUseCase;
import edu.ub.pis2324.xoping.domain.usecases.FetchAnimalsByNameUseCase;
import edu.ub.pis2324.xoping.domain.usecases.FetchAnimalsCatalogUseCase;
import edu.ub.pis2324.xoping.domain.usecases.LogInUseCase;
import edu.ub.pis2324.xoping.domain.usecases.SignUpUseCase;
import edu.ub.pis2324.xoping.domain.usecases.implementations.AddProductToCartUseCaseImpl;
import edu.ub.pis2324.xoping.domain.services.implementations.CheckClientExistsServiceImpl;
//import edu.ub.pis2324.xoping.domain.usecases.implementations.FetchCartUseCaseImpl;
import edu.ub.pis2324.xoping.domain.usecases.implementations.FetchClientUseCaseImpl;
import edu.ub.pis2324.xoping.domain.usecases.implementations.FetchAnimalsByNameUseCaseImpl;
import edu.ub.pis2324.xoping.domain.usecases.implementations.FetchAnimalsCatalogUseCaseImpl;
import edu.ub.pis2324.xoping.domain.usecases.implementations.LogInUseCaseImpl;
import edu.ub.pis2324.xoping.domain.usecases.implementations.SignUpUseCaseImpl;

public class AppContainer {
  /* Repositories */
  public final AbstractRepositoryFactory abstractRepositoryFactory
      = new FirestoreRepositoryFactory();
  public final ClientRepository clientRepository
      = abstractRepositoryFactory.createClientRepository();
  public final AnimalRepository productRepository
      = abstractRepositoryFactory.createProductRepository();

  /* Domain-application services */
  public final CheckClientExistsService checkClientUseCase
      = new CheckClientExistsServiceImpl(clientRepository);

  /* Use cases */
  public final AddProductToCartUseCase addProductToCartUseCase
      = new AddProductToCartUseCaseImpl(clientRepository);

  public final FetchClientUseCase fetchClientUseCase
      = new FetchClientUseCaseImpl(clientRepository);

  //public final FetchCartUseCase fetchCartUseCase
     // = new FetchCartUseCaseImpl(fetchClientUseCase, productRepository);

  public final FetchAnimalsCatalogUseCase fetchAnimalsCatalogUseCase
      = new FetchAnimalsCatalogUseCaseImpl(productRepository);

  public final FetchAnimalsByNameUseCase fetchAnimalsByNameUseCase
      = new FetchAnimalsByNameUseCaseImpl(productRepository);

  public final LogInUseCase logInUseCase
      = new LogInUseCaseImpl(fetchClientUseCase);

  public final SignUpUseCase signUpUseCase
      = new SignUpUseCaseImpl(checkClientUseCase, clientRepository);

  //public final RemoveFromCartUseCase removeFromCartUseCase
    //  = new RemoveFromCartUseCaseImpl(fetchCartUseCase, clientRepository);
}
