package skripsi.com.grubber.nonar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.ListRestaurantAdapter;
import skripsi.com.grubber.model.Restaurant;
import skripsi.com.grubber.model.User;
import skripsi.com.grubber.restaurant.RestaurantProfileActivity;
import skripsi.com.grubber.utility.ConnectionDetector;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;

public class MainActivity extends ActionBarActivity {

  List<Restaurant> mResult = null;
  ListView listView;
  ListRestaurantAdapter mAdapter;
  double latitude, longitude;
  Boolean useCurrentLocation = false;
  Boolean isInternetPresent = false;
  Boolean flag = false;
  double finalDistance = 0.0;

  // The minimum distance to change Updates in meters
  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
  // The minimum time between updates in milliseconds
  private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

  private LocationManager locationManager = null;
  private LocationListener locationListener = null;

  protected static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_list);
    mResult = new ArrayList<Restaurant>();
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    Toast.makeText(getBaseContext(), "Logged in as " + User.getCurrentUser().getUserName(),
        Toast.LENGTH_LONG).show();
    checkConn();
    listView = (ListView) findViewById(R.id.LVrest);
    listView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        Log.v(TAG, "Long = " + longitude + " + Lat = " + latitude);
        finalDistance = countDist(mResult.get(arg2).getLat(), mResult.get(arg2).getLong());
        Toast.makeText(getBaseContext(), "Distance to Destination = " + finalDistance + " km",
            Toast.LENGTH_LONG).show();

        Log.v(TAG, String.format("Sending [%s %s]", mResult.get(arg2).getName(), mResult.get(arg2)
            .getObjectId()));
        Intent i = new Intent(MainActivity.this, RestaurantProfileActivity.class);
        i.putExtra("restObject", mResult.get(arg2));
        i.putExtra("restId", mResult.get(arg2).getObjectId());
        i.putExtra("restName", mResult.get(arg2).getName());
        startActivity(i);

      }
    });
    new getData().execute();

  }

  private void checkConn() {
    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
    // Check if Internet present
    isInternetPresent = cd.isConnectingToInternet();

    if (!isInternetPresent) {
      // Internet Connection is not present
      alertbox("Internet Connection Error", "Please connect to working Internet connection");
    } else {
      // check if GPS enabled
      flag = displayGpsStatus();
      if (flag) {

        Log.v(TAG, "onClick");

        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

        useCurrentLocation = true;

      } else {
        alertbox("Gps Status!!", "Your GPS is: OFF");
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  class getData extends AsyncTask<Void, Void, List<Restaurant>> {

    @Override
    protected List<Restaurant> doInBackground(Void... params) {

      List<Restaurant> result = null;
      try {
        Log.v(TAG, "Retrieving restaurant data");
        result = skripsi.com.grubber.dao.RestaurantDao.getRest();

        if (result != null) {
          Log.v(TAG, "Populating ListView");
          mResult.addAll(result);
        }
      } catch (ParseException e) {
        Log.w(TAG, "Problem retrieving restaurants", e);
      }
      return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(List<Restaurant> result) {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      mAdapter = new ListRestaurantAdapter(getBaseContext(), mResult);
      if (mAdapter.isEmpty()) {
        Log.v(TAG, "EMPTY ADAPTER?");
      } else {
        Log.v(TAG, "Adapter not empty!");
      }

      listView.setAdapter(mAdapter);

    }
  }

  /*----------Method to create an AlertBox ------------- */
  protected void alertbox(String title, String mymessage) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Your Device's GPS is Disable").setCancelable(false)
        .setTitle("** Gps Status **")
        .setPositiveButton("Gps On", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // finish the current activity
            // AlertBoxAdvance.this.finish();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);
            dialog.cancel();
          }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // cancel the dialog box
            dialog.cancel();
          }
        });
    AlertDialog alert = builder.create();
    alert.show();
  }

  /*----Method to Check GPS is enable or disable ----- */
  private Boolean displayGpsStatus() {
    ContentResolver contentResolver = getBaseContext().getContentResolver();
    boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver,
        LocationManager.GPS_PROVIDER);

    if (gpsStatus) {
      return true;

    } else {
      return false;
    }
  }

  /*----------Listener class to get coordinates ------------- */
  private class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location loc) {

      NumberFormat nm = NumberFormat.getNumberInstance();
      Toast.makeText(getBaseContext(), "Loading data...", Toast.LENGTH_SHORT).show();
      // prints longitude
      longitude = loc.getLongitude();
      Log.v(TAG, "Long = " + longitude);

      // prints latitude
      latitude = loc.getLatitude();
      Log.v(TAG, "Lat = " + latitude);

    }

    @Override
    public void onProviderDisabled(String arg0) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
      // TODO Auto-generated method stub

    }
  }

  public static String getMetadata(Context context, String key) {
    try {
      ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
          PackageManager.GET_META_DATA);
      if (ai.metaData != null) {
        return ai.metaData.getString(key);
      }
    } catch (PackageManager.NameNotFoundException e) {
      // if we can't find it in the manifest, just return null
    }

    return null;
  }

  public double countDist(double lat, double lng) {

    double currentLatitude = latitude;
    double currentLongitude = longitude;

    double endLatitude = lat;
    double endLongitude = lng;

    float[] results = new float[3];
    Location.distanceBetween(currentLatitude, currentLongitude, endLatitude, endLongitude, results);

    BigDecimal bd = new BigDecimal(results[0]);// results in meters
    BigDecimal rounded = bd.setScale(2, RoundingMode.HALF_UP);
    double values = rounded.doubleValue();

    if (values > 1) {
      values = (Double) (values * 0.001f);// convert meters to Kilometers
      bd = new BigDecimal(values);
      rounded = bd.setScale(2, RoundingMode.HALF_UP);
      values = rounded.doubleValue();
    }

    return values;
  }

}