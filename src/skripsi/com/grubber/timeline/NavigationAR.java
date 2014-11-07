package skripsi.com.grubber.timeline;

import skripsi.com.grubber.R;
import skripsi.com.grubber.ar.ARtab;
import skripsi.com.grubber.nonar.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class NavigationAR extends Fragment {
  public static final String TAG = NavigationAR.class.getSimpleName();

  public Button AR, NONAR;

  /*
   * (non-Javadoc)
   * 
   * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    Log.v(TAG, "Nav AR Tab Created");
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
    View view = inflater.inflate(R.layout.fragment_navar, container, false);

    AR = (Button) view.findViewById(R.id.btnAR);
    NONAR = (Button) view.findViewById(R.id.btnNonAR);

    AR.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent ar = new Intent(getActivity(), ARtab.class);
        startActivity(ar);
      }
    });

    NONAR.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent nonar = new Intent(getActivity(), MainActivity.class);
        startActivity(nonar);
      }
    });

    return view;
  }
}
