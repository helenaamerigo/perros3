package edu.ub.pis2324.xoping.presentation.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import edu.ub.pis2324.xoping.AppContainer;
import edu.ub.pis2324.xoping.MyApplication;
import edu.ub.pis2324.xoping.databinding.FragmentViewProductDetailsBinding;
import edu.ub.pis2324.xoping.presentation.pos.ProductPO;
import edu.ub.pis2324.xoping.presentation.viewmodels.fragments.ViewProductDetailsViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewProductDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProductDetailsFragment extends Fragment {

  private ViewProductDetailsViewModel viewProductDetailsViewModel;

  /* View binding & navigation */
  private AppContainer appContainer;
  private NavController navController;
  private FragmentViewProductDetailsBinding binding;

  public static ViewProductDetailsFragment newInstance() {
    return new ViewProductDetailsFragment();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentViewProductDetailsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    appContainer = ((MyApplication) getActivity().getApplication()).getAppContainer();
    navController = Navigation.findNavController(view);

    /* Get product model from arguments */
//    ClientModel clientModel = ((MainActivity) getActivity()).getClientModel();
//    assert clientModel != null;
    ProductPO productPO = getArguments().getParcelable("PRODUCT");
    assert productPO != null;

    /* Initializations */
    initWidgetListeners();
//    initViewModel(clientModel, productModel);
    initViewModel(productPO);
  }

  /**
   * Initialize the listeners of the widgets
   */
  private void initWidgetListeners() {
    // Set listener of the "+" button
    binding.ibtnIncreaseItemQuantity.setOnClickListener(v ->
        viewProductDetailsViewModel.incrementQuantity()
    );

    // Set listener of the "-" button
    binding.ibtnDecreaseItemQuantity.setOnClickListener(v ->
        viewProductDetailsViewModel.decrementQuantity()
    );

    // Set listener of the "Buy" button
    binding.btnBuy.setEnabled(true);
    binding.btnBuy.setOnClickListener(v -> {
      binding.btnBuy.setEnabled(false);
      viewProductDetailsViewModel.addToCart();
    });

//    binding.tbViewProductDetails.setNavigationOnClickListener(v ->
//        navController.navigateUp()
//    );
  }

  /**
   * Initialize the view model
   */
//  private void initViewModel(ClientModel clientModel, ProductModel productModel) {
//    /* Init viewmodel */
//    viewProductDetailsViewModel = new ViewModelProvider(
//        this,
//        new ViewProductDetailsViewModel.Factory(
//            appContainer.addProductToCartUseCase,
//            clientModel,
//            productModel
//        )
//    ).get(ViewProductDetailsViewModel.class);
//
//    /* Initialize the observers */
//    initObservers();
//  }
  private void initViewModel(ProductPO productPO) {
    /* Init viewmodel */
    viewProductDetailsViewModel = new ViewModelProvider(
        this,
        new ViewProductDetailsViewModel.Factory(
            getActivity().getApplication(),
            appContainer.addProductToCartUseCase,
            productPO
        )
    ).get(ViewProductDetailsViewModel.class);

    /* Initialize the observers */
    initObservers();
  }

  /**
   * Initialize the observers of the view model
   */
  private void initObservers() {
    /* Observe changes in the product state */
    viewProductDetailsViewModel.getProductState().observe(getViewLifecycleOwner(), productModel -> {
      binding.tvDialogProductName.setText(productModel.getName());
      String priceText = productModel.getPrice().toString();
      binding.tvDialogProductPrice.setText(priceText);
      binding.tvDialogProductDescription.setText(productModel.getDescription());
      Picasso.get().load(productModel.getImageUrl()).into(binding.ivDialogProductImage);
    });

    /* Observe changes in the quantity */
    viewProductDetailsViewModel.getQuantityState().observe(getViewLifecycleOwner(), quantity ->
      binding.tvDialogProductQuantity.setText(quantity.toString())
    );

    /* Observe changes in the added to cart state */
    viewProductDetailsViewModel.getAddedToCartState().observe(getViewLifecycleOwner(), addedToCart -> {
      switch (addedToCart.getStatus()) {
        case LOADING:
          binding.btnBuy.setEnabled(false);
          break;
        case COMPLETE:
          binding.btnBuy.setEnabled(true);
          Toast.makeText(getActivity(), "Product added to cart!", Toast.LENGTH_SHORT).show();
          navController.navigateUp();
          break;
        case ERROR:
          binding.btnBuy.setEnabled(true);
          Toast.makeText(getActivity(), "Error adding product to cart", Toast.LENGTH_SHORT).show();
          navController.navigateUp();
          break;
        default:
          break;
      }
    });
  }
}