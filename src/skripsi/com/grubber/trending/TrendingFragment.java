package skripsi.com.grubber.trending;

import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.TrendingAdapter;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.model.Restaurant;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

public class TrendingFragment extends Fragment {
  public static final String TAG = TrendingFragment.class.getSimpleName();

  public TextView tvRate, tvCash, tvPop;
  public ListView lvList;
  private List<Restaurant> mRest = null;
  private TrendingAdapter mTrendAdapter;
  private String buttonPressed = "none";
  private TrendingTask trendTask;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub

    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    View view = inflater.inflate(R.layout.fragment_trending, container, false);

    lvList = (ListView) view.findViewById(R.id.lvTrending);
    tvRate = (TextView) view.findViewById(R.id.tvRate);
    tvRate.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        buttonPressed = "rate";
        tvRate.setTypeface(null, Typeface.BOLD);
        tvCash.setTypeface(null, Typeface.NORMAL);
        tvPop.setTypeface(null, Typeface.NORMAL);
        trendTask = new TrendingTask();
        trendTask.execute();
      }
    });
    tvCash = (TextView) view.findViewById(R.id.tvCash);
    tvCash.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        buttonPressed = "cash";
        tvRate.setTypeface(null, Typeface.NORMAL);
        tvCash.setTypeface(null, Typeface.BOLD);
        tvPop.setTypeface(null, Typeface.NORMAL);
        trendTask = new TrendingTask();
        trendTask.execute();
      }
    });
    tvPop = (TextView) view.findViewById(R.id.tvPop);
    tvPop.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        buttonPressed = "pop";
        tvRate.setTypeface(null, Typeface.NORMAL);
        tvCash.setTypeface(null, Typeface.NORMAL);
        tvPop.setTypeface(null, Typeface.BOLD);
        trendTask = new TrendingTask();
        trendTask.execute();
      }
    });

    return view;
  }

  class TrendingTask extends AsyncTask<Void, Void, List<Restaurant>> {
    static final String SKIP_COUNT = "skipCount";
    private int skipCount;

    public TrendingTask(int skipCount) {
      this.skipCount = skipCount;
    }

    public TrendingTask() {
      this(0);
    }

    @Override
    protected void onPreExecute() {
      Log.v(TAG, "+++ SearchPeople.onPreExecute() called! +++");
      super.onPreExecute();

    }

    @Override
    protected List<Restaurant> doInBackground(Void... params) {
      Log.v(TAG, "+++ SearchTrendRate.doInBackground() called! +++");
      List<Restaurant> result = null;
      try {
        if (buttonPressed.equals("rate")) {
          Log.v(TAG, "Getting Trending by Rate");
          result = ActivityDao.getTrendingByRate();
        } else if (buttonPressed.equals("cash")) {
          Log.v(TAG, "Getting Trending by Cash");
          result = ActivityDao.getTrendingByCash();
        } else if (buttonPressed.equals("pop")) {
          Log.v(TAG, "Getting Trending by Popularity");
          result = ActivityDao.getTrendingByPop();
        } else {
          Toast.makeText(getActivity().getBaseContext(), "Invalid Task", Toast.LENGTH_LONG).show();
        }
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
      return result;
    }

    @Override
    protected void onPostExecute(List<Restaurant> result) {
      Log.v(TAG, "+++ SearchTrendRate.onPostExecute() called! +++");
      if (skipCount == 0) {
        mRest = result;
      } else {
        if (result != null) {
          mRest.addAll(result);
        }
      }
      // initialize UPA
      mTrendAdapter = new TrendingAdapter(getActivity().getBaseContext(),
          R.layout.adapter_trending, mRest);
      lvList.setAdapter(mTrendAdapter);
    }
  }

}
