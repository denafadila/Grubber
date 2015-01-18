package skripsi.com.grubber.restaurant;

import java.util.ArrayList;
import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.PostListAdapter;
import skripsi.com.grubber.adapter.RestaurantProfileAdapter;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.dao.RestaurantDao;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.Restaurant;
import skripsi.com.grubber.restaurant.RestaurantProfileActivity.FragmentChangeListener;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseException;

public class RestaurantProfileFragment extends Fragment implements FragmentChangeListener {
  private final static String TAG = RestaurantProfileFragment.class.getSimpleName();

  private ImageButton mPhoto = null;
  private byte[] photoBitmap = null;
  private Bitmap photoThumbnail = null;
  private ImageView mProfilePic = null;
  private TextView mRestName = null;
  private TextView mRestCity = null;
  private TextView mRestDesc = null;
  private TextView mTotalRev = null;
  private RatingBar mCashBar = null;
  private RatingBar mRateBar = null;
  private Button mPostBtn = null;
  Bundle restObject = new Bundle();

  private RestaurantProfileAdapter mAdapter;
  private PostListAdapter mPostListAdapter;
  private Restaurant mRest;
  private List<Activity> mAct;
  Restaurant mDataRest;
  ListView mListView;

  private LoadProfileTask mLoadProfileTask;
  private CountingTask mCountingTask;
  private LoadReviewTask mLoadReviewTask;

  private String mRestIdString;
  private int tempCountReviews = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub

    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      mRestIdString = (String) getActivity().getIntent().getSerializableExtra("restId");
      if (mRestIdString == null) {
        Log.v(TAG, "mRest is null again!");
      } else
        Log.v(TAG, "Bundle mRest = " + mRestIdString);
    } else {
      mRestIdString = (String) savedInstanceState.getSerializable("restId");
    }

    mCountingTask = new CountingTask();
    mCountingTask.execute();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_restprofile, container, false);
    return view;
  }

  public void getRestView() {
    mProfilePic = (ImageView) getView().findViewById(R.id.ibProfilePhoto);
    mRestName = (TextView) getView().findViewById(R.id.tvRestName);
    mRestCity = (TextView) getView().findViewById(R.id.tvRestCity);
    mRestDesc = (TextView) getView().findViewById(R.id.tvRestDesc);
    mCashBar = (RatingBar) getView().findViewById(R.id.rbCash);
    mRateBar = (RatingBar) getView().findViewById(R.id.rbRate);
    mTotalRev = (TextView) getView().findViewById(R.id.tvTotalReview);
    mPostBtn = (Button) getView().findViewById(R.id.btnPostReview);
    mPostBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        showOtherFragment();
      }
    });

    mListView = (ListView) getView().findViewById(R.id.lvRestReview);
  }

  public void setRestView(Restaurant rest) throws ParseException {
    getRestView();
    // Bitmap bm;
    // bm = BitmapFactory.decodeByteArray(rest.getPhotoThumbnail().getData(), 0, rest
    // .getPhotoThumbnail().getData().length);
    // mProfilePic.setImageBitmap(bm);
    mRestName.setText(rest.getName());
    mRestCity.setText(rest.getCity());
    mRestDesc.setText(rest.getDesc());
    mRateBar.setRating(rest.getStar());
    mCashBar.setRating(rest.getCash());
    mTotalRev.setText(getResources().getQuantityString(R.plurals.grubber_restaurant_reviews,
        tempCountReviews, tempCountReviews));
  }

  public void showOtherFragment() {

    restObject.putSerializable("RestData", mDataRest);
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    PostReviewFragment fragment3 = new PostReviewFragment();
    // fragmentTransaction.replace(R.id.content_frame, fragment3);
    // fragment3.setArguments(restObject);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

  public void sendNewReview(Restaurant rest) {

  }

  class LoadProfileTask extends AsyncTask<Void, Object, Restaurant> {

    public LoadProfileTask() {
    }

    @Override
    protected Restaurant doInBackground(Void... params) {
      Restaurant up = null;
      try {
        // first step is load user Profile
        Log.v(TAG, "Sending " + mRestIdString + " away!");
        up = RestaurantDao.getRestProfileById(mRestIdString);
        mDataRest = up;

        if (mDataRest == null) {
          Log.v(TAG, "mData = Null!");
        } else {
          Log.v(TAG, "mData = " + mDataRest.getName());
        }
        if (up != null) {
          // publish the progress
          publishProgress(up);
          // ParseFile pf = up.getPhotoThumbnail();
          // if (pf != null) {
          // // if there is profile picture, publish it
          // publishProgress(pf);
          // }
        }
      } catch (Exception e) {
        Log.w(TAG, "Problem loading profile", e);
        e.printStackTrace();
      }
      return up;
    }

    @Override
    protected void onPostExecute(Restaurant result) {
      super.onPostExecute(result);
      mAdapter = new RestaurantProfileAdapter(getActivity().getBaseContext(), mDataRest);
      if (mAdapter.isEmpty()) {
        Log.v(TAG, "EMPTY ADAPTER?");
      } else {
        Log.v(TAG, "Adapter not empty!");
      }
      // mListView.setAdapter(mAdapter);
      if (mDataRest != null) {
        try {
          setRestView(mDataRest);
        } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } else {
        Log.v(TAG, "mDataRest = null!");
      }
      mLoadReviewTask = new LoadReviewTask();
      mLoadReviewTask.execute();
    }
  }

  class CountingTask extends AsyncTask<Void, Void, Activity> {
    public CountingTask() {
    }

    @Override
    protected Activity doInBackground(Void... params) {
      try {
        Log.v(TAG, "Count review for " + mRestIdString);
        tempCountReviews = ActivityDao.getRevCount(mRestIdString);
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
    protected void onPostExecute(Activity result) {
      super.onPostExecute(result);
      mLoadProfileTask = new LoadProfileTask();
      mLoadProfileTask.execute();
    }
  }

  class UpdateStarRate extends AsyncTask<Void, Void, Activity> {
    public UpdateStarRate() {
    }

    double star, rate;

    @Override
    protected Activity doInBackground(Void... params) {
      try {
        Log.v(TAG, "Count review for " + mRestIdString);
        tempCountReviews = ActivityDao.getRevCount(mRestIdString);
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Activity result) {
      super.onPostExecute(result);
      mLoadProfileTask = new LoadProfileTask();
      mLoadProfileTask.execute();
    }
  }

  class LoadReviewTask extends AsyncTask<Void, Object, List<Activity>> {

    public void LoadProfileTask() {
    }

    @Override
    protected List<Activity> doInBackground(Void... params) {
      List<Activity> up;
      up = new ArrayList<Activity>();
      try {
        // first step is load user reviews
        Log.v(TAG, "Sending " + mRestIdString + " away to get review!");
        up = ActivityDao.getRestReviews(mRestIdString);
        Log.v(TAG, "Size up = " + up.size());
        mAct = up;
        if (up == null) {
          Log.v(TAG, "up = Null!");
        } else {
          Log.v(TAG, "up = " + up.size());
        }
        if (up != null) {
          // publish the progress
          publishProgress(up);
          // ParseFile pf = up.getPhotoThumbnail();
          // if (pf != null) {
          // // if there is profile picture, publish it
          // publishProgress(pf);
          // }
        }
      } catch (Exception e) {
        Log.w(TAG, "Problem loading profile", e);
        e.printStackTrace();
      }
      return up;
    }

    @Override
    protected void onPostExecute(List<Activity> result) {
      super.onPostExecute(result);
      mPostListAdapter = new PostListAdapter(getActivity().getBaseContext(), mAct);
      if (mAdapter.isEmpty()) {
        Log.v(TAG, "EMPTY ADAPTER?");
      } else {
        Log.v(TAG, "Adapter not empty!");
      }
      // mListView.setAdapter(mAdapter);
      if (mDataRest != null) {
        mListView.setAdapter(mPostListAdapter);
      } else {
        Log.v(TAG, "mDataRest = null!");
      }
    }
  }

  @Override
  public void replaceFragment(Fragment fragment) {
    // TODO Auto-generated method stub

  }

}