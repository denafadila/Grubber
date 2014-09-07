package skripsi.com.grubber;

import skripsi.com.grubber.model.User;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.parse.ParseAnalytics;

public class FrontActivity extends GrubberActivity implements
    LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {
  private static final String TAG = FrontActivity.class.getSimpleName();

  private static final int FRAGMENT_LOGIN = 1;
  private static final int FRAGMENT_REGISTER = 2;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
    Log.d(TAG, "OpenGL version  : " + configurationInfo.getGlEsVersion());

    // Parse
    ParseAnalytics.trackAppOpened(getIntent());

    if (savedInstanceState == null) {
      showFragment(FRAGMENT_LOGIN, false);
    } else {
      gotoNextPage();
    }

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_main;
  }

  private void showGettingStarted() {
    Log.d(TAG, "Launch Tutorial");
    this.finish();
    GrubberApplication.showTutorial(this);
  }

  private void gotoNextPage() {
    Log.v(TAG, "Go Timeline");
  }

  private void showFragment(int fragmentIndex, boolean addToBackStack) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction transaction = fm.beginTransaction();
    Fragment f = null;
    switch (fragmentIndex) {
    case FRAGMENT_LOGIN:
      if (findViewById(R.id.registration_scroll_view) == null) {
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
            R.anim.slide_in_left, R.anim.slide_out_right);
        f = Fragment.instantiate(this, LoginFragment.class.getName());
        if (addToBackStack)
          transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, f);
        transaction.commit();
      }
      this.getSupportActionBar().show();
      this.getSupportActionBar().setTitle(R.string.grubber_main_register);
      this.getSupportActionBar().setIcon(R.drawable.ic_launcher);
      break;
    case FRAGMENT_REGISTER:
      if (findViewById(R.id.registration_scroll_view) == null) {
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
            R.anim.slide_in_left, R.anim.slide_out_right);
        f = Fragment.instantiate(this, RegisterFragment.class.getName());
        if (addToBackStack)
          transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, f);
        transaction.commit();
      }
      this.getSupportActionBar().show();
      this.getSupportActionBar().setTitle(R.string.grubber_main_register);
      this.getSupportActionBar().setIcon(R.drawable.ic_launcher);
      break;
    }
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();

  }

  @Override
  public void onSuccessfulRegistration() {
    // successful registration
    Log.d(TAG, "Returned to MainActivity after successful registration");
    showGettingStarted();
  }

  @Override
  public void onRegisterAction() {
    Log.v(TAG, "onRegisterAction");
    showFragment(FRAGMENT_REGISTER, true);
  }

  @Override
  public void onBackPressed() {
    Log.v(TAG, "onBackPressed");
    super.onBackPressed();
    this.getSupportActionBar().hide();
  }

  @Override
  public void onLoginAction(User user) {
    Log.v(TAG, "onLoginAction");
    gotoNextPage();
  }

  @Override
  public void onResetPasswordAction() {
    Log.v(TAG, "onResetPasswordAction");
    // showFragment(FRAGMENT_RESET_PASSWORD, true);
  }

}