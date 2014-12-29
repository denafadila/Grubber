package skripsi.com.grubber.timeline;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import skripsi.com.grubber.MainActivity;
import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.CommentListAdapter;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.image.ImageLoader;
import skripsi.com.grubber.model.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Comment extends Activity {

  /* Declare variables */
  protected static final String TAG = MainActivity.class.getSimpleName();

  /* Comment View */
  String reviewId;
  String userId;
  String username;
  String restaurant;
  String review;
  double rating;
  double price;
  String date;
  String profilePic;
  String commentStatus;

  TextView tvUser;
  TextView tvRestName;
  TextView tvContent;
  RatingBar rbRate;
  RatingBar rbCash;
  TextView tvDate;
  ImageView profileImage;
  ImageLoader imageLoader;

  /* Add Comment */
  EditText editComment;
  Button sendBtn;

  ProgressDialog progressDialog;
  List<skripsi.com.grubber.model.Activity> commentResult;
  List<skripsi.com.grubber.model.Activity> commentCache;
  ListView commentlistview;
  skripsi.com.grubber.model.Activity activity;
  ParseUser puTargetUser;
  User uTargetUser = null;
  CommentListAdapter cAdapter;
  final String CACHE_COMMENT = "reviewId";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);

    setContentView(R.layout.single_post);

    Intent intent = getIntent();
    imageLoader = new ImageLoader(getBaseContext());
    reviewId = intent.getStringExtra("reviewId");
    Log.v(TAG, "Rev = " + reviewId);
    /*
     * username = intent.getStringExtra("username"); restaurant =
     * intent.getStringExtra("restaurant"); review = intent.getStringExtra("review"); rating =
     * intent.getDoubleExtra("rating", 0); price = intent.getDoubleExtra("price", 0); date =
     * intent.getStringExtra("date"); profilePic = intent.getStringExtra("profilePic");
     */
    commentStatus = intent.getStringExtra("commentStatus");

    tvUser = (TextView) findViewById(R.id.username);
    tvRestName = (TextView) findViewById(R.id.restaurant);
    tvContent = (TextView) findViewById(R.id.review);
    rbRate = (RatingBar) findViewById(R.id.rating);
    rbCash = (RatingBar) findViewById(R.id.price);
    tvDate = (TextView) findViewById(R.id.date);
    profileImage = (ImageView) findViewById(R.id.profilePic);

    activity = ActivityDao.getSinglePost(reviewId);
    puTargetUser = activity.getCreatedBy();
    uTargetUser = new User();
    uTargetUser.parseUser = puTargetUser;

    ParseFile pp = (ParseFile) activity.getCreatedBy().getParseFile("profilePic");
    final String imageUrl = pp.getUrl();
    Format formatter = new SimpleDateFormat("dd MMMM, yyyy");
    NumberFormat nm = NumberFormat.getNumberInstance();

    tvUser.setText(activity.getCreatedBy().getUsername().toString());
    tvRestName.setText(activity.getRestName());
    // tvRestName.setText(activity.getResoId().getName().toString());
    tvContent.setText(activity.getContent().toString());
    rbRate.setRating((float) activity.getRate());
    rbCash.setRating((float) activity.getCash());
    tvDate.setText(formatter.format(activity.getCreatedAt()));
    imageLoader.DisplayImage(imageUrl, profileImage);

    /*
     * tvUser.setText(username); tvRestName.setText(restaurant); tvContent.setText(review);
     * rbRate.setText(nm.format(rating)); rbCash.setText(nm.format(price)); tvDate.setText(date);
     * imageLoader.DisplayImage(profilePic, profileImage);
     */

    updateStatus();
    addNewComment();
    new CommentDataTask().execute();
  }

  public void addNewComment() {

    editComment = (EditText) findViewById(R.id.comment_text);
    sendBtn = (Button) findViewById(R.id.buttonSend);

    sendBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        String content = editComment.getText().toString();

        if (!content.isEmpty()) {
          ActivityDao.createNewComment(content, User.getCurrentUser(), "Unread", reviewId,
              uTargetUser);
          Log.d("Send Comment = ", "Succeed");
          updateStatus();
          new CommentDataTask().execute();
        } else {
          Log.d("Send Comment = ", "Failed");
          Toast msg = Toast.makeText(getBaseContext(), "Comment is empty", Toast.LENGTH_LONG);
          msg.show();
        }
      }
    });

  }

  public void updateStatus() {
    if (commentStatus == null) {
      Log.v(TAG, "CommentStatus is null!");
    } else if (commentStatus.equals("ReadAll")) {
      ActivityDao.updateCommentStatus(reviewId);
      Log.d("Update Status = ", "Sukses Update");
    } else {
      Log.v(TAG, "CommentStatus is whatttt!");
    }
  }

  private class CommentDataTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      // TODO Auto-generated method stub
      super.onPreExecute();

      progressDialog = new ProgressDialog(Comment.this);
      progressDialog.setTitle("Comment Activity");
      progressDialog.setMessage("Comment Loading");
      progressDialog.setIndeterminate(false);
      progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
      // TODO Auto-generated method stub
      try {
        Log.v("Comment", "Getting comment list for current review = success");
        commentResult = ActivityDao.getCommentList(reviewId);
        ParseObject.unpinAllInBackground(CACHE_COMMENT, commentResult, new DeleteCallback() {

          @Override
          public void done(ParseException e) {
            // TODO Auto-generated method stub
            ParseObject.pinAllInBackground(CACHE_COMMENT, commentResult);
            Log.d(TAG, "cache succeed");
          }
        });
      } catch (Exception e) {
        // TODO: handle exception
        Log.v("Comment", "Failed to get post list for current user");
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      // Locate the listview in listview_timeline.xml
      commentlistview = (ListView) findViewById(R.id.list_comment);
      // Pass the result to ListViewAdapter.java
      cAdapter = new CommentListAdapter(Comment.this, commentResult);
      Log.d("Adapter", "Sukses");
      // Binds the adapter to ListView
      commentlistview.setAdapter(cAdapter);
      // Close the progress dialog
      progressDialog.dismiss();
    }

  }

}
