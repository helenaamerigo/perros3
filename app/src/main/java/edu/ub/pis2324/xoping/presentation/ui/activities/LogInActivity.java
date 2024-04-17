package edu.ub.pis2324.xoping.presentation.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import androidx.navigation.fragment.NavHostFragment;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import edu.ub.pis2324.xoping.R;
import edu.ub.pis2324.xoping.databinding.FragmentLogInBinding;
import edu.ub.pis2324.xoping.presentation.viewmodels.fragments.LogInViewModel;

public class LogInActivity extends AppCompatActivity {
    /* Attributes */
    private NavController navController;

    AppBarConfiguration appBarConfiguration;
    private FragmentLogInBinding binding;
    private LogInViewModel logInViewModel;

    /**
     * Called when the activity is being created.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* Initializations */
        initLogIn();

    }


    private void setVisibilitybtnLogIn(NavDestination navDestination) {
        if (navDestination.getId() == R.id.logInFragment) {
            binding.btnLogIn.setVisibility(View.VISIBLE);
        } else {
            binding.btnLogIn.setVisibility(View.GONE);
        }
    }

    /**
     * Initialize the log in.
     */

    private void initLogIn() {
        /* Set up the navigation controller */
        NavHostFragment a = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentLogin);
        System.out.println(a);
        navController = a.getNavController();

        /* Set up the bottom navigation, indicating the fragments
           that are part of the bottom navigation.
         */
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.logInFragment
        ).build();

        /* Set up the action bar */
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        /* Set up the log in button */
        binding.btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.etLoginUsername.getText().toString(), binding.etLoginPassword.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("LogInActivity", "User logged in");
                                showHome();
                            } else {
                                Log.d("LogInActivity", "User not logged in");
                                Toast.makeText(LogInActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        /* Set up the listener for the navigation */
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            setVisibilitybtnLogIn(destination);
        });


    }

    private void showHome() {
        navController.navigate(R.id.action_logInFragment_to_shoppingFragment);
    }
    @Override
    public boolean onSupportNavigateUp() {
        /* Enable the up navigation: the button shown in the left of the action bar */
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
