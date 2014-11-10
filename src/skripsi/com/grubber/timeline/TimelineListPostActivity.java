package skripsi.com.grubber.timeline;

import skripsi.com.grubber.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TimelineListPostActivity extends Fragment {
  public static final String TAG = NavigationAR.class.getSimpleName();

  ListView lvPostList;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    Log.v(TAG, "Timeline List Tab Created");
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    View view = inflater.inflate(R.layout.fragment_postlist_timeline, container, false);

    lvPostList = (ListView) view.findViewById(R.id.LVrestTL);

    return view;
  }
}
