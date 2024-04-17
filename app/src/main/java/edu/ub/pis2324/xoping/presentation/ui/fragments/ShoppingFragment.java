package edu.ub.pis2324.xoping.presentation.ui.fragments;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import edu.ub.pis2324.xoping.AppContainer;
import edu.ub.pis2324.xoping.MyApplication;
import edu.ub.pis2324.xoping.R;
import edu.ub.pis2324.xoping.databinding.FragmentShoppingBinding;
import edu.ub.pis2324.xoping.presentation.pos.ProductPO;
import edu.ub.pis2324.xoping.presentation.ui.adapters.ShopRecyclerViewAdapter;
import edu.ub.pis2324.xoping.presentation.viewmodels.fragments.ShoppingViewModel;

public class ShoppingFragment extends Fragment {
  /* Constants */
  private static final String RECYCLER_VIEW_STATE = "recycler_view_state";

  /* View model */
  private ShoppingViewModel shoppingViewModel;

  private ShopRecyclerViewAdapter rvProductsAdapter;
  private RecyclerView.LayoutManager rvLayoutManager;
  private Parcelable rvStateParcelable;

  /* View binding & navigation */
  private AppContainer appContainer;
  private NavController navController;
  private FragmentShoppingBinding binding;

  public static ShoppingFragment newInstance() {
    return new ShoppingFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentShoppingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    appContainer = ((MyApplication) getActivity().getApplication()).getAppContainer();
    navController = Navigation.findNavController(view);

    /* Initializations */
    initWidgetListeners();
    initRecyclerView();
    initViewModel();
  }

  /**
   * Lifecycle method called when the activity is being resumed.
   */
  @Override
  public void onResume() {
    super.onResume();
    if (rvStateParcelable != null) {
      rvLayoutManager.onRestoreInstanceState(rvStateParcelable);
    }
  }

  /**
   * Lifecycle method called when the activity is being paused.
   * @param state the bundle to save the state of the activity
   */
  @Override
  public void onSaveInstanceState(@NonNull Bundle state) {
    super.onSaveInstanceState(state);
    rvStateParcelable = rvLayoutManager.onSaveInstanceState();
    state.putParcelable(RECYCLER_VIEW_STATE, rvStateParcelable);
  }

  /**
   * Initialize the listeners of the widgets.
   */
  private void initWidgetListeners() {
    /* To include the SearchView in the actionbar just for this Fragment */
    getActivity().addMenuProvider(new MenuProvider() {
      @Override
      public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.shop_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
            return false;
          }
          @Override
          public boolean onQueryTextChange(String newText) {
            return false;
          }
        });
      }

      @Override
      public boolean onMenuItemSelected(@io.reactivex.rxjava3.annotations.NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.searchView) {
          return true;
        }
        return false;
      }
    }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
  }

  /**
   * Initialize the recycler view.
   */
  private void initRecyclerView() {
    rvLayoutManager = new LinearLayoutManager(getContext());
    binding.rvProducts.setLayoutManager(rvLayoutManager);

    initRecyclerViewAdapter();
  }

  /**
   * Initialize the recycler view adapter.
   */
  private void initRecyclerViewAdapter() {
    rvProductsAdapter = new ShopRecyclerViewAdapter(
        product -> navigateToViewProductDetailsFragment(product),
        position -> shoppingViewModel.hideProduct(position)
    );
    binding.rvProducts.setAdapter(rvProductsAdapter);
  }

  /**
   * Starts the ViewProductDetailsActivity.
   * @param productPO the product to be shown
   */
  private void navigateToViewProductDetailsFragment(ProductPO productPO) {
    Bundle bundle = new Bundle();
    bundle.putParcelable("PRODUCT", productPO);
    navController.navigate(R.id.action_shoppingFragment_to_viewProductDetailsFragment, bundle);
  }

  /**
   * Initialize the view model.
   */
  private void initViewModel() {
    /* Init viewmodel */
    shoppingViewModel = new ViewModelProvider(
        this,
        new ShoppingViewModel.Factory(
            appContainer.fetchProductsCatalogUseCase,
            appContainer.fetchProductsByNameUseCase
        )
    ).get(ShoppingViewModel.class);

    /* Initialize the observers */
    initObservers();
  }

  /**
   * Initialize the observers of the view model.
   */
  private void initObservers() {
    /* Observe the state of the products' catalog */
    shoppingViewModel.getProductsState().observe(getViewLifecycleOwner(), state -> {
      switch (state.getStatus()) {
        case SUCCESS:
          assert state.getData() != null;
          showNoProductsAvailable(state.getData().isEmpty());
          rvProductsAdapter.setData(state.getData());
          break;
        case ERROR:
          showNoProductsAvailable(true);
          break;
        default:
          break;
      }
    });

    shoppingViewModel.getHiddenProductState().observe(getViewLifecycleOwner(), position -> {
      switch (position.getStatus()) {
        case SUCCESS:
          assert position.getData() != null;
          rvProductsAdapter.removeProduct(position.getData());
          break;
        default:
          break;
      }
    });
  }

  /**
   * Shows or hides the "No products available" message.
   * @param mustShow true if the message must be shown, false otherwise
   */
  private void showNoProductsAvailable(boolean mustShow) {
    binding.clNoProductsAvailable.setVisibility(
        mustShow ? android.view.View.VISIBLE : android.view.View.GONE
    );
  }
}