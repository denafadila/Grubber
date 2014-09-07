package skripsi.com.grubber;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

public abstract class GrubberActivity extends ActionBarActivity {
  protected final String TAG = getClass().getName();
  private boolean shouldGoInvisible = false;
  private float mPreviousOffset = 0f;
  protected ListView mDrawerList;
  protected DrawerLayout mDrawerLayout;

  protected abstract int getContentViewId();

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentViewId());
  }

  public GrubberApplication getGrubberApplication() {
    return (GrubberApplication) this.getApplicationContext();
  }

}
