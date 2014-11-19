package skripsi.com.grubber.ar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import skripsi.com.grubber.dao.RestaurantDao;
import skripsi.com.grubber.gps.GPSActivity;
import skripsi.com.grubber.model.Restaurant;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.parse.ParseException;

public class TestActivity extends ActionBarActivity {

  protected static final String TAG = TestActivity.class.getSimpleName();

  private List<Restaurant> mResult;
  private List<Restaurant> mTemp = new ArrayList<Restaurant>();
  private GPSActivity gpsAct;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    gpsAct = new GPSActivity(this);
    new getData().execute();
  }

  public void TestList() {

    Log.v(TAG, "Total Temp = " + mTemp.size());
    Log.v(TAG, "Total mRest = " + mResult.size());
    for (int i = 0; i < mTemp.size(); i++) {
      Log.d(TAG, "Nama = " + mTemp.get(i).getName());
    }
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
        // Transfer via Add
        Log.v(TAG, "Moving Data");
        int k = 0;
        for (int i = 0; i < mResult.size(); i++) {
          if (countDist(mResult.get(i).getLat(), mResult.get(i).getLong()) <= 2) {
            Log.v(TAG, "K = " + k);
            mTemp.add(k++, mResult.get(i));
          }
        }
        TestList();
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

}