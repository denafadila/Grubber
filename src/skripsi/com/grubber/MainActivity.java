package skripsi.com.grubber;

import skripsi.com.grubber.model.User;
import skripsi.com.grubber.timeline.TimelineActivity;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

public class MainActivity extends GrubberActivity implements
    LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

  private static final String TAG = MainActivity.class.getSimpleName();

  private static final int FRAGMENT_LOGIN = 1;
  private static final int FRAGMENT_REGISTER = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActionBar actionBar = getActionBar();
    actionBar.hide();

    final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
    Log.d(TAG, "OpenGL version  : " + configurationInfo.getGlEsVersion());

    Context context = this.getApplicationContext();
    SharedPreferences settings = context.getSharedPreferences("PREFERENCES", 0);
    boolean isLogged = settings.getBoolean("isLogged", false);

    // Parse
    ParseAnalytics.trackAppOpened(getIntent());

    if (savedInstanceState == null && ParseUser.getCurrentUser() == null) {
      showFragment(FRAGMENT_LOGIN, false);
    } else if (ParseUser.getCurrentUser() != null) {
      gotoNextPage();
    } else {
      boolean isActionBarShowing = savedInstanceState.getBoolean("isActionBarShowing");
      if (isActionBarShowing) {
        getSupportActionBar().show();
        this.getSupportActionBar().setIcon(R.drawable.ic_navigation_drawer);
      } else {
        getSupportActionBar().hide();
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected int getContentViewId() {
    // TODO Auto-generated method stub
    return R.layout.activity_main;
  }

  private void showFragment(int fragmentIndex, boolean addToBackStack) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction transaction = fm.beginTransaction();
    Fragment f = null;
    switch (fragmentIndex) {
    case FRAGMENT_LOGIN:
      Log.v(TAG, "Showing Main Screen");
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
      this.getSupportActionBar().setIcon(R.drawable.ic_launcher_new);
      break;
    case FRAGMENT_REGISTER:
      Log.v(TAG, "Showing Registration");
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
      this.getSupportActionBar().setIcon(R.drawable.ic_launcher_new);
      break;
    }
  }

  private void gotoNextPage() {
    Log.v(TAG, "Go Timeline");
    Intent timeline = new Intent(getBaseContext(), TimelineActivity.class);
    startActivity(timeline);
    this.finish();
  }

  private void showGettingStarted() {
    Log.d(TAG, "Launch Tutorial");
    this.finish();
    GrubberApplication.showTutorial(this);
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
    Context context = this.getApplicationContext();
    SharedPreferences settings = context.getSharedPreferences("PREFERENCES", 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putBoolean("isLogged", true);
    editor.commit();
    gotoNextPage();
  }

  @Override
  public void onResetPasswordAction() {
    Log.v(TAG, "onResetPasswordAction");
    // showFragment(FRAGMENT_RESET_PASSWORD, true);
  }

}
