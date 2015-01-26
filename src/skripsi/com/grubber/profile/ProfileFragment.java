package skripsi.com.grubber.profile;

import java.util.ArrayList;
import java.util.List;

import skripsi.com.grubber.MainActivity;
import skripsi.com.grubber.R;
import skripsi.com.grubber.RegisterFragment;
import skripsi.com.grubber.adapter.PostListAdapter;
import skripsi.com.grubber.adapter.SearchListAdapter;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.dao.UserDao;
import skripsi.com.grubber.image.ImageLoader;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.User;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment implements
    RegisterFragment.OnFragmentInteractionListener {
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

  private LinearLayout llsearchClick = null;
  private LinearLayout llSearchField = null;

  private ImageView btnHelp = null;
  private ImageView btnLogout = null;
  private ImageView btnUpdate = null;

  private TextView tvStalk = null;
  private TextView tvStalked = null;
  private TextView tvReview = null;

  private int stalkCount = 0;
  private int revCount = 0;
  private int stalkedCount = 0;
  private List<User> listUser = new ArrayList<User>();
  private User user;
  private ImageView mPhoto = null;
  private ImageLoader imageLoader = null;
  private String aboutMe = null;

  private Button mFollow;
  private Button mLogOut;

  private boolean isHidden = true;
  private EditText etSearch;
  private TextView tvShowSearch;
  private Button btnSearch;

  String get_requestedUpId;
  String get_username;
  private ParseUser isMe;
  private CountingTask mCountTask = null;
  private LoadUserReview mLoadReview = null;
  private LoadUserStalk mLoadStalk = null;
  private LoadUserStalked mLoadStalked = null;
  private LoadProfile mLoadTask = null;
  private searchTask mSearchTask = null;

  public ListView lvList;
  private List<Activity> mReviews = new ArrayList<Activity>();
  private List<ParseUser> mStalk = new ArrayList<ParseUser>();
  private List<ParseUser> mStalked = new ArrayList<ParseUser>();
  private List<User> searchResult = new ArrayList<User>();
  private PostListAdapter mAdapter;
  private SearchListAdapter mAdapter2;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setRetainInstance(true);

    // go to own profile
    if (getArguments() == null) {
      get_requestedUpId = User.getCurrentUser().getObjectId();
      Log.d(TAG, String.format("ProfileActivity is called for UserProfile %s", get_requestedUpId));
      get_username = User.getCurrentUser().getUserName();
      Log.d(TAG, String.format("ProfileActivity is called for Screenname %s", get_username));
    } else { // go to x's profile
      if (getArguments().getString("objectId").equals(User.getCurrentUser().getObjectId())) {
        get_requestedUpId = User.getCurrentUser().getObjectId();
        Log.d(TAG, String.format("ProfileActivity is called for UserProfile %s", get_requestedUpId));
        get_username = User.getCurrentUser().getUserName();
        Log.d(TAG, String.format("ProfileActivity is called for Screenname %s", get_username));
      } else {
        get_requestedUpId = getArguments().getString("objectId");
        Log.d(TAG, String.format("ProfileActivity is called for UserProfile %s", get_requestedUpId));
        get_username = getArguments().getString("userName");
        Log.d(TAG, String.format("ProfileActivity is called for Screenname %s", get_username));
      }
    }
    imageLoader = new ImageLoader(getActivity().getBaseContext());
    mLoadTask = new LoadProfile();
    mLoadTask.execute();

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // TODO Auto-generated method stub

    View v = inflater.inflate(R.layout.fragment_user_profile, null);

    lvList = (ListView) v.findViewById(R.id.lvUserProfile);
    lvList.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        Log.v(TAG, "Profile selected ->" + searchResult.get(position).getUserName());
        Bundle bundle = new Bundle();
        bundle.putString("objectId", searchResult.get(position).getObjectId());
        bundle.putString("userName", searchResult.get(position).getParseUser().getUsername());
        Fragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.tabcontent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
      }
    });
    // initialize
    tvShowSearch = (TextView) v.findViewById(R.id.showSearch);
    tvShowSearch.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        if (isHidden == true) {
          tvShowSearch.setTypeface(null, Typeface.NORMAL);
          isHidden = false;
          llSearchField.setVisibility(View.GONE);
        } else {
          tvShowSearch.setTypeface(null, Typeface.BOLD);
          isHidden = true;
          llSearchField.setVisibility(View.VISIBLE);
        }
      }
    });

    llsearchClick = (LinearLayout) v.findViewById(R.id.LLsearch);
    llsearchClick.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        if (isHidden == true) {
          tvShowSearch.setTypeface(null, Typeface.NORMAL);
          isHidden = false;
          llSearchField.setVisibility(View.GONE);
        } else {
          tvShowSearch.setTypeface(null, Typeface.BOLD);
          isHidden = true;
          llSearchField.setVisibility(View.VISIBLE);
        }
      }
    });
    llSearchField = (LinearLayout) v.findViewById(R.id.searchField);
    llSearchField.setVisibility(View.GONE);
    etSearch = (EditText) v.findViewById(R.id.ETsearch);
    btnSearch = (Button) v.findViewById(R.id.btnSearch);
    btnSearch.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        searchResult.clear();
        mReviews.clear();
        tvStalked.setTypeface(null, Typeface.NORMAL);
        tvStalk.setTypeface(null, Typeface.NORMAL);
        tvReview.setTypeface(null, Typeface.NORMAL);
        Toast.makeText(getActivity().getBaseContext(), etSearch.getText(), Toast.LENGTH_LONG)
            .show();
        mSearchTask = new searchTask();
        mSearchTask.execute();
      }
    });

    btnHelp = (ImageView) v.findViewById(R.id.Btnhelp);
    btnHelp.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Fragment fragment = new HelpFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.tabcontent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
      }
    });
    btnLogout = (ImageView) v.findViewById(R.id.BtnLogout);
    btnLogout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.d("LogOut Button", "Clicked");
        ParseUser.logOut();
        Intent i = new Intent(getActivity().getBaseContext(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
        Toast.makeText(getActivity().getBaseContext(), "Logged out", Toast.LENGTH_LONG).show();
      }
    });
    btnUpdate = (ImageView) v.findViewById(R.id.BtnUpdate);
    btnUpdate.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.d("Update Button", "Clicked");
        Bundle bundle = new Bundle();
        bundle.putBoolean("editMode", true);
        Fragment fragment = new RegisterFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.tabcontent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
      }
    });

    tvFullName = (TextView) v.findViewById(R.id.tvFullName);
    tvAboutMe = (TextView) v.findViewById(R.id.tvAboutMe);
    tvStalked = (TextView) v.findViewById(R.id.tvUserStalked);
    tvStalked.setText("Loading...");
    tvStalked.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mStalked.clear();
        tvStalked.setTypeface(null, Typeface.BOLD);
        tvStalk.setTypeface(null, Typeface.NORMAL);
        tvReview.setTypeface(null, Typeface.NORMAL);
        mLoadStalked = new LoadUserStalked();
        mLoadStalked.execute();
      }
    });
    tvStalk = (TextView) v.findViewById(R.id.tvUserStalk);
    tvStalk.setText("Loading...");
    tvStalk.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mStalk.clear();
        tvStalked.setTypeface(null, Typeface.NORMAL);
        tvStalk.setTypeface(null, Typeface.BOLD);
        tvReview.setTypeface(null, Typeface.NORMAL);
        mLoadStalk = new LoadUserStalk();
        mLoadStalk.execute();
      }
    });
    tvReview = (TextView) v.findViewById(R.id.tvUserReview);
    tvReview.setText("Loading...");
    tvReview.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        mReviews.clear();
        tvStalked.setTypeface(null, Typeface.NORMAL);
        tvStalk.setTypeface(null, Typeface.NORMAL);
        tvReview.setTypeface(null, Typeface.BOLD);
        mLoadReview = new LoadUserReview();
        mLoadReview.execute();
      }
    });

    //
    // mProgressBar = (ProgressBar) v.findViewById(R.id.pbSpinner);
    mPhoto = (ImageView) v.findViewById(R.id.ibProfilePhoto);
    mPhoto.setClickable(false);

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
      if (isAdded()) {
        user = listUser.get(0);
        isMe = user.getParseUser();
        userName = user.getUserName();
        fullName = user.getFullName();
        aboutMe = user.getAboutMe();
        tvFullName.setText(fullName);
        tvAboutMe.setText(aboutMe);
        // Setting Pic
        ParseFile pp = (ParseFile) user.getParseUser().getParseFile("profilePic");
        final String imageUrl = pp.getUrl();
        imageLoader.DisplayImage(imageUrl, mPhoto);

        mCountTask = new CountingTask();
        mCountTask.execute();

      }
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
          stalkedCount = UserDao.getStalkedCountById(isMe);
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
      if (isAdded()) {
        tvStalk.setText(getResources().getQuantityString(R.plurals.grubber_user_stalk, stalkCount,
            stalkCount));
        tvReview.setText(getResources().getQuantityString(R.plurals.grubber_user_review, revCount,
            revCount));
        tvStalked.setText(getResources().getQuantityString(R.plurals.grubber_user_stalked,
            stalkedCount, stalkedCount));

        tvReview.setTypeface(null, Typeface.BOLD);
        mLoadReview = new LoadUserReview();
        mLoadReview.execute();
      }
    }
  }

  class LoadUserReview extends AsyncTask<Void, Void, List<Activity>> {
    static final String SKIP_COUNT = "skipCount";
    private int skipCount;

    public LoadUserReview(int skipCount) {
      this.skipCount = skipCount;
    }

    public LoadUserReview() {
      this(0);
    }

    @Override
    protected void onPreExecute() {
      Log.v(TAG, "+++ LoadUserReview.onPreExecute() called! +++");
      super.onPreExecute();

    }

    @Override
    protected List<Activity> doInBackground(Void... params) {
      Log.v(TAG, "+++ LoadUserReview.doInBackground() called! +++");
      List<Activity> review = null;
      try {
        Log.v(TAG, "Getting data for user " + user.getUserName());
        review = ActivityDao.getUserRev(user);
      } catch (ParseException e) {
        getView().post(new Runnable() {
          @Override
          public void run() {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("Something is wrong while trying to query data");
            alert.show();
          }
        });
      }
      return review;
    }

    @Override
    protected void onPostExecute(List<Activity> review) {
      Log.v(TAG, "+++ LoadUserReview.onPostExecute() called! +++");
      if (isAdded()) {
        if (skipCount == 0) {
          mReviews = review;
        } else {
          if (review != null) {
            mReviews.addAll(review);
          }
        }
        // initialize UPA
        mAdapter = new PostListAdapter(getActivity().getBaseContext(), mReviews);
        lvList.setAdapter(mAdapter);
      }
    }
  }

  class LoadUserStalk extends AsyncTask<Void, Void, List<Activity>> {
    static final String SKIP_COUNT = "skipCount";
    private int skipCount;

    public LoadUserStalk(int skipCount) {
      this.skipCount = skipCount;
    }

    public LoadUserStalk() {
      this(0);
    }

    @Override
    protected void onPreExecute() {
      Log.v(TAG, "+++ LoadUserStalk.onPreExecute() called! +++");
      super.onPreExecute();

    }

    @Override
    protected List<Activity> doInBackground(Void... params) {
      Log.v(TAG, "+++ LoadUserStalk.doInBackground() called! +++");
      List<Activity> stalk = null;
      try {
        Log.v(TAG, "Getting data for user " + user.getUserName());
        stalk = ActivityDao.getUserStalk(user);
      } catch (ParseException e) {
        getView().post(new Runnable() {
          @Override
          public void run() {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("Something is wrong while trying to query data");
            alert.show();
          }
        });
      }
      return stalk;
    }

    @Override
    protected void onPostExecute(List<Activity> stalk) {
      Log.v(TAG, "+++ LoadUserStalk.onPostExecute() called! +++");
      if (isAdded()) {
        Log.v(TAG, "stalk = " + stalk.size());
        if (stalk != null) {
          for (int i = 0; i < stalk.size(); i++) {
            mStalk.add(i, stalk.get(i).getTargetUserProfile().getParseUser());
          }

        }
        // initialize UPA
        Log.v(TAG, "mStalk = " + mStalk.size());
        mAdapter2 = new SearchListAdapter(getActivity().getBaseContext(), null, null, mStalk, false);
        lvList.setAdapter(mAdapter2);
      }
    }
  }

  class LoadUserStalked extends AsyncTask<Void, Void, List<Activity>> {
    static final String SKIP_COUNT = "skipCount";
    private int skipCount;

    public LoadUserStalked(int skipCount) {
      this.skipCount = skipCount;
    }

    public LoadUserStalked() {
      this(0);
    }

    @Override
    protected void onPreExecute() {
      Log.v(TAG, "+++ LoadUserStalked.onPreExecute() called! +++");
      super.onPreExecute();

    }

    @Override
    protected List<Activity> doInBackground(Void... params) {
      Log.v(TAG, "+++ LoadUserStalked.doInBackground() called! +++");
      List<Activity> stalked = null;
      try {
        Log.v(TAG, "Getting data for user " + user.getUserName());
        stalked = ActivityDao.getUserStalked(user);
      } catch (ParseException e) {
        getView().post(new Runnable() {
          @Override
          public void run() {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("Something is wrong while trying to query data");
            alert.show();
          }
        });
      }
      return stalked;
    }

    @Override
    protected void onPostExecute(List<Activity> stalked) {
      Log.v(TAG, "+++ LoadUserStalked.onPostExecute() called! +++");
      if (isAdded()) {
        for (int i = 0; i < stalked.size(); i++) {
          mStalked.add(i, stalked.get(i).getCreatedBy());
        }

        // initialize UPA
        mAdapter2 = new SearchListAdapter(getActivity().getBaseContext(), null, null, mStalked,
            false);
        lvList.setAdapter(mAdapter2);
      }
    }
  }

  class searchTask extends AsyncTask<Void, Void, List<User>> {

    @Override
    protected List<User> doInBackground(Void... params) {

      List<User> result = null;
      Log.v("TAG", "Retrieving People data");
      try {
        Log.v(TAG, "Searching for " + etSearch.getText().toString() + " in database");
        result = ActivityDao.searchByPeople(etSearch.getText().toString(), User.getCurrentUser());
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      if (result != null) {
        List<User> temp = new ArrayList<User>();
        temp.clear();
        int index = 0;
        // process to remove self from search
        for (int i = 0; i < result.size(); i++) {
          if (result.get(i).getParseUser() != User.getCurrentUser().getParseUser()) {
            Log.v(TAG, "Inserted -> " + result.get(i).getUserName() + " -> " + result.get(i));
            temp.add(index++, result.get(i));
          }
        }

        Log.v("TAG", "Populating ListView");
        searchResult.addAll(temp);
        Log.v(TAG, "size of userList = " + searchResult.size());

        try {
          List<Activity> result2 = null;
          Log.v("TAG", "Retrieving Act data");
          result2 = ActivityDao.searchFollowedUser(User.getCurrentUser());
          if (result2 != null) {
            Log.v("TAG", "Populating ListView");
            mReviews.addAll(result2);
            Log.v(TAG, "size of actList = " + mReviews.size());
          }
        } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } else {
      }
      return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(List<User> result) {
      // TODO Auto-generated method stub
      super.onPostExecute(result);

      // Parameter userList menyimpan hasil query yang didapat dari
      // activityDao dan akan dikirim ke searchListAdapter
      if (result != null) {
        mAdapter2 = new SearchListAdapter(getActivity().getBaseContext(), mReviews, searchResult,
            null, true);
        lvList.setAdapter(mAdapter2);
        Log.v(TAG, "Finished");
      } else {
        new AlertDialog.Builder(getActivity()).setTitle("Search User").setMessage("User not found")
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                // continue with delete
              }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
      }
    }
  }

  @Override
  public void onSuccessfulRegistration() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onLoginAction(User user) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onSuccessfulEdit() {
    // TODO Auto-generated method stub

  }

}
