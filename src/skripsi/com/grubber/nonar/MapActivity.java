package skripsi.com.grubber.nonar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.dao.RestaurantDao;
import skripsi.com.grubber.gps.GPSActivity;
import skripsi.com.grubber.model.Restaurant;
import skripsi.com.grubber.restaurant.RestaurantProfileActivity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;

public class MapActivity extends ActionBarActivity {
  protected static final String TAG = MapActivity.class.getSimpleName();

  // Google Map
  private LatLngBounds AUSTRALIA;
  private GoogleMap googleMap;
  List<Restaurant> mResult;
  GPSActivity gpsAct;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);
    gpsAct = new GPSActivity(this);
    if (gpsAct.canGetLocation()) {
      new getData().execute();
    }
  }

  /**
   * function to load map. If map is not created it will create it for you
   * */
  private void initilizeMap() {
    if (googleMap == null) {

      googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
      // check if map is created successfully or not
      if (googleMap == null) {
        Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT)
            .show();
      }
      googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gpsAct.getLatitude(), gpsAct
          .getLongitude())));
      googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }
    addMarker();
  }

  private void addMarker() {
    MarkerOptions marker;

    googleMap.clear();
    // create marker
    for (int i = 0; i < mResult.size(); i++) {
      // adding marker
      if (countDist(mResult.get(i).getLat(), mResult.get(i).getLong()) < 2) {
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(mResult.get(i).getLat(), mResult.get(i).getLong()))
            .title(mResult.get(i).getName()).snippet(mResult.get(i).getCity())
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pinpoint)));
        final int currPost = i;
        googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

          @Override
          public boolean onMarkerClick(Marker arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getBaseContext(), RestaurantProfileActivity.class);
            intent.putExtra("restObject", mResult.get(currPost));
            intent.putExtra("restId", mResult.get(currPost).getObjectId());
            intent.putExtra("restName", mResult.get(currPost).getName());
            startActivity(intent);
            return false;
          }
        });
      }
    }

    googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(gpsAct.getLatitude(), gpsAct.getLongitude())).title("You are here")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.currloc)));

  }

  @Override
  protected void onResume() {
    super.onResume();
    // initilizeMap();
  }

  class getData extends AsyncTask<Void, Void, List<Restaurant>> {

    @Override
    protected List<Restaurant> doInBackground(Void... params) {

      try {
        Log.v(TAG, "Retrieving restaurant data");
        mResult = RestaurantDao.getRest();
        if (mResult != null) {
          Log.v(TAG, "Populating List");
        }
      } catch (ParseException e) {
        Log.w(TAG, "Problem retrieving restaurants", e);
      }
      return mResult;
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
      Log.v(TAG, "Finished getting restaurant list!");
      Log.v(TAG, "Size mResult = " + mResult.size());
      try {
        // Loading map
        Log.v(TAG, "Initialize Map");
        initilizeMap();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public double countDist(double lat, double lng) {

    double currentLatitude = gpsAct.getLatitude();
    double currentLongitude = gpsAct.getLongitude();

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