package edu.ub.pis2324.xoping.presentation.viewmodels.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.domain.usecases.FetchProductsByNameUseCase;
import edu.ub.pis2324.xoping.domain.usecases.FetchProductsCatalogUseCase;
import edu.ub.pis2324.xoping.presentation.pos.ProductPO;
import edu.ub.pis2324.xoping.presentation.pos.mappers.DomainToPOMapper;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import edu.ub.pis2324.xoping.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CatalegViewModel extends ViewModel {
    /* Attributes */
    private final FetchProductsCatalogUseCase fetchProductsCatalogUseCase;
    private final FetchProductsByNameUseCase fetchProductsByNameUseCase;
    private final List<ProductPO> productPOs;
    /* LiveData */
    private final StateLiveData<List<ProductPO>> productsState;  // products' list
    private final StateLiveData<Integer> hiddenProductState;
    /* RxJava */
    private final CompositeDisposable compositeDisposable;

    private final DomainToPOMapper domainToPOMapper;

    /* Constructor */
    public CatalegViewModel(FetchProductsCatalogUseCase fetchProductsCatalogUseCase,
                            FetchProductsByNameUseCase fetchProductsByNameUseCase) {
        super();
        this.fetchProductsCatalogUseCase = fetchProductsCatalogUseCase;
        this.fetchProductsByNameUseCase = fetchProductsByNameUseCase;
        productPOs = new ArrayList<>();
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
    public StateLiveData<List<ProductPO>> getProductsState() {
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
        Disposable d = fetchProductsCatalogUseCase.execute()
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
        Disposable d = fetchProductsByNameUseCase.execute(name)
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
        List<ProductPO> gottenProductPOs = gottenProducts
                .stream()
                .map(product -> domainToPOMapper.map(product, ProductPO.class))
                .collect(Collectors.toList());

        productPOs.clear();
        productPOs.addAll(gottenProductPOs);
        productsState.postSuccess(productPOs);
    }

    public void handleFetchProductsCatalogError(Throwable throwable) {
        String message;
        XopingError xError = ((XopingThrowable) throwable).getError();
        if (xError == FetchProductsCatalogUseCase.Error.PRODUCTS_DATA_ACCESS_ERROR)
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
        productPOs.remove(position);
        hiddenProductState.postSuccess(position);
    }

    /**
     * Factory for the ViewModel to be able to pass parameters to the constructor
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final FetchProductsCatalogUseCase fetchProductsCatalogUseCase;
        private final FetchProductsByNameUseCase fetchProductsByNameUseCase;

        public Factory(
                FetchProductsCatalogUseCase fetchProductsCatalogUseCase,
                FetchProductsByNameUseCase fetchProductsByNameUseCase
        ) {
            this.fetchProductsCatalogUseCase = fetchProductsCatalogUseCase;
            this.fetchProductsByNameUseCase = fetchProductsByNameUseCase;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CatalegViewModel(
                    fetchProductsCatalogUseCase,
                    fetchProductsByNameUseCase
            );
        }
    }
}
