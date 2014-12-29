package skripsi.com.grubber.profile;

import skripsi.com.grubber.GrubberActivity;
import skripsi.com.grubber.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ProfileActivity extends GrubberActivity {

  private static final String TAG = ProfileActivity.class.getSimpleName();
  public static final String USER_OBJECT_ID = "objectId";
  public static final String USER_USERNAME = "username";
  private static final int FRAGMENT_LOGIN = 1;
  String requestedUpId;
  String userName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestedUpId = getIntent().getStringExtra(USER_OBJECT_ID);
    Log.d(TAG, String.format("ProfileActivity is called for UserProfile %s", requestedUpId));
    userName = getIntent().getStringExtra(USER_USERNAME);
    Log.d(TAG, String.format("ProfileActivity is called for Screenname %s", userName));

    showFragment(FRAGMENT_LOGIN, false);
  }

  @Override
  protected int getContentViewId() {
    // TODO Auto-generated method stub
    return R.layout.profile_activity;
  }

  private void showFragment(int fragmentIndex, boolean addToBackStack) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction transaction = fm.beginTransaction();
    Fragment f = null;
    switch (fragmentIndex) {
    case FRAGMENT_LOGIN:
      Log.v(TAG, "Showing Main Screen");
      Bundle profileBundle = new Bundle();
      profileBundle.putString(USER_OBJECT_ID, requestedUpId);
      profileBundle.putString(USER_USERNAME, userName);
      if (findViewById(R.id.registration_scroll_view) == null) {
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
            R.anim.slide_in_left, R.anim.slide_out_right);
        f = Fragment.instantiate(this, ProfileFragment.class.getName());
        if (addToBackStack)
          transaction.addToBackStack(null);
        transaction.replace(R.id.content_profile, f);
        transaction.commit();
      }
      break;
    }
  }

  @Override
  public void onBackPressed() {
    Log.v(TAG, "onBackPressed");
    super.onBackPressed();
    this.getSupportActionBar().hide();
  }

}
