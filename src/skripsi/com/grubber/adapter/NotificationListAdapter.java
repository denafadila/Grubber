package skripsi.com.grubber.adapter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.parse.ParseFile;

import skripsi.com.grubber.R;
import skripsi.com.grubber.R.color;
import skripsi.com.grubber.image.ImageLoader;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.User;
import skripsi.com.grubber.timeline.TimelineActivity.RemoteDataTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationListAdapter extends BaseAdapter {
	
	private static final String TAG = NotificationListAdapter.class.getSimpleName();
	
	Context context;
	LayoutInflater inflater;
	List<Activity> listNotif = null;
	TextView tvUsername;
	TextView tvContent;
	TextView tvDate;
	ImageView profilePic;
	ImageLoader imageLoader;
	View notif_line;
	
	public NotificationListAdapter(Context context, List<Activity> listNotif) {
		
		this.context = context;
	    this.listNotif = listNotif;
	    imageLoader = new ImageLoader(context);
	    
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listNotif.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listNotif.get(position);
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
	      view = inflater.inflate(R.layout.adapter_notif, null);
	    }
		
		tvUsername = (TextView) view.findViewById(R.id.username);
		tvContent = (TextView) view.findViewById(R.id.content_info);
	    tvDate = (TextView) view.findViewById(R.id.date);
	    profilePic = (ImageView) view.findViewById(R.id.profilePic);
	    notif_line = (View) view.findViewById(R.id.notif_line);
	    Log.d("Adapter", "Berhasil set holder");
	    
	    final Format formatter = new SimpleDateFormat("dd MMM yyyy");
	    
	    Log.v(TAG, "post = " + listNotif.get(position).getCreatedBy().getUsername());
    
    
	    ParseFile pp = (ParseFile) listNotif.get(position).getCreatedBy().getParseFile("profilePic");
	    final String imageUrl = pp.getUrl();
	    
	    tvUsername.setText(listNotif.get(position).getCreatedBy().get("fullName").toString());
	    
	    //tvContent.setTextColor(color.abc_search_url_text_holo);
	    
	    if(listNotif.get(position).getType().equals("C")) {
	    	tvContent.setText("commented on your post");
	    	if(listNotif.get(position).getStatus().equals("Unread")) {
	    		tvContent.setTextColor(Color.parseColor("#2e2a2a"));
		    	notif_line.setBackgroundColor(Color.parseColor("#e3db00"));
		    }
	    }
	    else if (listNotif.get(position).getType().equals("S")) {
	    	tvContent.setText("stalked you!");
	    	if(listNotif.get(position).getStatus().equals("Unread")) {
	    		tvContent.setTextColor(Color.parseColor("#2e2a2a"));
	    		notif_line.setBackgroundColor(Color.parseColor("#1dc4a3"));
		    }
		}
	    
	    tvDate.setText(formatter.format(listNotif.get(position).getCreatedAt()));
	    imageLoader.DisplayImage(imageUrl, profilePic);
	    
	    view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = null;
				if(listNotif.get(position).getType().equals("C")) {
					intent = new Intent(context, skripsi.com.grubber.timeline.Comment.class);
					intent.putExtra("reviewId", listNotif.get(position).getReviewId());
					
					if(listNotif.get(position).getTargetUserProfile().getObjectId().equals(User.getCurrentUser().getObjectId())) {
						intent.putExtra("commentStatus", "Read");
						Log.d("commentStatus", "Read");
					}
					else {
						intent.putExtra("commentStatus", "Unread");
						Log.d("commentStatus", "Unread");
					}
			    }
				context.startActivity(intent);
				
			
			}
		});
	    
		return view;
	}

}
