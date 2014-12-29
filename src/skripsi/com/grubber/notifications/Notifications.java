package skripsi.com.grubber.notifications;

import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.NotificationListAdapter;
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

import com.parse.ParseException;

public class Notifications extends Fragment {

  protected static final String TAG = Notifications.class.getSimpleName();

  public static final String USER_OBJECT_ID = "objectId";
  public static final String USER_USERNAME = "username";
  ListView listview;
  List<Activity> mResult;
  ProgressDialog progressDialog;
  NotificationListAdapter mAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    View view = inflater.inflate(R.layout.fragment_notifications, container, false);
    new RemoteDataTask().execute();
    return view;
  }

  private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      // Create a progress dialog
      progressDialog = new ProgressDialog(getActivity());
      // Set progress dialog title
      progressDialog.setTitle("Processing Notifications");
      // Set progress dialog message
      progressDialog.setMessage("Loading...");
      progressDialog.setIndeterminate(false);
      // Show progress dialog
      progressDialog.show();

    }

    @Override
    protected Void doInBackground(Void... params) {
      // TODO Auto-generated method stub
      try {
        mResult = ActivityDao.getNotifications(User.getCurrentUser(), "no");

      } catch (ParseException e) {
        Log.v(TAG, "Failed to get post list for current user");
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {

      // Locate the listview in listview_timeline.xml
      listview = (ListView) getView().findViewById(R.id.list_notif);
      // Pass the result to ListViewAdapter.java
      mAdapter = new NotificationListAdapter(getActivity(), mResult);
      Log.d("Adapter", "Sukses");
      // Binds the adapter to ListView
      listview.setAdapter(mAdapter);
      // Close the progress dialog
      progressDialog.dismiss();
    }
  }
}
