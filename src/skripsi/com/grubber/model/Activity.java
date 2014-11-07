package skripsi.com.grubber.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("Activity")
public class Activity extends AuditableParseObject {
  private static final String TAG = Activity.class.getSimpleName();

  public static final String TYPE = "type";

  public static final String TYPE_REVIEW = "R";
  public static final String COUNT_REVIEW = "review";

  public static final String TYPE_COMMENT = "C";
  public static final String COUNT_COMMENTS = "comments";

  public static final String TYPE_STALK = "S";
  public static final String TARGET_USER_PROFILE = "targetUserProfile";
  public static final String TARGET_USER_PROFILE_ID = "targetUserProfileId";

  public static final String TYPE_FAVORITE = "F";

  public static final String REVIEW = "review";
  public static final String STAR = "stars";
  public static final String CASH = "cash";
  public static final String REST = "restId";

  public static final String PARENT_ACT = "parentActivity";

  public static final String[] TYPES = new String[] { TYPE_REVIEW, TYPE_COMMENT, TYPE_STALK };

  private boolean isSaved = false;
  private int revCount = 0;
  private int commentsCount = 0;
  private boolean likesCountLoaded = false;
  private boolean commentsCountLoaded = false;
  public static final int MAX_CONTENT_LENGTH = 1000;

  public boolean isSaved() {
    return isSaved;
  }

  public void setSaved(boolean isSaved) {
    this.isSaved = isSaved;
  }

  public String getContent() {
    return getString(REVIEW);
  }

  public String getType() {
    return getString(TYPE);
  }

  public void setType(String type) {
    put(TYPE, type);
  }

  // POST
  public void setContent(String content) {
    put(REVIEW, content);
  }

  public void setRate(double rate) {
    put(STAR, rate);
  }

  public void setCash(double cash) {
    put(CASH, cash);
  }

  public void setRestId(Restaurant rest) {
    put(REST, rest);
  }

  public void setParentPost(Activity post) {
    put(PARENT_ACT, post);
  }

  public Activity getParentPost() {
    return (Activity) get(PARENT_ACT);
  }

  public User getTargetUserProfile() {
    return new User((ParseUser) get(TARGET_USER_PROFILE));
  }

  public void setTargetUser(User targetUserProfile) {
    put(TARGET_USER_PROFILE, targetUserProfile.getParseUser());
    put(TARGET_USER_PROFILE_ID, targetUserProfile.getObjectId());
  }

  public void setStrComment(String comment) {
    put(REVIEW, comment);
  }

  public static List<Activity> getUniquePosts(List<Activity> posts) {
    List<Activity> parentPosts = new ArrayList<Activity>();
    if (posts != null) {
      for (Activity p : posts) {
        if (p.getParentPost() != null) {
          if (!parentPosts.contains(p.getParentPost()))
            parentPosts.add(p.getParentPost());
        } else if (!parentPosts.contains(p)) {
          parentPosts.add(p);
        }
      }
    }
    Log.v(TAG, String.format("getUniquePosts Incoming Post size vs Outgoing Post = %s vs %s",
        posts == null ? 0 : posts.size(), parentPosts.size()));
    return parentPosts;
  }

  public int getActualLikesCount() {
    return getInt(COUNT_REVIEW);
  }

  public int getActualCommentsCount() {
    return getInt(COUNT_COMMENTS);
  }

  public int getLikesCount() {
    if (!likesCountLoaded) {
      revCount = getActualLikesCount();
      likesCountLoaded = true;
    }
    return revCount;
  }

  public void setLikesCount(int likesCount) {
    this.revCount = likesCount;
  }

  public int incrementLikesCount() {
    this.revCount += 1;
    return revCount;
  }

  public int getCommentsCount() {
    if (!commentsCountLoaded) {
      commentsCount = getActualCommentsCount();
      commentsCountLoaded = true;
    }
    return commentsCount;
  }

  public void setCommentsCount(int commentsCount) {
    this.commentsCount = commentsCount;
  }

  public int incrementCommentsCount() {
    this.commentsCount += 1;
    return commentsCount;
  }

  @Override
  public boolean equals(Object o) {
    boolean result = super.equals(o);
    if (o instanceof Activity) {
      String oId = ((Activity) o).getObjectId();

      result = (oId == null && getObjectId() == null)
          || (oId != null && getObjectId() != null && getObjectId().equals(oId));
    }
    return result;
  }

  @Override
  public String toString() {
    return "Activity [getObjectId()=" + getObjectId() + "]";
  }
}