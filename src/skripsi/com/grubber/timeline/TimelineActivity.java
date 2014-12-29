package skripsi.com.grubber.timeline;

import java.util.List;

import skripsi.com.grubber.MainActivity;
import skripsi.com.grubber.R;
import skripsi.com.grubber.ar.ARActivity;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.User;
import skripsi.com.grubber.notifications.Notifications;
import skripsi.com.grubber.profile.ProfileActivity;
import skripsi.com.grubber.profile.ProfileFragment;
import skripsi.com.grubber.trending.TrendingActivity;
import skripsi.com.grubber.trending.TrendingFragment;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.parse.ParseException;

public class TimelineActivity extends FragmentActivity {

  public static final String USER_OBJECT_ID = "objectId";
  public static final String USER_USERNAME = "username";
  /* Tab Identifiers */
  static String timeline_tab = "Timeline";
  TabHost tabhost;
  TimelineListPostActivity timeline;
  TrendingFragment trending;
  MainActivity MapList;
  ARActivity ar_tab;
  NavigationARFragment nav_ar;
  Notifications notif;
  ProfileFragment prof;
  List<Activity> mResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.timeline_main_tab);

    /* Action Bar */
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowHomeEnabled(false);
    actionBar.setDisplayShowTitleEnabled(false);

    LayoutInflater inflater = LayoutInflater.from(this);
    View actionBar_view = inflater.inflate(R.layout.action_bar, null);

    Typeface font_style = Typeface.createFromAsset(getAssets(), "fonts/Bireun.ttf");
    TextView title_app = (TextView) actionBar_view.findViewById(R.id.title_app);
    title_app.setTypeface(font_style);

    actionBar.setCustomView(actionBar_view);
    actionBar.setDisplayShowCustomEnabled(true);

    /* Tab */
    timeline = new TimelineListPostActivity();
    trending = new TrendingFragment();
    ar_tab = new ARActivity();
    nav_ar = new NavigationARFragment();
    MapList = new MainActivity();
    notif = new Notifications();
    prof = new ProfileFragment();

    tabhost = (TabHost) findViewById(android.R.id.tabhost);
    tabhost.setOnTabChangedListener(listener);
    tabhost.setup();

    new RemoteDataTask().execute();
    initializeTab();

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  /* Initialize the tabs and set views and identifiers for the tabs */
  public void initializeTab() {

    // Do the same for the other tabs

    TabHost.TabSpec spec = tabhost.newTabSpec("Timeline");
    spec.setContent(new TabHost.TabContentFactory() {

      @Override
      public View createTabContent(String tag) {
        // TODO Auto-generated method stub
        return findViewById(android.R.id.tabcontent);
      }
    });
    spec.setIndicator(createTabView(R.drawable.home));
    tabhost.addTab(spec);

    spec = tabhost.newTabSpec("Trending");
    spec.setContent(new TabHost.TabContentFactory() {

      @Override
      public View createTabContent(String tag) {
        // TODO Auto-generated method stub
        return findViewById(android.R.id.tabcontent);
      }
    });
    spec.setIndicator(createTabView(R.drawable.trending));
    tabhost.addTab(spec);

    spec = tabhost.newTabSpec("NavAR");
    spec.setContent(new TabHost.TabContentFactory() {

      @Override
      public View createTabContent(String tag) {
        // TODO Auto-generated method stub
        return findViewById(android.R.id.tabcontent);
      }
    });
    spec.setIndicator(createTabView(R.drawable.ar));
    tabhost.addTab(spec);

    // LayoutInflater inflater = LayoutInflater.from(this);
    // View view = inflater.inflate(R.layout.tab_icon, null);
    // final TextView txtCount = (TextView) view.findViewById(R.id.txtCount);
    spec = tabhost.newTabSpec("Notifications");
    spec.setContent(new TabHost.TabContentFactory() {

      @Override
      public View createTabContent(String tag) {
        // TODO Auto-generated method stub
        return findViewById(android.R.id.tabcontent);
      }
    });
    spec.setIndicator(createTabView(R.drawable.notif));
    tabhost.addTab(spec);

    spec = tabhost.newTabSpec("Profile");
    spec.setContent(new TabHost.TabContentFactory() {

      @Override
      public View createTabContent(String tag) {
        // TODO Auto-generated method stub
        return findViewById(android.R.id.tabcontent);
      }
    });
    spec.setIndicator(createTabView(R.drawable.profile));
    tabhost.addTab(spec);

  }

  /* TabChangeListener for changing the tab when one of the tabs is pressed */
  TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {

    @Override
    public void onTabChanged(String tabId) {
      // TODO Auto-generated method stub

      // Set current tab

      if (tabId.equals("Timeline")) {
        pushFragments(tabId, timeline);
      } else if (tabId.equals("Trending")) {
        // pushFragments(tabId, trending);
        Intent intent = new Intent(getBaseContext(), TrendingActivity.class);
        startActivity(intent);
      } else if (tabId.equals("NavAR")) {
        pushFragments(tabId, nav_ar);
      } else if (tabId.equals("Notifications")) {
        pushFragments(tabId, notif);
      } else {
        // pushFragments(tabId, prof);
        Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
        intent.putExtra(USER_OBJECT_ID, User.getCurrentUser().getObjectId());
        intent.putExtra(USER_USERNAME, User.getCurrentUser().getUserName());
        startActivity(intent);
      }
    }
  };

  private void pushFragments(String tabId, Fragment fragment) {
    // TODO Auto-generated method stub
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction ft = manager.beginTransaction();

    ft.replace(android.R.id.tabcontent, fragment);
    ft.commit();
  }

  /* returns the tab view i.e. the tab icon */
  private View createTabView(final int id) {
    // TODO Auto-generated method stub
    View view = LayoutInflater.from(this).inflate(R.layout.tab_icon, null);
    ImageView image_view = (ImageView) view.findViewById(R.id.icon_image);
    image_view.setImageDrawable(getResources().getDrawable(id));

    return view;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    /*
     * if (id == R.id.action_settings) { return true; }
     */
    return super.onOptionsItemSelected(item);
  }

  private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {
      // TODO Auto-generated method stub
      try {
        mResult = ActivityDao.getNotifications(User.getCurrentUser(), "yes");

      } catch (ParseException e) {
        Log.v("pas awal banget", "Failed to get post list for current user");
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      int count_notif = mResult.size();
      Log.d("Jumlah notif",
          String.format("getNotifications found %s records", result == null ? 0 : count_notif));
    }
  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
  }

  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();

  }
}
