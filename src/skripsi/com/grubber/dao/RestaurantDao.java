package skripsi.com.grubber.dao;

import java.util.List;

import skripsi.com.grubber.model.Restaurant;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;

public class RestaurantDao {

  private static final String TAG = RestaurantDao.class.getSimpleName();

  public static List<skripsi.com.grubber.model.Restaurant> getRest() throws ParseException {
    // posting by self

    ParseQuery<skripsi.com.grubber.model.Restaurant> pqMine = ParseQuery
        .getQuery(skripsi.com.grubber.model.Restaurant.class);

    List<Restaurant> result = null;
    try {
      result = (List<Restaurant>) pqMine.find();
      Log.d(TAG, String.format("getRest found %s records", result == null ? 0 : result.size()));
    } catch (ParseException e) {
      Log.w(TAG, "Problem in retrieving timeline", e);
      throw e;
    }
    return result;
  }

  public static Restaurant getRestProfileById(String restId) throws ParseException {
    ParseQuery<Restaurant> pqAll = ParseQuery.getQuery(Restaurant.class);

    // pqAll.whereContains(Restaurant.ID, restId);
    pqAll.whereEqualTo(Restaurant.ID, restId);

    Restaurant pu;
    Restaurant temp = null;
    try {
      pu = pqAll.getFirst();
      Log.v(TAG, String.format("Loaded RestProfile [%s]", restId));
      temp = pu;
    } catch (ParseException e) {
      Log.w(TAG, String.format("Problem loading RestProfile [%s]", restId), e);
      throw e;
    }

    return temp;
  }
}