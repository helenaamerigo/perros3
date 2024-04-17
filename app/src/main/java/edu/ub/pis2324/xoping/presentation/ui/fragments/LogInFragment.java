package edu.ub.pis2324.xoping.presentation.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
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

import com.google.gson.Gson;

import edu.ub.pis2324.xoping.AppContainer;
import edu.ub.pis2324.xoping.MyApplication;
import edu.ub.pis2324.xoping.R;
import edu.ub.pis2324.xoping.presentation.viewmodels.fragments.LogInViewModel;

public class LogInFragment extends Fragment {

  private LogInViewModel logInViewModel;

  private AppContainer appContainer;
  private NavController navController;
  private edu.ub.pis2324.xoping.databinding.FragmentLogInBinding binding;

  public static LogInFragment newInstance() {
    return new LogInFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = edu.ub.pis2324.xoping.databinding.FragmentLogInBinding.inflate(inflater, container, false);

//    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//    if (actionBar != null)
//      actionBar.hide();

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    appContainer = ((MyApplication) getActivity().getApplication()).getAppContainer();
    navController = Navigation.findNavController(view);

    /* Initializations */
    initWidgetListeners();
    initViewModel();}

  /**
   * Initialize the listeners of the widgets.
   */
  private void initWidgetListeners() {
    binding.btnLogIn.setOnClickListener(ignoredView -> {
      // Delegate the log-in logic to the viewmodel
      logInViewModel.login(
          String.valueOf(binding.etLoginUsername.getText()),
          String.valueOf(binding.etLoginPassword.getText())
      );
    });

    binding.btnSignUp.setOnClickListener(ignoredView -> {
      navController.navigate(R.id.action_logInFragment_to_signUpFragment);
    });
  }

  /**
   * Initialize the viewmodel and its observers.
   */
  private void initViewModel() {
    /* Init viewmodel */
    logInViewModel = new ViewModelProvider(
        this,
        new LogInViewModel.Factory(appContainer.logInUseCase)
    ).get(LogInViewModel.class);

    /* Init observers */
    initObservers();
  }

  /**
   * Initialize the observers of the viewmodel.
   */
  private void initObservers() {
    /* Observe the login state */
    logInViewModel.getLogInState().observe(getViewLifecycleOwner(), logInState -> {
      // Whenever there's a change in the login state of the viewmodel
      switch (logInState.getStatus()) {
        case LOADING:
          binding.btnLogIn.setEnabled(false);
          break;
        case SUCCESS:
          assert logInState.getData() != null;
//          Intent intent = new Intent(getActivity(), MainActivity.class);
//          intent.putExtra("CLIENT_MODEL", logInState.getData());
//          startActivity(intent);
//          getActivity().finish();
          SharedPreferences.Editor editor = getActivity().getSharedPreferences("LOGIN", MODE_PRIVATE).edit();
          String clientModelJsonString = new Gson().toJson(logInState.getData());
          editor.putString("CLIENT_MODEL", clientModelJsonString);
          editor.commit();
          navController.navigate(R.id.action_logInFragment_to_shoppingFragment);
          break;
        case ERROR:
          assert logInState.getError() != null;
          String errorMessage = logInState.getError().getMessage();
          Toast.makeText(this.getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
          binding.btnLogIn.setEnabled(true);
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + logInState.getStatus());
      }
    });
  }
}