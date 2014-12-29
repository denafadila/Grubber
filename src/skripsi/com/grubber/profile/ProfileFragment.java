package skripsi.com.grubber.profile;

import java.util.ArrayList;
import java.util.List;

import skripsi.com.grubber.MainActivity;
import skripsi.com.grubber.R;
import skripsi.com.grubber.dao.UserDao;
import skripsi.com.grubber.image.ImageLoader;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.User;
import skripsi.com.grubber.search.SearchActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
  private final static String TAG = ProfileFragment.class.getSimpleName();
  public static final String USER_ID = "userId";
  public static final String USER_USERNAME = "username";
  public static final String USER_OBJECT_ID = "objectId";
  // Declare
  private String mUserId = null;
  private String userName = null;
  private String fullName = null;
  private TextView tvFullName = null;
  private TextView tvUserName = null;
  private TextView tvAboutMe = null;
  private TextView tvStalk = null;
  private TextView tvRev = null;
  private int stalkCount = 0;
  private int revCount = 0;
  private List<User> listUser = new ArrayList<User>();
  private User user;
  private ImageView mPhoto = null;
  private ImageLoader imageLoader = null;
  private String aboutMe = null;
  private Button mFollow;
  private Button mLogOut;
  private Button mSearch;

  String get_requestedUpId;
  String get_username;
  private ParseUser isMe;
  private CountingTask mCountTask = null;
  private LoadProfile mLoadTask = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);

    get_requestedUpId = getActivity().getIntent().getStringExtra(USER_OBJECT_ID);
    Log.d(TAG, String.format("ProfileActivity is called for UserProfile %s", get_requestedUpId));
    get_username = getActivity().getIntent().getStringExtra(USER_USERNAME);
    Log.d(TAG, String.format("ProfileActivity is called for Screenname %s", get_username));
    imageLoader = new ImageLoader(getActivity().getBaseContext());
    mLoadTask = new LoadProfile();
    mLoadTask.execute();

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // TODO Auto-generated method stub

    View v = inflater.inflate(R.layout.fragment_user_profile, null);

    // initialize
    tvFullName = (TextView) v.findViewById(R.id.tvFullName);
    tvUserName = (TextView) v.findViewById(R.id.tvUserName);
    tvAboutMe = (TextView) v.findViewById(R.id.tvAboutMe);
    tvStalk = (TextView) v.findViewById(R.id.tvStalk);
    tvRev = (TextView) v.findViewById(R.id.tvRevCount);

    //
    // mProgressBar = (ProgressBar) v.findViewById(R.id.pbSpinner);
    mPhoto = (ImageView) v.findViewById(R.id.ibProfilePhoto);
    mPhoto.setClickable(false);

    mLogOut = (Button) v.findViewById(R.id.btnLogOut);
    mLogOut.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.d("LogOut Button", "Clicked");
        ParseUser.logOut();
        Intent i = new Intent(getActivity().getBaseContext(), MainActivity.class);
        startActivity(i);
        getActivity().finish();

      }
    });

    mSearch = (Button) v.findViewById(R.id.btnSearch);
    mSearch.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.d("Search Button", "Clicked");
        Intent i = new Intent(getActivity().getBaseContext(), SearchActivity.class);
        startActivity(i);

      }
    });

    mFollow = (Button) v.findViewById(R.id.btnFollow);

    return v;
  }

  class LoadProfile extends AsyncTask<Void, Void, User> {
    public LoadProfile() {
    }

    @Override
    protected User doInBackground(Void... params) {
      Log.v(TAG, "Getting profile " + get_username);

      try {
        listUser = UserDao.getUser(get_username, get_requestedUpId);
        Log.v(TAG, "Size of this list = " + listUser.size());
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(User result) {
      super.onPostExecute(result);
      user = listUser.get(0);
      isMe = user.getParseUser();
      userName = user.getUserName();
      fullName = user.getFullName();
      aboutMe = user.getAboutMe();
      tvFullName.setText(fullName);
      tvUserName.setText(userName);
      tvAboutMe.setText(aboutMe);
      // Setting Pic
      ParseFile pp = (ParseFile) user.getParseUser().getParseFile("profilePic");
      final String imageUrl = pp.getUrl();
      imageLoader.DisplayImage(imageUrl, mPhoto);

      if (isMe != User.getCurrentUser().getParseUser()) {
        mSearch.setVisibility(View.GONE);
        mLogOut.setVisibility(View.GONE);
      } else {
        mFollow.setVisibility(View.GONE);
      }
      mCountTask = new CountingTask();
      mCountTask.execute();
    }
  }

  class CountingTask extends AsyncTask<Void, Void, Activity> {
    public CountingTask() {
    }

    @Override
    protected Activity doInBackground(Void... params) {
      Log.v(TAG, "Count review for " + user.getFullName());

      if (user != null) {
        Log.v(TAG, "object = " + user.getObjectId());
        try {
          stalkCount = UserDao.getStalkCountById(isMe);
          revCount = UserDao.getRevCountById(isMe);
        } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } else {
        Log.v(TAG, "User Curr = " + user.getObjectId());
      }
      return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(Activity result) {
      super.onPostExecute(result);
      tvStalk.setText(getResources().getQuantityString(R.plurals.grubber_user_stalk, stalkCount,
          stalkCount));
      tvRev.setText(getResources().getQuantityString(R.plurals.grubber_user_review, revCount,
          revCount));
    }
  }

}
