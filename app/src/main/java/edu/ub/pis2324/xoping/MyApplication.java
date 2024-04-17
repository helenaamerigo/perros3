package edu.ub.pis2324.xoping;

import android.app.Application;

/**
 * To be able to perform manual dependency injection.
 * Source:
 * https://developer.android.com/training/dependency-injection/manual
 */
public class MyApplication extends Application {
  private AppContainer appContainer;

  @Override
  public void onCreate() {
    super.onCreate();
    appContainer = new AppContainer();
  }

  public AppContainer getAppContainer() {
    return appContainer;
  }
}