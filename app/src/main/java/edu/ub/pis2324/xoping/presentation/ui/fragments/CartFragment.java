package edu.ub.pis2324.xoping.presentation.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.ub.pis2324.xoping.AppContainer;
import edu.ub.pis2324.xoping.MyApplication;
import edu.ub.pis2324.xoping.databinding.FragmentCartBinding;
import edu.ub.pis2324.xoping.presentation.ui.adapters.CartRecyclerViewAdapter;
import edu.ub.pis2324.xoping.presentation.viewmodels.fragments.CartViewModel;

public class CartFragment extends Fragment {

  /* View model */
  private CartViewModel cartViewModel;
  private CartRecyclerViewAdapter rvCartLineItemsAdapter;
  private RecyclerView.LayoutManager rvLayoutManager;
  /* View binding & navigation */
  private AppContainer appContainer;
  private NavController navController;
  private FragmentCartBinding binding;

  public static CartFragment newInstance() {
    return new CartFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentCartBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    appContainer = ((MyApplication) getActivity().getApplication()).getAppContainer();
    navController = Navigation.findNavController(view);

    initRecyclerView();
    //initViewModel();

    cartViewModel.fetchCart();
  }

  private void initRecyclerView() {
    rvLayoutManager = new LinearLayoutManager(getContext());
    binding.rvCartLineItems.setLayoutManager(rvLayoutManager);

    initRecyclerViewAdapter();
  }

  private void initRecyclerViewAdapter() {
    rvCartLineItemsAdapter = new CartRecyclerViewAdapter(pricedLineItem -> {
      cartViewModel.removeFromCart(pricedLineItem);
    });
    binding.rvCartLineItems.setAdapter(rvCartLineItemsAdapter);
  }
/*
  private void initViewModel() {
    cartViewModel = new ViewModelProvider(
        this,
        new CartViewModel.Factory(
            getActivity().getApplication(),
            //appContainer.fetchCartUseCase,
            //appContainer.removeFromCartUseCase
 //            ((MainActivity) getActivity()).getClientModel()
        )
    ).get(CartViewModel.class);

    initObservers();
  }

 */

  private void initObservers() {
    cartViewModel.getFetchCartState().observe(getViewLifecycleOwner(), state -> {
      switch (state.getStatus()) {
        case SUCCESS:
          assert state.getData() != null;
          rvCartLineItemsAdapter.setData(state.getData().getCartLineModels());
          binding.tvTotalPriceValue.setText(state.getData().getPriceTotal().toString());
          break;
        case ERROR:
          Toast.makeText(getContext(), "Error fetching cart!", Toast.LENGTH_SHORT).show();
          break;
        default:
          break;
      }
    });

    cartViewModel.getRemoveFromCartState().observe(getViewLifecycleOwner(), state -> {
      switch (state.getStatus()) {
        case SUCCESS:
          assert state.getData() != null;
          binding.tvTotalPriceValue.setText(state.getData().getPriceTotal().toString());
          break;
        case ERROR:
          Toast.makeText(getContext(), "Error removing from cart!", Toast.LENGTH_SHORT).show();
          break;
        default:
          break;
      }
    });
  }

}