package skripsi.com.grubber.dao;

import java.util.ArrayList;
import java.util.List;

import skripsi.com.android.Utility;
import skripsi.com.grubber.model.AuditableParseObject;
import skripsi.com.grubber.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UserDao extends ParseObject {
  private static final String TAG = UserDao.class.getSimpleName();

  public static User registerUser(Context context, String email, String password, String username,
      String fullname, String aboutMe, byte[] photo, Bitmap photoThumbnail) throws ParseException {
    // validation is already done, now attempt registration
    User user = new User();
    user.getParseUser().setUsername(username);
    user.getParseUser().setEmail(email);
    user.getParseUser().setPassword(password);

    // ------------------------------------------------------

    assert (ParseUser.getCurrentUser() != null);
    user.setUserName(username);
    user.setFullName(fullname);
    user.setAboutMe(aboutMe);
    if (photo != null) {
      ParseFile pp = new ParseFile(username.concat(".png"), photo);
      pp.save();
      Log.d(TAG, "Done saving profile photo file");
      user.setPhoto(pp);
    }
    if (photoThumbnail != null) {
      ParseFile thumbnail = Utility.savePicture(photoThumbnail, username.concat("-tn.png"));
      thumbnail.save();
      Log.d(TAG, "Done saving profile photo thumbnail file");
      user.setPhotoThumbnail(thumbnail);
      photoThumbnail.recycle();
    }// ------------------------------------------------------
    try {
      user.getParseUser().signUp();
    } catch (ParseException e) {
      Log.v(TAG, "Problem while signing up user", e);
      throw e;
    }

    return user;
  }

  public static User getUserProfileById(String userId, String username) throws ParseException {
    ParseQuery<ParseUser> pqAll = getUserParseQuery(userId, username);

    ParseUser pu;
    User temp = null;
    try {
      pu = pqAll.getFirst();
      Log.v(TAG, String.format("Loaded UserProfile [%s/%s]", userId, username));
    } catch (ParseException e) {
      Log.w(TAG, String.format("Problem loading UserProfile [%s/%s]", userId, username), e);
      throw e;
    }
    if (pu != null) {
      temp = new User();
      temp.parseUser = pu;
    }
    return temp;
  }

  public static ParseQuery<ParseUser> getUserParseQuery(String userId, String username) {
    List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();
    if (userId != null) {
      ParseQuery<ParseUser> pq1 = ParseUser.getQuery();
      pq1.whereEqualTo(AuditableParseObject.OBJECT_ID, userId);
      queries.add(pq1);
    }
    if (username != null) {
      ParseQuery<ParseUser> pq2 = ParseUser.getQuery();
      pq2.whereMatches(User.USERNAME, username, "i");
      queries.add(pq2);
    }

    ParseQuery<ParseUser> pqU = ParseQuery.or(queries);
    pqU.setLimit(1);
    return pqU;
  }
}
