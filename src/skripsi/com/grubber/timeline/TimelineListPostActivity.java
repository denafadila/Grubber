package skripsi.com.grubber.timeline;

import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.PostListAdapter;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.User;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class TimelineListPostActivity extends Fragment {

  protected static final String TAG = TimelineListPostActivity.class.getSimpleName();

  ListView listview;
  List<Activity> mResult;
  List<ParseUser> uResult;
  ProgressDialog timeline_progress;
  PostListAdapter mAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_postlist_timeline, container, false);
    new RemoteDataTask().execute();
    return view;
  }

  private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();

      // Create a progress dialog
      timeline_progress = new ProgressDialog(getActivity());
      // Set progress dialog title
      timeline_progress.setTitle("Processing Timeline");
      // Set progress dialog message
      timeline_progress.setMessage("Loading...");
      timeline_progress.setIndeterminate(false);
      // Show progress dialog
      timeline_progress.show();

    }

    @Override
    protected Void doInBackground(Void... params) {
      // TODO Auto-generated method stub
      try {
        Log.v(TAG, "Getting post list for current user = "
            + ParseUser.getCurrentUser().getUsername());
        final String CACHE_REVIEW = "reviewId";
        // mResult = ActivityDao.getPostList(ParseUser.getCurrentUser().toString());
        if (ActivityDao.getLocalPostList(User.getCurrentUser()).size() == 0) {
          Log.d("mResult", "!!!!getPostList");
          mResult = ActivityDao.getPostList(User.getCurrentUser());
        } else {
          Log.d("mResult", "!!!!getLocalPostList");
          mResult = ActivityDao.getLocalPostList(User.getCurrentUser());
        }
        ParseObject.unpinAllInBackground(CACHE_REVIEW, mResult, new DeleteCallback() {

          @Override
          public void done(ParseException e) {
            // TODO Auto-generated method stub
            ParseObject.pinAllInBackground(CACHE_REVIEW, mResult);
            Log.d(TAG, "cache succeed");
          }
        });
      } catch (ParseException e) {
        Log.v(TAG, "Failed to get post list for current user");
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {

      // Locate the listview in listview_timeline.xml
      listview = (ListView) getView().findViewById(R.id.list_timeline);
      // Pass the result to ListViewAdapter.java
      mAdapter = new PostListAdapter(getActivity(), mResult);
      Log.d("Adapter", "Sukses");
      // Binds the adapter to ListView
      listview.setAdapter(mAdapter);
      // Close the progress dialog
      timeline_progress.dismiss();
    }

  }

}
