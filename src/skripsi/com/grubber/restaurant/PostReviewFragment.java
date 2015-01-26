package skripsi.com.grubber.restaurant;

import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.RestaurantProfileAdapter;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.dao.RestaurantDao;
import skripsi.com.grubber.model.Restaurant;
import skripsi.com.grubber.model.User;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.parse.ParseException;

public class PostReviewFragment extends FragmentActivity {
  private final static String TAG = PostReviewFragment.class.getSimpleName();

  private ImageButton mPhoto = null;
  private byte[] photoBitmap = null;
  private Bitmap photoThumbnail = null;

  // to be filled by user
  private EditText mRestRev = null;
  private RatingBar mCashBar = null;
  private RatingBar mRateBar = null;
  private Button mPostBtn = null;
  private Button mCancelBtn = null;

  // temp to be filled by user
  private String content;
  private float cash;
  private float rate;

  private RestaurantProfileAdapter mAdapter;
  private String mRest;
  private Restaurant mDataRest;
  private String mStringRestName;
  private String mStringRestId;

  private PostReviewTask mPostReviewTask;

  private String mRestNameString;
  private int tempCountReviews = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_postreview);
    if (savedInstanceState == null) {
      Intent intent = getIntent();
      mStringRestId = intent.getStringExtra("restId");
      if (mStringRestId == null) {
        Log.v(TAG, "Bundle is null!");
      } else {
        try {
          mDataRest = RestaurantDao.getRestProfileById(mStringRestId);
        } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    mRestRev = (EditText) findViewById(R.id.etReviewBox);
    mCashBar = (RatingBar) findViewById(R.id.cashBar);
    mRateBar = (RatingBar) findViewById(R.id.ratingBar);
    mPostBtn = (Button) findViewById(R.id.btnPost);
    mPostBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.v(TAG, "Post review to server initialized!");
        getRestViewContent();
        mPostReviewTask = new PostReviewTask();
        mPostReviewTask.execute();
      }
    });
    mCancelBtn = (Button) findViewById(R.id.btnCancel);
    mCancelBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getBaseContext(), RestaurantActivity.class);
        intent.putExtra("restId", mStringRestId);
        startActivity(intent);
      }
    });

  }

  public void getRestView() {

    LayoutInflater inflater = LayoutInflater.from(this);
    View getView = inflater.inflate(R.layout.fragment_postreview, null);

  }

  public void getRestViewContent() {
    content = mRestRev.getText().toString();
    rate = mRateBar.getRating();
    cash = mCashBar.getRating();
    // mCountReviews.setText(tempCountReviews);
  }

  class PostReviewTask extends AsyncTask<Void, Object, Restaurant> {

    public PostReviewTask() {
    }

    @Override
    protected Restaurant doInBackground(Void... params) {
      // first step to make sure it is sending
      boolean send = false;
      try {
        if (content != null && mDataRest != null && cash >= 0 && rate >= 0
            && mDataRest.getName() != null && mDataRest.getObjectId() != null) {
          ActivityDao.createNewPost(content, User.getCurrentUser(), mDataRest, cash, rate);
          send = true;
          Log.v(TAG, "Send = " + send);
        } else {
          send = false;
          Log.v(TAG, "Content = " + content);
          Log.v(TAG, "Rest = " + mDataRest.getName());
          Log.v(TAG, "Cash = " + cash);
          Log.v(TAG, "Rate = " + rate);
          Log.v(TAG, "Name = " + mDataRest.getName());
          Log.v(TAG, "RestId = " + mDataRest.getObjectId());

        }
      } catch (Exception e) {
        Log.w(TAG, "Problem loading profile", e);
        e.printStackTrace();
      }
      return mDataRest;
    }

    @Override
    protected void onPostExecute(Restaurant result) {
      super.onPostExecute(result);
      Log.v(TAG, "Finished task!");
      Intent intent = new Intent(getBaseContext(), RestaurantActivity.class);
      intent.putExtra("restId", mStringRestId);
      startActivity(intent);
    }
  }

}