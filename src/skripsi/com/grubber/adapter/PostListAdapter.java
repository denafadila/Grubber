package skripsi.com.grubber.adapter;

import java.text.NumberFormat;
import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.model.Restaurant;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class PostListAdapter extends BaseAdapter {

  public TextView tvRestName;
  public TextView tvRestCity;
  public RatingBar rbStar;
  public RatingBar rbCash;

  public TextView tvUser;
  public TextView tvContentUser;

  private List<Restaurant> rest;
  private Context context;

  public PostListAdapter(Context context, List<Restaurant> rest) {
    // TODO Auto-generated constructor stub
    this.context = context;
    this.rest = rest;
  }

  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return rest.size();
  }

  @Override
  public Object getItem(int position) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long getItemId(int position) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // TODO Auto-generated method stub
    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) this.context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.list_adapter, null);
    }
    Restaurant data = new Restaurant();
    NumberFormat nm = NumberFormat.getNumberInstance();

    tvRestName = (TextView) convertView.findViewById(R.id.mainLine);
    tvRestCity = (TextView) convertView.findViewById(R.id.secondLine);
    rbStar = (RatingBar) convertView.findViewById(R.id.fourthline);
    rbCash = (RatingBar) convertView.findViewById(R.id.fifthline);

    tvRestName.setText(rest.get(position).getName());
    tvRestCity.setText(rest.get(position).getCity());
    rbStar.setRating(rest.get(position).getStar());
    rbCash.setRating(rest.get(position).getCash());

    return convertView;
  }
}