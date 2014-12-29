package skripsi.com.grubber.trending;

import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.TrendingAdapter;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.model.Restaurant;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseException;

public class TrendingCashFragment extends Fragment {
  private static final String TAG = TrendingCashFragment.class.getSimpleName();

  private TrendingAdapter mTrendAdapter;
  // these variables will survive ConfigChange
  private List<Restaurant> mRest = null;

  private SearchTrendRate searchRateTask;

  private ListView lvRate = null;
  // private ProgressBar mProgressBar = null;
  private boolean isSearch = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_trend_rate, container, false);
    lvRate = (ListView) view.findViewById(R.id.lvRate);
    searchRateTask = new SearchTrendRate();
    searchRateTask.execute();
    return view;
  }

  class SearchTrendRate extends AsyncTask<Void, Void, List<Restaurant>> {
    static final String SKIP_COUNT = "skipCount";
    private int skipCount;

    public SearchTrendRate(int skipCount) {
      this.skipCount = skipCount;
    }

    public SearchTrendRate() {
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
        result = ActivityDao.getTrendingByCash();
      } catch (ParseException e) {
        getView().post(new Runnable() {
          @Override
          public void run() {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("Something is wrong here");
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

      lvRate.setAdapter(mTrendAdapter);
    }
  }

}
