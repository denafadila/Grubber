package skripsi.com.grubber.dao;

import java.util.Arrays;
import java.util.List;

import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.AuditableParseObject;
import skripsi.com.grubber.model.Restaurant;
import skripsi.com.grubber.model.User;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;

public class ActivityDao {

  private static final String TAG = RestaurantDao.class.getSimpleName();

  public static Activity createNewPost(String content, User up, Restaurant rest, double cash,
      double rate) {
    Activity activity = new Activity();
    activity.setType(Activity.TYPE_REVIEW);
    activity.setRestId(rest);
    activity.setCash(cash);
    activity.setRate(rate);
    activity.setContent(content);
    activity.setCreatedBy(up);

    activity.saveInBackground();
    return activity;
  }

  public static List<Activity> getRestReviews(String restId) throws ParseException {
    // posting by self

    ParseQuery<Activity> pq = ParseQuery.getQuery(Activity.class);
    pq.whereMatches(Restaurant.ID, restId);

    // include only supported types
    pq.whereContainedIn(Activity.TYPE, Arrays.asList(Activity.TYPES));
    pq.setLimit(10);
    pq.orderByDescending(AuditableParseObject.CREATED_AT);
    pq.include(AuditableParseObject.CREATED_BY);

    List<Activity> result = null;
    try {
      result = pq.find();
      Log.d(TAG,
          String.format("getRestReview found %s records", result == null ? 0 : result.size()));
    } catch (ParseException e) {
      Log.w(TAG, "Problem in retrieving postsByUser", e);
      throw e;
    }
    return result;
  }

  public static int getRevCount(Restaurant restId) throws ParseException {
    int result = 0;
    List<Activity> act;
    ParseQuery<Activity> pq = ParseQuery.getQuery(Activity.class);
    pq.whereEqualTo(Restaurant.ID, restId.getObjectId());
    pq.whereContains(Activity.TYPE, Activity.TYPE_REVIEW);
    try {
      act = pq.find();
      // result = pq.count();
      Log.v(TAG, String.format("Counted %s posts for [%s]", act.size(), restId.getName()));
    } catch (ParseException e) {
      Log.w(TAG, "Problem in counting posts", e);
      throw e;
    }

    return 0;
  }

}