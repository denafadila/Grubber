package skripsi.com.grubber;

import skripsi.com.android.Utility;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.Restaurant;
import skripsi.com.grubber.tutorial.Tutorial;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class GrubberApplication extends Application {
  public static final String TAG = GrubberApplication.class.getSimpleName();

  public static final String PARSE_APP_ID = "com.parse.APP_ID";
  public static final String PARSE_CLIENT_KEY = "com.parse.CLIENT_KEY";

  @Override
  public void onCreate() {
    super.onCreate();
    Log.v(TAG, "Initializing Parse");
    initializeParse();
  }

  private void initializeParse() {
    // ParseObject subclass registration
    ParseObject.registerSubclass(Restaurant.class);
    ParseObject.registerSubclass(Activity.class);
    // initialize Parse
    Parse.initialize(this, Utility.getMetadata(this, GrubberApplication.PARSE_APP_ID),
        Utility.getMetadata(this, GrubberApplication.PARSE_CLIENT_KEY));
    // by default, all objects are public
    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }

  public static void showTutorial(Context context) {
    Intent i = new Intent(context, Tutorial.class);
    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    context.startActivity(i);
  }

  public static void showTimeline(Context context) {
    // Intent i = new Intent(context, TimelineActivity.class);
    // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    // context.startActivity(i);
    Log.v(TAG, "Timeline haha");
  }

  public static void showSearch(Context context) {
    throw new UnsupportedOperationException();
  }

  private void logoutInternally(Context context) {
    ParseUser.logOut();

    Intent i = new Intent(context, MainActivity.class);
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    context.startActivity(i);
  }

}
