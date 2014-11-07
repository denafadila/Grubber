package skripsi.com.grubber.timeline;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

public class Timeline extends Fragment {

	/*ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<ReviewList> review_list = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.listview_timeline);
        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
    }
 
    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(Timeline.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parse.com Custom ListView Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
        	review_list = new ArrayList<ReviewList>();
            try {
                // Locate the class table named "Country" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Country");
                // Locate the column named "ranknum" in Parse.com and order list
                // by ascending
                query.orderByAscending("ranknum");
                ob = query.find();
                for (ParseObject reviews : ob) {
                    ReviewList item = new ReviewList();
                    item.setUsername((String) reviews.get("username"));
                    item.setRestaurant((String) reviews.get("restaurant"));
                    item.setReview((String) reviews.get("review"));
                    item.setRating(Integer.parseInt((String) reviews.get("rating")));
                    item.setPrice(Integer.parseInt((String) reviews.get("price")));
                    item.setDate((String) reviews.get("date"));
                    
                    review_list.add(item);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(Timeline.this,
                    review_list);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }*/
	
}
