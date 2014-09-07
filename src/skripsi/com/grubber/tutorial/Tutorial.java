package skripsi.com.grubber.tutorial;

import skripsi.com.grubber.GrubberActivity;
import skripsi.com.grubber.R;
import android.os.Bundle;

public class Tutorial extends GrubberActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_tutorial);
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();

  }

  @Override
  protected int getContentViewId() {
    // TODO Auto-generated method stub
    return 0;
  }
}