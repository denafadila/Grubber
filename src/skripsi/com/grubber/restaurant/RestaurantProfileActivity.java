package skripsi.com.grubber.restaurant;

import skripsi.com.grubber.R;
import skripsi.com.grubber.model.Restaurant;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ListView;

public class RestaurantProfileActivity extends FragmentActivity {
  private static final String TAG = RestaurantProfileActivity.class.getSimpleName();

  private static final int PROFILE_TAB = 1;
  private static final int POST_TAB = 2;

  public static final String USER_OBJECT_ID = "objectId";
  public static final String USER_SCREENNAME = "screenname";
  public static String REST_OBJECT_ID = "restId";
  public static String REST_NAME = "restName";

  private Restaurant mDataRest;
  ListView mListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
    Log.d(TAG, "OpenGL version  : " + configurationInfo.getGlEsVersion());
    REST_OBJECT_ID = (String) getIntent().getSerializableExtra("restId");
    REST_NAME = (String) getIntent().getSerializableExtra("restName");
    mDataRest = (Restaurant) getIntent().getSerializableExtra("restObject");
    Log.v(TAG, "ObjectToString = " + mDataRest.toString());
    Log.v(TAG, "Object = " + mDataRest);
    Log.v(TAG, "ID = " + REST_OBJECT_ID);
    Log.v(TAG, "CITY + " + mDataRest.getCity()); // null
    Log.v(TAG, "NAMA = " + REST_NAME);
    showFragment(PROFILE_TAB, false);

  }

  public void showFragment(int fragmentIndex, boolean addToBackStack) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction transaction = fm.beginTransaction();
    Fragment f = null;
    switch (fragmentIndex) {
    case PROFILE_TAB:
      if (findViewById(R.id.registration_scroll_view) == null) {
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
            R.anim.slide_in_left, R.anim.slide_out_right);
        f = Fragment.instantiate(this, RestaurantProfileFragment.class.getName());
        if (addToBackStack)
          transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, f);
        transaction.commit();
      }
      break;
    case POST_TAB:
      if (findViewById(R.id.registration_scroll_view) == null) {
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
            R.anim.slide_in_left, R.anim.slide_out_right);
        f = Fragment.instantiate(this, PostReviewFragment.class.getName());
        if (addToBackStack)
          transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, f);
        transaction.commit();
      }
    }
  }

  public interface FragmentChangeListener {
    public void replaceFragment(Fragment fragment);
  }

}
