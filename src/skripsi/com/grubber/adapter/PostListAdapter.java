package skripsi.com.grubber.adapter;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.image.ImageLoader;
import skripsi.com.grubber.model.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseFile;

public class PostListAdapter extends BaseAdapter {

  private static final String TAG = PostListAdapter.class.getSimpleName();
  // Declare variables
  Context context;
  LayoutInflater inflater;
  TextView tvUser;
  TextView tvRestName;
  TextView tvContent;
  RatingBar rbRate;
  RatingBar rbCash;
  TextView tvDate;
  ImageView profilePic;
  ImageLoader imageLoader;
  private List<Activity> mPost = null;

  private Bitmap ppBitmap;

  public PostListAdapter(Context context, List<Activity> postList) {

    this.context = context;
    this.mPost = postList;

    imageLoader = new ImageLoader(context);
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

  @Override
  public View getView(final int position, View view, ViewGroup parent) {
    // TODO Auto-generated method stub
    if (view == null) {
      LayoutInflater inflater = (LayoutInflater) this.context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.adapter_post_list, null);
    }

    // Locate text view in listview_post
    tvUser = (TextView) view.findViewById(R.id.username);
    tvRestName = (TextView) view.findViewById(R.id.restaurant);
    tvContent = (TextView) view.findViewById(R.id.review);
    rbRate = (RatingBar) view.findViewById(R.id.rating);
    rbCash = (RatingBar) view.findViewById(R.id.price);
    tvDate = (TextView) view.findViewById(R.id.date);
    profilePic = (ImageView) view.findViewById(R.id.profilePic);
    Log.d("Adapter", "Berhasil set holder");

    final Format formatter = new SimpleDateFormat("dd MMMM, yyyy");
    NumberFormat nm = NumberFormat.getNumberInstance();

    Log.v(TAG, "post = " + mPost.get(position).getCreatedBy().getUsername());
    ParseFile pp = (ParseFile) mPost.get(position).getCreatedBy().getParseFile("profilePic");
    final String imageUrl = pp.getUrl();
    // Set the results into TextView

    tvUser.setText(mPost.get(position).getCreatedBy().getUsername());
    tvRestName.setText(mPost.get(position).getRestName());
    tvContent.setText(mPost.get(position).getContent());
    rbRate.setRating((float) mPost.get(position).getRate());
    rbCash.setRating((float) mPost.get(position).getCash());
    tvDate.setText(formatter.format(mPost.get(position).getCreatedAt()));
    imageLoader.DisplayImage(imageUrl, profilePic);

    // Listen for ListView Item Click
    view.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        // Send single item click data to SingleItemView Class
        Intent intent = new Intent(context, skripsi.com.grubber.timeline.Comment.class);
        intent.putExtra("reviewId", mPost.get(position).getObjectId());
        intent.putExtra("username", mPost.get(position).getCreatedBy().getUsername());
        intent.putExtra("restaurant", mPost.get(position).getRestName());
        intent.putExtra("review", mPost.get(position).getContent());
        intent.putExtra("rating", mPost.get(position).getRate());
        intent.putExtra("price", mPost.get(position).getCash());
        intent.putExtra("date", formatter.format(mPost.get(position).getCreatedAt()));
        intent.putExtra("profilePic",
            ((ParseFile) mPost.get(position).getCreatedBy().getParseFile("profilePic")).getUrl());
        context.startActivity(intent);
      }
    });

    return view;
  }
}
