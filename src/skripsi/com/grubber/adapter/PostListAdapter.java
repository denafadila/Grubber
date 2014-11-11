package skripsi.com.grubber.adapter;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.model.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PostListAdapter extends BaseAdapter {

  private static final String TAG = PostListAdapter.class.getSimpleName();
  // Declare variables
  Context context;
  LayoutInflater inflater;
  TextView tvUser;
  TextView tvRestName;
  TextView tvContent;
  TextView rbRate;
  TextView rbCash;
  TextView tvDate;
  private List<Activity> mPost = null;

  public PostListAdapter(Context context, List<Activity> postList) {

    this.context = context;
    this.mPost = postList;
  }

  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return mPost.size();
  }

  @Override
  public Activity getItem(int position) {
    // TODO Auto-generated method stub
    return mPost.get(position);
  }

  @Override
  public long getItemId(int position) {
    // TODO Auto-generated method stub
    return position;
  }

  public View getView(final int position, View view, ViewGroup parent) {
    // TODO Auto-generated method stub
    if (view == null) {
      LayoutInflater inflater = (LayoutInflater) this.context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.activity_post_list, null);
    }

    // Locate text view in listview_post
    tvUser = (TextView) view.findViewById(R.id.username);
    tvRestName = (TextView) view.findViewById(R.id.restaurant);
    tvContent = (TextView) view.findViewById(R.id.review);
    rbRate = (TextView) view.findViewById(R.id.rating);
    rbCash = (TextView) view.findViewById(R.id.price);
    tvDate = (TextView) view.findViewById(R.id.date);
    Log.d("Adapter", "Berhasil set holder");

    Format formatter = new SimpleDateFormat("dd MMMM, yyyy");
    NumberFormat nm = NumberFormat.getNumberInstance();

    Log.v(TAG, "post = " + mPost.get(position).getCreatedBy().getUsername());

    // Set the results into TextView
    tvUser.setText(mPost.get(position).getCreatedBy().getUsername());
    tvRestName.setText(mPost.get(position).getRestName());
    tvContent.setText(mPost.get(position).getContent());
    rbRate.setText(nm.format(mPost.get(position).getRate()));
    rbCash.setText(nm.format(mPost.get(position).getCash()));
    tvDate.setText(formatter.format(mPost.get(position).getCreatedAt()));

    return view;
  }
}
