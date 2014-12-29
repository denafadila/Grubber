package skripsi.com.grubber.trending;

import skripsi.com.grubber.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TrendingFragment extends Fragment {
  public static final String TAG = TrendingFragment.class.getSimpleName();

  public Button TREND;

  /*
   * (non-Javadoc)
   * 
   * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    Log.v(TAG, "Going to get Trending Tab Created");
    Intent ar = new Intent(getActivity(), TrendingActivity.class);
    startActivity(ar);
    super.onCreate(savedInstanceState);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
   * android.view.ViewGroup, android.os.Bundle)
   */
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    View view = inflater.inflate(R.layout.fragment_trending, container, false);

    return view;
  }
}
