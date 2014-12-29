package skripsi.com.grubber.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import skripsi.com.android.Utility;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.AuditableParseObject;
import skripsi.com.grubber.model.Restaurant;
import skripsi.com.grubber.model.User;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ActivityDao {

  private static final String TAG = ActivityDao.class.getSimpleName();

  public static Activity createNewPost(String content, User up, Restaurant rest, String rName,
      String restId, double cash, double rate) {

    Activity activity = new Activity();
    activity.setType(Activity.TYPE_REVIEW);
    activity.setRestObject(rest);
    activity.setRestId(restId);
    activity.setRestName(rName);
    activity.setCash(cash);
    activity.setRate(rate);
    activity.setContent(content);
    activity.setCreatedBy(up);

    activity.saveInBackground();
    return activity;
  }

  public static Activity createNewFollow(User userTo, User userFrom, String status) {

    Log.v(TAG, "Succeed");
    Activity activity = new Activity();
    activity.setType(Activity.TYPE_STALK);
    activity.setTargetUser(userTo);
    activity.setStatus(status);
    activity.setCreatedBy(userFrom);

    activity.saveInBackground();
    return activity;
  }

  public static Activity createNewComment(String content, User up, String status, String reviewId,
      User targetUser) {
    Activity activity = new Activity();
    activity.setContent(content);
    activity.setCreatedBy(up);
    activity.setStatus(status);
    activity.setReviewId(reviewId);
    Log.d("DAO comment", targetUser.toString());
    activity.setTargetUser(targetUser);
    activity.setType(Activity.TYPE_COMMENT);

    activity.saveInBackground();
    return activity;
  }

  public static Activity getSinglePost(String reviewId) {
    Activity result = null;
    ParseQuery<Activity> query = new ParseQuery<Activity>("Activity");
    query.include("createdBy");
    query.include("restoId");
    query.include("targetUserProfile");
    try {
      result = query.get(reviewId);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return result;
  }

  public static List<Activity> getRestReviews(String restId) throws ParseException {
    // posting by self

    ParseQuery<Activity> pq = ParseQuery.getQuery(Activity.class);
    pq.whereMatches(Activity.REST, restId);
    pq.whereEqualTo(Activity.TYPE, Activity.TYPE_REVIEW);

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

  public static List<Restaurant> getTrendingByRate() throws ParseException {
    // posting by self

    ParseQuery<Restaurant> pq = ParseQuery.getQuery(Restaurant.class);

    // include only supported types
    pq.setLimit(10);
    pq.orderByDescending(Restaurant.STAR);
    pq.include(AuditableParseObject.CREATED_BY);

    List<Restaurant> result = null;
    try {
      result = pq.find();
      Log.d(TAG,
          String.format("getTrendByRate found %s records", result == null ? 0 : result.size()));
    } catch (ParseException e) {
      Log.w(TAG, "Problem in retrieving Restaurant", e);
      throw e;
    }
    return result;
  }

  public static List<Restaurant> getTrendingByCash() throws ParseException {
    // posting by self

    ParseQuery<Restaurant> pq = ParseQuery.getQuery(Restaurant.class);

    // include only supported types
    pq.setLimit(10);
    pq.orderByDescending(Restaurant.CASH);
    pq.include(AuditableParseObject.CREATED_BY);

    List<Restaurant> result = null;
    try {
      result = pq.find();
      Log.d(TAG,
          String.format("getTrendByRate found %s records", result == null ? 0 : result.size()));
    } catch (ParseException e) {
      Log.w(TAG, "Problem in retrieving Restaurant", e);
      throw e;
    }
    return result;
  }

  public static int getRevCount(String restId) throws ParseException {
    int result = 0;
    ParseQuery<Activity> pq = ParseQuery.getQuery(Activity.class);
    pq.whereEqualTo(Activity.REST, restId);
    pq.whereEqualTo(Activity.TYPE, Activity.TYPE_REVIEW);
    try {
      result = pq.count();
      Log.v(TAG, String.format("Counted %s posts for [%s]", result, restId));
    } catch (ParseException e) {
      Log.w(TAG, "Problem in counting posts", e);
      throw e;
    }

    return result;
  }

  public static List<Activity> getPostList(User userFrom) throws ParseException {
    // posting by self
    ParseQuery<Activity> pqMine = ParseQuery.getQuery(Activity.class);
    pqMine.whereEqualTo(Activity.TYPE, Activity.TYPE_REVIEW);
    pqMine.whereEqualTo(AuditableParseObject.CREATED_BY, userFrom.getParseUser());

    // posting by followed person
    ParseQuery<Activity> pqFollowedPerson = ParseQuery.getQuery(Activity.class);
    pqFollowedPerson.whereEqualTo(Activity.CREATED_BY, userFrom.getParseUser());
    pqFollowedPerson.whereEqualTo(Activity.TYPE, Activity.TYPE_STALK);

    ParseQuery<Activity> pqFollowedPersonPost = ParseQuery.getQuery(Activity.class);
    pqFollowedPersonPost.whereEqualTo(Activity.TYPE, Activity.TYPE_REVIEW);
    pqFollowedPersonPost.whereMatchesKeyInQuery(Activity.CREATED_BY, Activity.TARGET_USER_PROFILE,
        pqFollowedPerson);

    // consolidate
    ArrayList<ParseQuery<Activity>> queries = new ArrayList<ParseQuery<Activity>>();
    queries.add(pqMine);
    queries.add(pqFollowedPersonPost);
    // consolidated query
    ParseQuery<Activity> pq = ParseQuery.or(queries);
    // include only supported types
    pq.whereContainedIn(Activity.TYPE, Arrays.asList(Activity.TYPES));
    pq.setLimit(10);
    pq.orderByDescending(AuditableParseObject.CREATED_AT);
    pq.include(AuditableParseObject.CREATED_BY);

    List<Activity> result = null;
    try {
      result = pq.find();
      Log.d(TAG, String.format("getTimeline found %s records", result == null ? 0 : result.size()));
    } catch (ParseException e) {
      Log.w(TAG, "Problem in retrieving timeline", e);
      throw e;
    }
    return result;
  }

  public static List<Activity> getLocalPostList(User userFrom) throws ParseException {
    // posting by self
    ParseQuery<Activity> pqMine = ParseQuery.getQuery(Activity.class);
    pqMine.whereEqualTo(Activity.TYPE, Activity.TYPE_REVIEW);
    pqMine.whereEqualTo(AuditableParseObject.CREATED_BY, userFrom.getParseUser());

    // posting by followed person
    ParseQuery<Activity> pqFollowedPerson = ParseQuery.getQuery(Activity.class);
    pqFollowedPerson.whereEqualTo(Activity.CREATED_BY, userFrom.getParseUser());
    pqFollowedPerson.whereEqualTo(Activity.TYPE, Activity.TYPE_STALK);

    ParseQuery<Activity> pqFollowedPersonPost = ParseQuery.getQuery(Activity.class);
    pqFollowedPersonPost.whereEqualTo(Activity.TYPE, Activity.TYPE_REVIEW);
    pqFollowedPersonPost.whereMatchesKeyInQuery(Activity.CREATED_BY, Activity.TARGET_USER_PROFILE,
        pqFollowedPerson);

    // consolidate
    ArrayList<ParseQuery<Activity>> queries = new ArrayList<ParseQuery<Activity>>();
    queries.add(pqMine);
    queries.add(pqFollowedPersonPost);
    // consolidated query
    ParseQuery<Activity> pq = ParseQuery.or(queries);
    // include only supported types
    pq.whereContainedIn(Activity.TYPE, Arrays.asList(Activity.TYPES));
    pq.setLimit(10);
    pq.orderByDescending(AuditableParseObject.CREATED_AT);
    pq.include(AuditableParseObject.CREATED_BY);
    pq.fromLocalDatastore();
    List<Activity> result = null;
    try {
      result = pq.find();
      Log.d(TAG, String.format("getTimeline found %s records", result == null ? 0 : result.size()));
    } catch (ParseException e) {
      Log.w(TAG, "Problem in retrieving timeline", e);
      throw e;
    }
    return result;
  }

  public static List<Activity> getCommentList(String reviewId) throws ParseException {
    ParseQuery<Activity> query = new ParseQuery<Activity>("Activity");
    query.whereEqualTo("type", "C");
    query.whereEqualTo("reviewId", reviewId);
    query.include("createdBy");
    query.orderByAscending("createdAt");

    List<Activity> result = null;
    // List<Activity> local_result = null;
    try {
      result = query.find();

      /*
       * local_result = getLocalCommentList(reviewId); if(local_result.equals(result))
       * Log.d("Notification", "No new comment(s)"); dx else Log.d("Notification", "comment(s)");
       */
      Log.d(TAG,
          String.format("getCommentPostList found %s records", result == null ? 0 : result.size()));

    } catch (ParseException e) {
      Log.v(TAG, "Problem in getting posts", e);
      throw e;
    }
    return result;
  }

  public static List<Activity> getLocalCommentList(String reviewId) throws ParseException {
    ParseQuery<Activity> query = new ParseQuery<Activity>("Activity");
    query.whereEqualTo("type", "C");
    query.whereEqualTo("reviewId", reviewId);
    query.include("createdBy");
    query.orderByAscending("createdAt");
    query.fromLocalDatastore();

    List<Activity> result = null;
    try {
      result = query.find();
      Log.d(TAG,
          String.format("getCommentPostList found %s records", result == null ? 0 : result.size()));

    } catch (ParseException e) {
      Log.v(TAG, "Problem in getting posts", e);
      throw e;
    }
    return result;
  }

  public static List<Activity> getNotifications(User up, String count) throws ParseException {
    ParseQuery<Activity> query = new ParseQuery<Activity>("Activity");
    query.whereNotEqualTo("type", "R");
    query.whereNotEqualTo(Activity.CREATED_BY, up.getParseUser());
    query.whereEqualTo("targetUserProfile", up.getParseUser());
    if (count.equals("yes"))
      query.whereEqualTo("status", "Unread");
    query.include("createdBy");
    query.include("targetUserProfile");
    query.orderByDescending("createdAt");

    List<Activity> result = null;
    try {
      result = query.find();
      Log.d(TAG,
          String.format("getNotifications found %s records", result == null ? 0 : result.size()));

    } catch (ParseException e) {
      Log.v(TAG, "Problem in getting posts", e);
      throw e;
    }

    return result;
  }

  public static Activity updateCommentStatus(String reviewId) {

    Log.d("updateCommentStatus", "Masuk");
    ParseQuery<Activity> query = new ParseQuery<Activity>("Activity");
    query.whereEqualTo("type", "C");
    query.whereEqualTo("status", "Unread");
    query.whereEqualTo("reviewId", reviewId);

    query.findInBackground(new FindCallback<Activity>() {

      @Override
      public void done(List<Activity> objects, ParseException e) {
        // TODO Auto-generated method stub
        if (e == null) {
          Log.d("updateCommentStatus", String.format("%s", objects.size()));
          for (Activity act : objects) {
            Log.d("updateCommentStatus", "Coba");
            act.setStatus("Read");
            act.saveInBackground();
            Log.d("updateCommentStatus", "Berhasil");
          }

        }
      }

    });

    return null;
  }

  public static List<Activity> searchFollowedUser(User curr) throws ParseException {

    ParseQuery<Activity> pqFindAct = new ParseQuery<Activity>("Activity");

    pqFindAct.whereEqualTo(Activity.CREATED_BY, curr.getParseUser());
    pqFindAct.whereEqualTo(Activity.TYPE, Activity.TYPE_STALK);
    List<Activity> result = null;
    try {
      result = pqFindAct.find();
      Log.v(TAG, String.format("Search act found %s records", result == null ? 0 : result.size()));
    } catch (ParseException e) {
      Log.v(TAG, "Problem in retrieving people", e);
      throw e;
    }
    return result;
  }

  public static List<ParseObject> getFollowPeopleToRemove(User personToUnfollow, User up)
      throws ParseException {
    List<ParseObject> results = new ArrayList<ParseObject>();
    // find the existing FollowPerson for removal
    ParseQuery<Activity> pq = ParseQuery.getQuery(Activity.class);
    pq.whereEqualTo(Activity.CREATED_BY, up.getParseUser());
    pq.whereEqualTo(Activity.TYPE, Activity.TYPE_STALK);
    pq.whereEqualTo(Activity.TARGET_USER_PROFILE, personToUnfollow.getParseUser());
    List<Activity> existingFollowPeople = pq.find();
    for (Activity e : existingFollowPeople) {
      results.add(e);
    }
    return results;
  }

  public static List<User> searchByPeople(String searchKeyword, User currentUserProfile)
      throws ParseException {
    String regex = Utility.convertStringToRegex(searchKeyword);

    ParseQuery<ParseUser> pqFullName = ParseQuery.getQuery(ParseUser.class).whereMatches(
        User.FULL_NAME, regex, "i");
    ParseQuery<ParseUser> pqScreenName = ParseQuery.getQuery(ParseUser.class).whereMatches(
        User.USERNAME, regex, "i");
    ParseQuery<ParseUser> pqAboutMe = ParseQuery.getQuery(ParseUser.class).whereMatches(
        User.ABOUT_ME, regex, "i");
    ParseQuery<ParseUser> pqExcludeMe = ParseQuery.getQuery(ParseUser.class).whereNotEqualTo(
        User.USERNAME, currentUserProfile.getUserName());

    List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();
    queries.add(pqExcludeMe);
    queries.add(pqFullName);
    queries.add(pqScreenName);
    queries.add(pqAboutMe);

    ParseQuery<ParseUser> pqAll = ParseQuery.or(queries);
    pqAll.orderByAscending(User.FULL_NAME);
    pqAll.addAscendingOrder(User.USERNAME);
    pqAll.addAscendingOrder(User.ABOUT_ME);
    pqAll.whereNotEqualTo(User.USERNAME, currentUserProfile.getUserName());

    pqAll.setLimit(10);

    pqAll.include(User.PARSE_USER);

    List<User> result = null;
    List<ParseUser> temp = null;
    int recordsFound = 0;
    try {
      recordsFound = pqAll.count();
      Log.v(TAG, "User like found -> " + recordsFound);
      temp = pqAll.find();

      if (temp != null && !temp.isEmpty()) {
        result = new ArrayList<User>();
        for (ParseUser parseUser : temp) {
          result.add(new User(parseUser));
        }
      }
      Log.d(TAG,
          String.format("Search people found %s records", result == null ? 0 : result.size()));
    } catch (ParseException e) {
      Log.w(TAG, "Problem in retrieving people", e);
      throw e;
    }
    return result;
  }

}