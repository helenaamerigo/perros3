package edu.ub.pis2324.xoping.presentation.ui.fragments;

import static edu.ub.pis2324.xoping.utils.livedata.StateData.DataStatus.ERROR;
import static edu.ub.pis2324.xoping.utils.livedata.StateData.DataStatus.LOADING;
import static edu.ub.pis2324.xoping.utils.livedata.StateData.DataStatus.SUCCESS;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import edu.ub.pis2324.xoping.AppContainer;
import edu.ub.pis2324.xoping.databinding.FragmentCatalegBinding;
import edu.ub.pis2324.xoping.presentation.viewmodels.fragments.CatalegViewModel;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.ub.pis2324.xoping.MyApplication;
import edu.ub.pis2324.xoping.R;

public class CatalegFragment extends Fragment {
    private CatalegViewModel catalegViewModel;
    private AppContainer appContainer;
    private NavController navController;
    private FragmentCatalegBinding binding;

    public static CatalegFragment newInstance() {
        return new CatalegFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCatalegBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appContainer = ((MyApplication) getActivity().getApplication()).getAppContainer();
        navController = Navigation.findNavController(view);

        initViewModel();
        initWidgetListeners();
    }

    private void initWidgetListeners() {
        binding.layout.setOnClickListener(v -> {
            navController.navigate(R.id.action_catalegFragment_to_logInFragment);
        });
    }

    private void initViewModel() {
        catalegViewModel = new ViewModelProvider(
                this,
                new CatalegViewModel.Factory(appContainer.fetchAnimalsCatalogUseCase)
        ).get(CatalegViewModel.class);

        initObservers();
    }

    private void initObservers() {
        catalegViewModel.getAnimalsState().observe(getViewLifecycleOwner(), animals -> {
           switch (animals.getStatus()) {
               case LOADING:
                   Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                   break;
               case ERROR:
                   Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                   break;
               case SUCCESS:
                   Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                   break;
           }
        });
    }
}
