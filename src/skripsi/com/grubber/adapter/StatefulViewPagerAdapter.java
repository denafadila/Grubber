package skripsi.com.grubber.adapter;

import java.util.ArrayList;
import java.util.List;

import skripsi.com.android.widget.TabInfo;
import skripsi.com.grubber.trending.TrendingActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;

public class StatefulViewPagerAdapter extends FragmentStatePagerAdapter implements
    ActionBar.TabListener, ViewPager.OnPageChangeListener {

  private TrendingActivity mAba = null;
  private ViewPager mViewPager = null;
  private final List<TabInfo> mTabs = new ArrayList<TabInfo>();
  private final List<ActionBar.Tab> mAbTabs = new ArrayList<ActionBar.Tab>();

  public StatefulViewPagerAdapter(TrendingActivity trendingActivity, ViewPager vp) {
    super(trendingActivity.getSupportFragmentManager());
    this.mAba = trendingActivity;
    this.mViewPager = vp;
    mViewPager.setAdapter(this);
    mViewPager.setOnPageChangeListener(this);
  }

  public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
    TabInfo info = new TabInfo(clss, args);
    tab.setTag(info);
    tab.setTabListener(this);
    mTabs.add(info);
    mAbTabs.add(tab);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return mTabs.size();
  }

  @Override
  public Fragment getItem(int position) {
    TabInfo info = mTabs.get(position);
    return Fragment.instantiate(mAba, info.clss.getName(), info.args);
  }

  @Override
  public void onPageSelected(int position) {
    ActivityCompat.invalidateOptionsMenu(mAba);
  }

  @Override
  public void onPageScrollStateChanged(int arg0) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onPageScrolled(int arg0, float arg1, int arg2) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onTabSelected(Tab tab, FragmentTransaction ft) {
    if (mViewPager.getCurrentItem() != tab.getPosition()) {
      Object tag = tab.getTag();
      for (int i = 0; i < mTabs.size(); i++) {
        if (mTabs.get(i) == tag) {
          mViewPager.setCurrentItem(i);
        }
      }
    }
  }

  @Override
  public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mAbTabs.get(position).getText();
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }
}
