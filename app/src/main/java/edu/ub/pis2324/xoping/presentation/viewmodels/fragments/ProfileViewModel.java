package edu.ub.pis2324.xoping.presentation.viewmodels.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;

import edu.ub.pis2324.xoping.presentation.pos.ClientPO;
import edu.ub.pis2324.xoping.utils.livedata.StateLiveData;

public class ProfileViewModel extends AndroidViewModel {

  StateLiveData<ClientPO> clientState;

  /* Constructor */
  public ProfileViewModel(
      Application application
  ) {
    super(application);
    clientState = new StateLiveData<>();
    try {
      SharedPreferences preferences = application
          .getSharedPreferences("LOGIN", MODE_PRIVATE);
      String json = preferences.getString("CLIENT_MODEL", null);
      ClientPO clientPO = new Gson().fromJson(json, ClientPO.class);
      this.clientState.postSuccess(clientPO);
    } catch (Exception e) {
      clientState.postError(e);
    }
  }

  public StateLiveData<ClientPO> getClientState() {
    return clientState;
  }
}
