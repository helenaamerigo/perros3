package edu.ub.pis2324.xoping.presentation.viewmodels.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.domain.usecases.FetchAnimalsByNameUseCase;
import edu.ub.pis2324.xoping.domain.usecases.FetchAnimalsCatalogUseCase;
import edu.ub.pis2324.xoping.presentation.pos.AnimalPO;
import edu.ub.pis2324.xoping.presentation.pos.mappers.DomainToPOMapper;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import edu.ub.pis2324.xoping.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ShoppingViewModel extends ViewModel {
  /* Attributes */
  private final FetchAnimalsCatalogUseCase fetchAnimalsCatalogUseCase;
  private final FetchAnimalsByNameUseCase fetchAnimalsByNameUseCase;
  private final List<AnimalPO> animalPOS;
  /* LiveData */
  private final StateLiveData<List<AnimalPO>> productsState;  // products' list
  private final StateLiveData<Integer> hiddenProductState;
  /* RxJava */
  private final CompositeDisposable compositeDisposable;

  private final DomainToPOMapper domainToPOMapper;

  /* Constructor */
  public ShoppingViewModel(FetchAnimalsCatalogUseCase fetchAnimalsCatalogUseCase,
                           FetchAnimalsByNameUseCase fetchAnimalsByNameUseCase) {
    super();
    this.fetchAnimalsCatalogUseCase = fetchAnimalsCatalogUseCase;
    this.fetchAnimalsByNameUseCase = fetchAnimalsByNameUseCase;
    animalPOS = new ArrayList<>();
    productsState = new StateLiveData<>();
    hiddenProductState = new StateLiveData<>();
    compositeDisposable = new CompositeDisposable();

    domainToPOMapper = new DomainToPOMapper();

    fetchProductsCatalog();
  }

  /**
   * Parent class (ViewModel's) lifecycle-related method
   * that is invoked when the activity is purposedly destroyed.
   */
  @Override
  public void onCleared() {
    compositeDisposable.dispose();
  }

  /**
   * Returns the state of the products being fetched
   * @return the state of the products being fetched
   */
  public StateLiveData<List<AnimalPO>> getProductsState() {
    return productsState;
  }

  /**
   * Returns the state of the product being hidden
   * @return
   */
  public StateLiveData<Integer> getHiddenProductState() {
    return hiddenProductState;
  }

  /**
   * Fetches the products from a data store
   */
  public void fetchProductsCatalog() {
    /*
      To inform to the activity that the products are being fetched,
      in case it wants to show a loading spinner or something.
    */
    productsState.postLoading();

    /*
      Now we ask for the products to some service/respository/usecase.
      But the response is asychronous, so we ask the productRepository
      to set an observable and we subscribe to it.
    */
    Disposable d = fetchAnimalsCatalogUseCase.execute()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        gottenProducts -> handleFetchProductsCatalogSuccess(gottenProducts),
        throwable -> productsState.postError(new Throwable("Cannot get products from data store"))
      );

      compositeDisposable.add(d);
  }

  /**
   * Fetches the products using the use case
   */
  public void fetchProductsByName(String name) {
    /* To inform to the activity that the products are being fetched */
    productsState.postLoading();

    /*
      Now we ask for the products to the data store.
      But the response is asychronous, so we ask the productRepository
      to set an observable and we subscribe to it.
    */
    Disposable d = fetchAnimalsByNameUseCase.execute(name)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        gottenProducts -> handleFetchProductsCatalogSuccess(gottenProducts),
        throwable -> handleFetchProductsCatalogError(throwable)
      );

    compositeDisposable.add(d);
  }

  public void handleFetchProductsCatalogSuccess(List<Animal> gottenProducts) {
    // Presentation code
    List<AnimalPO> gottenAnimalPOS = gottenProducts
        .stream()
        .map(product -> domainToPOMapper.map(product, AnimalPO.class))
        .collect(Collectors.toList());

    animalPOS.clear();
    animalPOS.addAll(gottenAnimalPOS);
    productsState.postSuccess(animalPOS);
  }

  public void handleFetchProductsCatalogError(Throwable throwable) {
    String message;
    XopingError xError = ((XopingThrowable) throwable).getError();
    if (xError == FetchAnimalsCatalogUseCase.Error.ANIMALS_DATA_ACCESS_ERROR)
      message = "Data access error";
    else
      message = "Unknown error";

    productsState.postError(new Throwable(message));
  }

  /**
   * Hides a product
   * @param position
   */
  public void hideProduct(int position) {
    animalPOS.remove(position);
    hiddenProductState.postSuccess(position);
  }

  /**
   * Factory for the ViewModel to be able to pass parameters to the constructor
   */
  public static class Factory extends ViewModelProvider.NewInstanceFactory {
    private final FetchAnimalsCatalogUseCase fetchAnimalsCatalogUseCase;
    private final FetchAnimalsByNameUseCase fetchAnimalsByNameUseCase;

    public Factory(
        FetchAnimalsCatalogUseCase fetchAnimalsCatalogUseCase,
        FetchAnimalsByNameUseCase fetchAnimalsByNameUseCase
    ) {
      this.fetchAnimalsCatalogUseCase = fetchAnimalsCatalogUseCase;
      this.fetchAnimalsByNameUseCase = fetchAnimalsByNameUseCase;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      return (T) new ShoppingViewModel(
              fetchAnimalsCatalogUseCase,
              fetchAnimalsByNameUseCase
      );
    }
  }
}
