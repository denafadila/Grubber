package skripsi.com.grubber.trending;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import skripsi.com.android.widget.EnhancedViewPager;
import skripsi.com.grubber.GrubberActivity;
import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.StatefulViewPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v7.app.ActionBar;

public class TrendingActivity extends GrubberActivity {

  private static final String CURRENT_TAB = "currentTab";
  private EnhancedViewPager mViewPager;
  private StatefulViewPagerAdapter mPagerAdapter;
  private List<WeakReference<Fragment>> mFragments;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    mFragments = new ArrayList<WeakReference<Fragment>>();
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);

    final ActionBar bar = this.getSupportActionBar();
    bar.setDisplayShowTitleEnabled(true);
    bar.setDisplayHomeAsUpEnabled(false);
    bar.setHomeButtonEnabled(false);

    // initialize view pager
    mViewPager = (EnhancedViewPager) findViewById(R.id.vpGettingStarted);
    final PagerTabStrip strip = PagerTabStrip.class.cast(mViewPager.findViewById(R.id.ptsMain));
    strip.setDrawFullUnderline(true);
    strip.setTabIndicatorColor(getResources().getColor(R.color.green_tosqa));
    mPagerAdapter = new StatefulViewPagerAdapter(this, mViewPager);
    mPagerAdapter.addTab(bar.newTab().setText("Rating"), TrendingRateFragment.class, null);
    mPagerAdapter.addTab(bar.newTab().setText("Cash"), TrendingCashFragment.class, null);

    if (savedInstanceState != null) {
      final int currentTab = savedInstanceState.getInt(CURRENT_TAB, 0);
      mViewPager.post(new Runnable() {
        @Override
        public void run() {
          mViewPager.setCurrentItem(currentTab);
        }
      });
    }
  }

  @Override
  public void onAttachFragment(Fragment fragment) {
    mFragments.add(new WeakReference<Fragment>(fragment));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(CURRENT_TAB, mViewPager.getCurrentItem());
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_trending;
  }

}