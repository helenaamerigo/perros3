package edu.ub.pis2324.xoping.presentation.viewmodels.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import edu.ub.pis2324.xoping.presentation.pos.ClientPO;
import edu.ub.pis2324.xoping.presentation.pos.ProductPO;
import edu.ub.pis2324.xoping.domain.usecases.AddProductToCartUseCase;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import edu.ub.pis2324.xoping.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ViewProductDetailsViewModel extends AndroidViewModel {

  private final AddProductToCartUseCase addProductToCartUseCase;
  /* Attributes */
  private final ClientPO clientPO;
  private final ProductPO productPO;
  private Integer quantity;
  /* LiveData */
  private final MutableLiveData<ProductPO> productState;
  private final MutableLiveData<Integer> quantityState;
  private final StateLiveData<Void> addedToCartState;
  /* RxJava */
  private final CompositeDisposable compositeDisposable;

  /* Constructor */
  public ViewProductDetailsViewModel(
      Application application,
      AddProductToCartUseCase addProductToCartUseCase,
      ProductPO productPO
  ) {
    super(application);
    this.addProductToCartUseCase = addProductToCartUseCase;
    this.productPO = productPO;

    this.quantity = 1;

    productState = new MutableLiveData<>(this.productPO);
    quantityState = new MutableLiveData<>(this.quantity);
    addedToCartState = new StateLiveData<>();

    SharedPreferences preferences = application
        .getSharedPreferences("LOGIN", MODE_PRIVATE);
    String json = preferences.getString("CLIENT_MODEL", null);
    clientPO = new Gson().fromJson(json, ClientPO.class);

    compositeDisposable = new CompositeDisposable();
  }

  @Override
  public void onCleared() {
    super.onCleared();
    compositeDisposable.dispose();
  }

  /**
   * Returns the product model
   * @return the product model
   */
  public LiveData<ProductPO> getProductState() { return productState  ; }
  /**
   * Returns the quantity of the product
   * @return the quantity of the product
   */
  public LiveData<Integer> getQuantityState() {
    return quantityState;
  }

  /**
   * Returns the state of the product being added to the cart
   * @return the state of the product being added to the cart
   */
  public StateLiveData<Void> getAddedToCartState() {
    return addedToCartState;
  }

  /**
   * Increments the quantity of the product
   */
  public void incrementQuantity() {
    quantityState.postValue(++quantity);  // Update the LiveData
  }

  /**
   * Decrements the quantity of the product
   */
  public void decrementQuantity() {
    if (quantity > 1)
      quantityState.postValue(--quantity);
  }

  /**
   * Adds the product to the cart
   */
  public void addToCart() {
    addedToCartState.postLoading();

    Disposable d = addProductToCartUseCase.execute(clientPO.getId(), productPO.getId(), quantity)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        ignored -> addedToCartState.postComplete(),
        throwable -> handleAddToCartError(throwable)
      );

    compositeDisposable.add(d);
  }

  private void handleAddToCartError(Throwable throwable) {
    String message;
    XopingError xError = ((XopingThrowable) throwable).getError();
    if (xError == AddProductToCartUseCase.Error.CLIENTS_DATA_ACCESS_ERROR)
      message = "Clients' data access error";
    else
      message = "Unknown error";

    addedToCartState.postError(new Throwable(message));
  }

  /**
   * Factory for the ViewModel to be able to pass parameters to the constructor
   */
  public static class Factory extends ViewModelProvider.NewInstanceFactory {
    private final Application application;
    private final AddProductToCartUseCase addProductToCartUseCase;
    private final ProductPO productPO;

    public Factory(
        Application application,
        AddProductToCartUseCase addProductToCartUseCase,
        ProductPO productPO
    ) {
      this.application = application;
      this.addProductToCartUseCase = addProductToCartUseCase;
      this.productPO = productPO;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      return (T) new ViewProductDetailsViewModel(
          application,
          addProductToCartUseCase,
//          clientModel,
          productPO
      );
    }
  }
}
