package skripsi.com.grubber.nonar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
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
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;

public class MapActivity extends ActionBarActivity {
  protected static final String TAG = MapActivity.class.getSimpleName();

  // Google Map
  private GoogleMap googleMap;
  private HashMap<Marker, Restaurant> markerMap = new HashMap<Marker, Restaurant>();

  List<Restaurant> mResult;
  GPSActivity gpsAct;
  final int currPost = 0;

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
      googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
      googleMap.getUiSettings().setZoomControlsEnabled(true);
      googleMap.getUiSettings().setCompassEnabled(true);
      googleMap.getUiSettings().setMyLocationButtonEnabled(true);

      googleMap.getUiSettings().setRotateGesturesEnabled(false);
      googleMap.getUiSettings().setScrollGesturesEnabled(true);
      googleMap.getUiSettings().setTiltGesturesEnabled(true);
      googleMap.getUiSettings().setZoomGesturesEnabled(true);

      googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
    }
    addMarker();
  }

  private void addMarker() {

    googleMap.clear();
    // create marker
    for (int i = 0; i < mResult.size(); i++) {
      // setting radius of <= 2 km
      if (countDist(mResult.get(i).getLat(), mResult.get(i).getLong()) < 2) {
        // adding marker
        markerMap.put(
            googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mResult.get(i).getLat(), mResult.get(i).getLong()))
                .title(mResult.get(i).getName()).snippet(mResult.get(i).getCity())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pinpoint))), mResult.get(i));

        googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

          @Override
          public void onInfoWindowClick(Marker arg0) {
            // TODO Auto-generated method stub
            arg0.showInfoWindow();
            if (markerMap.containsKey(arg0)) {
              Intent intent = new Intent(MapActivity.this, RestaurantProfileActivity.class);
              intent.putExtra("restObject", markerMap.get(arg0));
              intent.putExtra("restId", markerMap.get(arg0).getObjectId());
              intent.putExtra("restName", markerMap.get(arg0).getName());
              startActivity(intent);
            }

          }
        });

      }
    }

    googleMap.addMarker(new MarkerOptions()
        .position(new LatLng(gpsAct.getLatitude(), gpsAct.getLongitude())).title("You are here")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.currloc)));
    googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

      @Override
      public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        Log.v(TAG, "Clicked = " + arg0.getTitle());
        if (arg0.getTitle().equalsIgnoreCase("You are here")) {
          arg0.hideInfoWindow();
        } else {
          arg0.showInfoWindow();
        }
        return true;
      }
    });
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
      values = values * 0.001f;// convert meters to Kilometers
      bd = new BigDecimal(values);
      rounded = bd.setScale(2, RoundingMode.HALF_UP);
      values = rounded.doubleValue();
    }

    return values;
  }

  class MyInfoWindowAdapter implements InfoWindowAdapter {

    private final View myContentsView;

    MyInfoWindowAdapter() {
      myContentsView = getLayoutInflater().inflate(R.layout.adapter_custom_mapinfo, null);
    }

    @Override
    public View getInfoContents(Marker marker) {

      TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
      tvTitle.setText(marker.getTitle());
      TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.snippet));
      tvSnippet.setText(marker.getSnippet());

      RatingBar rbCash = ((RatingBar) myContentsView.findViewById(R.id.rbCash));
      RatingBar rbRate = ((RatingBar) myContentsView.findViewById(R.id.rbRate));

      return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
      // TODO Auto-generated method stub
      return null;
    }

  }

}