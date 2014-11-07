package skripsi.com.grubber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.fragment_splash);
    this.getActionBar().hide();
    // METHOD 1

    /****** Create Thread that will sleep for 5 seconds *************/
    Thread background = new Thread() {
      public void run() {

        try {
          // Thread will sleep for 5 seconds
          sleep(5 * 10);

          // After 5 seconds redirect to another intent
          Intent i = new Intent(getBaseContext(), MainActivity.class);
          startActivity(i);

          // Remove activity
          finish();

        } catch (Exception e) {

        }
      }
    };
    // start thread
    background.start();

  }

  @Override
  protected void onDestroy() {

    super.onDestroy();

  }
}