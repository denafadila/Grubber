package skripsi.com.grubber.timeline;

import skripsi.com.grubber.R;
import skripsi.com.grubber.ar.ARtab;
import skripsi.com.grubber.nonar.MainActivity;
import skripsi.com.grubber.profile.ProfileFragment;
import android.app.ActionBar;
import android.app.LocalActivityManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class TimelineActivity extends FragmentActivity {

  /* Tab Identifiers */
  static String timeline_tab = "Timeline";
  TabHost tabhost;
  TimelineListPostActivity timeline;
  Trending trending;
  MainActivity MapList;
  ARtab ar_tab;
  NavigationAR nav_ar;
  Notifications notif;
  Profile profile;
  ProfileFragment prof;

  LocalActivityManager mLocalActivityManager = new LocalActivityManager(MapList, false);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mLocalActivityManager.dispatchCreate(savedInstanceState);
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
    trending = new Trending();
    ar_tab = new ARtab();
    nav_ar = new NavigationAR();
    MapList = new MainActivity();
    notif = new Notifications();
    profile = new Profile();
    prof = new ProfileFragment();

    tabhost = (TabHost) findViewById(android.R.id.tabhost);
    tabhost.setOnTabChangedListener(listener);
    tabhost.setup();

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
        pushFragments(tabId, trending);
      } else if (tabId.equals("NavAR")) {
        pushFragments(tabId, nav_ar);
      } else if (tabId.equals("Notifications")) {
        pushFragments(tabId, notif);
      } else
        pushFragments(tabId, prof);
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

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    mLocalActivityManager.dispatchResume();
  }

  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
    mLocalActivityManager.dispatchPause(isFinishing());

  }
}
