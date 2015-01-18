package skripsi.com.grubber.search;

import java.util.ArrayList;
import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.SearchListAdapter;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.User;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseException;

public class SearchActivity extends FragmentActivity {
  private final static String TAG = SearchActivity.class.getSimpleName();
  // List<Restaurant> mRest = null;

  int total = 0;
  List<User> userList;
  List<Activity> actList = null;
  String username;
  EditText etSearchUser;
  Button btnActionSearch;
  String searchKeyword;
  private ListView listView;
  private SearchListAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    userList = new ArrayList<User>();
    actList = new ArrayList<Activity>();

    // Initialize

    listView = (ListView) findViewById(R.id.lvSearch);
    etSearchUser = (EditText) findViewById(R.id.etSearchUser);
    etSearchUser.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub
      }

      @Override
      public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        searchKeyword = String.valueOf(s.toString());
      }
    });
    btnActionSearch = (Button) findViewById(R.id.btnActionSearch);
    btnActionSearch.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        userList.clear();
        actList.clear();
        new getData().execute();
      }
    });
  }

  class getData extends AsyncTask<Void, Void, List<User>> {

    @Override
    protected List<User> doInBackground(Void... params) {

      List<User> result = null;
      Log.v("TAG", "Retrieving People data");
      try {
        result = ActivityDao.searchByPeople(searchKeyword, User.getCurrentUser());
      } catch (ParseException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      if (result != null) {
        Log.v("TAG", "Populating ListView");
        userList.addAll(result);
        Log.v(TAG, "size of userList = " + userList.size());
      }

      // Function to search followed user
      try {
        List<Activity> result2 = null;
        Log.v("TAG", "Retrieving Act data");
        result2 = ActivityDao.searchFollowedUser(User.getCurrentUser());
        if (result2 != null) {
          Log.v("TAG", "Populating ListView");
          actList.addAll(result2);
          Log.v(TAG, "size of actList = " + actList.size());
        }
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
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
      mAdapter = new SearchListAdapter(getBaseContext(), actList, userList, null);
      if (mAdapter.isEmpty()) {
        Log.v(TAG, "EMPTY ADAPTER?");
      } else {
        Log.v(TAG, "Adapter not empty!");
      }
      listView.setAdapter(mAdapter);
      Log.v(TAG, "Finished");
    }
  }
}
