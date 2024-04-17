package edu.ub.pis2324.xoping.presentation.ui.fragments;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.ub.pis2324.xoping.AppContainer;
import edu.ub.pis2324.xoping.MyApplication;
import edu.ub.pis2324.xoping.databinding.FragmentSignUpBinding;
import edu.ub.pis2324.xoping.presentation.viewmodels.fragments.SignUpViewModel;

public class SignUpFragment extends Fragment {
  /* Constants */
  public static final String TAG = "SignUpFragment";
  /* ViewModel */
  private SignUpViewModel signUpViewModel;

  private AppContainer appContainer;
  private NavController navController;
  private FragmentSignUpBinding binding;


  public static SignUpFragment newInstance() {
    return new SignUpFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentSignUpBinding.inflate(inflater, container, false);

    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null)
      actionBar.show();

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    appContainer = ((MyApplication) getActivity().getApplication()).getAppContainer();
    navController = Navigation.findNavController(view);

    initWidgetListeners();
    initViewModel();
  }

  /**
   * Initialize the listeners of the widgets.
   */
  private void initWidgetListeners() {
    binding.btnSignUp.setOnClickListener(ignoredView -> {
      signUpViewModel.signUp(
          String.valueOf(binding.etSignupUsername.getText()),
          String.valueOf(binding.etSignUpEmail.getText()),
          String.valueOf(binding.etSignupPassword.getText()),
          String.valueOf(binding.etSignupPasswordConfirmation.getText())
      );
    });
//    binding.tbSignUp.setOnClickListener(ignoredView ->
//        navController.navigateUp()
//    );
  }

  /**
   * Initialize the viewmodel and its observers.
   */
  private void initViewModel() {
    /* Access the app container for dependency injection */
    appContainer = ((MyApplication) getActivity().getApplication()).getAppContainer();

    /* Init viewmodel */
    signUpViewModel = new ViewModelProvider(
        this,
        new SignUpViewModel.Factory(appContainer.signUpUseCase)
    ).get(SignUpViewModel.class);

    /* Init observers */
    initObservers();
  }

  /**
   * Initialize the observers of the viewmodel.
   */
  private void initObservers() {
    signUpViewModel.getSignUpState().observe(getViewLifecycleOwner(), state -> {
      switch (state.getStatus()) {
        case COMPLETE:
          navController.navigateUp();
          break;
        case ERROR:
          Toast.makeText(getActivity(), state.getError().getMessage(), Toast.LENGTH_SHORT).show();
          break;
      }
    });
  }
}