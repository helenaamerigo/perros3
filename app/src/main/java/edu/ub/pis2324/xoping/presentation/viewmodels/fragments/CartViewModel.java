package edu.ub.pis2324.xoping.presentation.viewmodels.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2324.xoping.domain.model.entities.Animal;
import edu.ub.pis2324.xoping.domain.model.valueobjects.PricedLineItem;
import edu.ub.pis2324.xoping.presentation.pos.CartPO;
import edu.ub.pis2324.xoping.domain.responses.FetchCartResponse;
import edu.ub.pis2324.xoping.presentation.pos.ClientPO;
import edu.ub.pis2324.xoping.domain.usecases.FetchCartUseCase;
import edu.ub.pis2324.xoping.domain.usecases.RemoveFromCartUseCase;
import edu.ub.pis2324.xoping.presentation.pos.ProductPO;
import edu.ub.pis2324.xoping.presentation.pos.mappers.DomainToPOMapper;
import edu.ub.pis2324.xoping.utils.error_handling.XopingError;
import edu.ub.pis2324.xoping.utils.error_handling.XopingThrowable;
import edu.ub.pis2324.xoping.utils.livedata.StateLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartViewModel extends AndroidViewModel {

  /* Attributes */
  private FetchCartUseCase fetchCartUseCase;
  private RemoveFromCartUseCase removeFromCartUseCase;

  private ClientPO clientPO;

  private final StateLiveData<CartPO> fetchCartState;
  private final StateLiveData<CartPO> removeFromCartState;
  private final CompositeDisposable compositeDisposable;

  private final DomainToPOMapper domainToPOMapper;

  /**
   * Constructor
   * @param application the application
   * @param fetchCartUseCase the fetch cart use case
   * @param removeFromCartUseCase the remove from cart use case
   */
  public CartViewModel(Application application,
                       FetchCartUseCase fetchCartUseCase,
                       RemoveFromCartUseCase removeFromCartUseCase) {
    super(application);
    this.fetchCartUseCase = fetchCartUseCase;
    this.removeFromCartUseCase = removeFromCartUseCase;

    fetchCartState = new StateLiveData<>();
    removeFromCartState = new StateLiveData<>();
    compositeDisposable = new CompositeDisposable();

    SharedPreferences preferences = application
        .getSharedPreferences("LOGIN", MODE_PRIVATE);
    String json = preferences.getString("CLIENT_MODEL", null);
    clientPO = new Gson().fromJson(json, ClientPO.class);

    domainToPOMapper = new DomainToPOMapper();
  }

  public StateLiveData<CartPO> getFetchCartState() {
    return fetchCartState;
  }
  public StateLiveData<CartPO> getRemoveFromCartState() {
    return removeFromCartState;
  }

  public void fetchCart() {
    Disposable d = fetchCartUseCase.execute(clientPO.getId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            fetchCartResponse -> handleFetchCartSuccess(fetchCartResponse),
            throwable -> handleFetchCartError(throwable)
        );

    compositeDisposable.add(d);
  }

  public void removeFromCart(PricedLineItem<ProductPO> pricedLineItem) {
    ProductPO productPO = pricedLineItem.getItem();
    Disposable d = removeFromCartUseCase.execute(clientPO.getId(), productPO.getId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            fetchCartResponse -> handleFetchCartSuccess(fetchCartResponse),
            throwable -> handleFetchCartError(throwable)
        );

    compositeDisposable.add(d);
  }

  private void handleFetchCartSuccess(FetchCartResponse fetchCartResponse) {
    // Cast List<PricedLineItem<Product>> to List<PricedLineItem<ProductPO>>
    List<PricedLineItem<ProductPO>> pricedLineItems = new ArrayList<>();
    for (PricedLineItem<Animal> pricedLineItem : fetchCartResponse.getPricedLineItems()) {
      Animal product = pricedLineItem.getItem();
      pricedLineItems.add(
          new PricedLineItem<>(
              domainToPOMapper.map(product, ProductPO.class),
              pricedLineItem.getQuantity(),
              pricedLineItem.getSubtotalPrice()
          )
      );
    }
    // Create CartPO
    CartPO cartP0 = new CartPO(
        pricedLineItems,
        fetchCartResponse.getPriceTotal()
    );

    fetchCartState.postSuccess(cartP0);
  }

  public void handleFetchCartError(Throwable throwable) {
    String message;
    XopingError xError = ((XopingThrowable) throwable).getError();
    if (xError == RemoveFromCartUseCase.Error.CLIENT_NOT_FOUND)
      message = "Client not found";
    else if (xError == RemoveFromCartUseCase.Error.CLIENTS_DATA_ACCESS_ERROR)
      message = "Client's data access error";
    else
      message = "Unknown error";

    fetchCartState.postError(new Throwable(message));
  }

  /**
   * Factory for the ViewModel to be able to pass parameters to the constructor
   */
  public static class Factory extends ViewModelProvider.NewInstanceFactory {
    @NonNull
    private final Application application;
    private final FetchCartUseCase fetchCartUseCase;
    private final RemoveFromCartUseCase removeFromCartUseCase;

    public Factory(
        @NonNull Application application,
        FetchCartUseCase fetchCartUseCase,
        RemoveFromCartUseCase removeFromCartUseCase
    ) {
      this.application = application;
      this.fetchCartUseCase = fetchCartUseCase;
      this.removeFromCartUseCase = removeFromCartUseCase;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      return (T) new CartViewModel(application, fetchCartUseCase, removeFromCartUseCase);
    }
  }
}