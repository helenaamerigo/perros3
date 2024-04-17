package edu.ub.pis2324.xoping.presentation.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import edu.ub.pis2324.xoping.AppContainer;
import edu.ub.pis2324.xoping.MyApplication;
import edu.ub.pis2324.xoping.R;
import edu.ub.pis2324.xoping.presentation.pos.ClientPO;
import edu.ub.pis2324.xoping.databinding.FragmentProfileBinding;
import edu.ub.pis2324.xoping.presentation.viewmodels.fragments.ProfileViewModel;

public class ProfileFragment extends Fragment {

  private ProfileViewModel profileViewModel;

  /* View binding & navigation */
  private AppContainer appContainer;
  private NavController navController;
  private FragmentProfileBinding binding;

  public static ProfileFragment newInstance() {
    return new ProfileFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentProfileBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    appContainer = ((MyApplication) getActivity().getApplication()).getAppContainer();
    navController = Navigation.findNavController(view);

//    initWidgets();
    initWidgetListeners();
    initViewModels();
  }

//  private void initWidgets() {
////    ClientModel clientModel = ((MainActivity) getActivity()).getClientModel();
//    SharedPreferences mPrefs = getActivity().getPreferences(MODE_PRIVATE);
//    String json = mPrefs.getString("CLIENT_MODEL", null);
//    ClientModel clientModel = new Gson().fromJson(json, ClientModel.class);
//
////    ClientModel clientModel = super.getArguments().getParcelable("CLIENT_MODEL");
//    Picasso.get().load(clientModel.getPhotoUrl()).into(binding.ivProfileImage);
//    binding.tvProfileId.setText(clientModel.getId());
//    binding.tvProfileUsername.setText(clientModel.getUsername());
//  }

  private void initWidgetListeners() {
    binding.btnLogout.setOnClickListener(v -> {
//      ((MainActivity) getActivity()).logout();
      navController.navigate(R.id.action_profileFragment_to_logInFragment2);
    });
  }

  private void initViewModels() {
    profileViewModel = new ViewModelProvider(
        this
    ).get(ProfileViewModel.class);

    initObservers();
  }

  private void initObservers() {
    profileViewModel.getClientState().observe(getViewLifecycleOwner(), clientState -> {
      switch (clientState.getStatus()) {
        case SUCCESS:
          ClientPO clientPO = clientState.getData();
          Picasso.get().load(clientPO.getPhotoUrl()).into(binding.ivProfileImage);
          binding.tvProfileId.setText(clientPO.getId().toString());
          binding.tvProfileEmail.setText(clientPO.getEmail());
          break;
        case ERROR:
          Toast.makeText(getContext(), "Error loading profile info!", Toast.LENGTH_SHORT).show();
          break;
      }
    });
  }
}