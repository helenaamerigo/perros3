package edu.ub.pis2324.xoping.presentation.ui.fragments;

import android.content.Intent;

import static android.content.Context.MODE_PRIVATE;
import static edu.ub.pis2324.xoping.utils.livedata.StateData.DataStatus.ERROR;
import static edu.ub.pis2324.xoping.utils.livedata.StateData.DataStatus.LOADING;
import static edu.ub.pis2324.xoping.utils.livedata.StateData.DataStatus.SUCCESS;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import edu.ub.pis2324.xoping.AppContainer;
import edu.ub.pis2324.xoping.databinding.FragmentCatalegBinding;
import edu.ub.pis2324.xoping.presentation.viewmodels.fragments.CatalegViewModel;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.gson.Gson;

import edu.ub.pis2324.xoping.MyApplication;
import edu.ub.pis2324.xoping.presentation.ui.activities.MainActivity;
import edu.ub.pis2324.xoping.R;

public class CatalegFragment extends Fragment {
    private static final String RECYCLER_VIEW_STATE = "recycler_view_state";
    private CatalegViewModel catalegViewModel;
    private AppContainer appContainer;
    private NavController navController;
    private FragmentCatalegBinding binding;
    private Parcelable rvStateParcelable;
    private RecyclerView.LayoutManager rvLayoutManager;

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        rvStateParcelable = rvLayoutManager.onSaveInstanceState();
        outState.putParcelable(RECYCLER_VIEW_STATE, rvStateParcelable);
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
            navController.navigate(R.id.action_logInFragment_to_catalegFragment);
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
                   binding.enrique.setEnabled(false);
                   binding.bloom.setEnabled(false);
                   binding.marieta.setEnabled(false);
                   binding.kevin.setEnabled(false);
                   break;
               case ERROR:
                   assert animals.getError() != null;
                     Toast.makeText(getContext(), animals.getError().getMessage(), Toast.LENGTH_SHORT).show();
                     binding.enrique.setEnabled(true);
                     binding.bloom.setEnabled(true);
                     binding.marieta.setEnabled(true);
                     binding.kevin.setEnabled(true);
                   break;
               case SUCCESS:
                   assert animals.getData() != null;
                   //Intent intent = new Intent(getActivity(), MainActivity.class);
                   //startActivity(intent);
                   //SharedPreferences.Editor editor = getActivity().getSharedPreferences("CATALEG", MODE_PRIVATE).edit();
                   //String clientModelJsonString = new Gson().toJson(animals.getData());
                   //editor.putString("CLIENT_MODEL", clientModelJsonString);
                   //editor.commit();
                   navController.navigate(R.id.action_catalegFragment_to_logInFragment);
                   break;
           }
        });
    }
}
