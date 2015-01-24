package skripsi.com.grubber.adapter;

import java.util.List;

import skripsi.com.grubber.R;
import skripsi.com.grubber.dao.ActivityDao;
import skripsi.com.grubber.image.ImageLoader;
import skripsi.com.grubber.model.Activity;
import skripsi.com.grubber.model.User;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class SearchListAdapter extends BaseAdapter {
  private final static String TAG = SearchListAdapter.class.getSimpleName();

  public static final String USER_OBJECT_ID = "objectId";
  public static final String USER_USERNAME = "username";
  public TextView tvUserName, tvAbout;
  ImageLoader imageLoader;

  boolean isSearch = false;

  public ImageView ivProf;
  private ToggleButton btnFollowUser;
  private List<Activity> listAct;
  private List<User> listUser;
  private List<ParseUser> listParseUser;
  private Context context;

  // Perubahan 1.. Parameter diberi tambahan listUser sebagai hasil dari search follow pada class
  // ActivityDao
  public SearchListAdapter(Context context, List<Activity> listAct, List<User> listUser,
      List<ParseUser> listParseUser, boolean isSearch) {
    // TODO Auto-generated constructor stub
    this.context = context;
    this.listUser = listUser;
    this.listParseUser = listParseUser;
    this.listAct = listAct;
    this.isSearch = isSearch;
    imageLoader = new ImageLoader(context);
  }

  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    if (listUser != null)
      return listUser.size();
    else
      return listParseUser.size();
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
  public View getView(final int position, View v, ViewGroup parent) {
    // TODO Auto-generated method stub

    if (v == null) {
      LayoutInflater inflater = (LayoutInflater) this.context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      v = inflater.inflate(R.layout.adapter_userlist, null);
    }

    ivProf = (ImageView) v.findViewById(R.id.profilePic);
    tvUserName = (TextView) v.findViewById(R.id.username);
    tvAbout = (TextView) v.findViewById(R.id.aboutme);
    btnFollowUser = (ToggleButton) v.findViewById(R.id.btnfollow);

    // if listAct != null && listUser == null && search == true = search for self
    // if listAct != null || search == false = open userProfile from notif, stalk/leave tab
    // if listAct != null || search == true = open userProfile from search

    if (listUser == null) {
      if (listAct != null || isSearch == false) {
        ParseFile pp = (ParseFile) listParseUser.get(position).getParseFile("profilePic");
        final String imageUrl = pp.getUrl();
        imageLoader.DisplayImage(imageUrl, ivProf);
        tvUserName.setText(listParseUser.get(position).getUsername());
        tvAbout.setText(listParseUser.get(position).getString("aboutMe"));
      } else {
        tvUserName.setText("No user found");
      }
    } else if (listAct != null && listUser == null && isSearch == true) {
      new AlertDialog.Builder(context).setTitle("Search User").setMessage("User not found")
          .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              // continue with delete
            }
          }).setIcon(android.R.drawable.ic_dialog_alert).show();

    } else {
      ParseFile pp = (ParseFile) listUser.get(position).getPhoto();
      final String imageUrl = pp.getUrl();

      imageLoader.DisplayImage(imageUrl, ivProf);
      tvUserName.setText(listUser.get(position).getUserName());

      tvAbout.setText(listUser.get(position).getAboutMe());

      if (listAct != null) {
        for (int i = 0; i < listAct.size(); i++) {
          // Log.v(
          // TAG,
          // "#" + i + ". " + listAct.get(i).getObjectId() + " Is "
          // + listAct.get(i).getTargetParseUserProfile().toString() + " & "
          // + listUser.get(position).getParseUser().toString() + " the same ?");
          if (listAct.get(i).getTargetParseUserProfile() == listUser.get(position).getParseUser()) {
            Log.v(TAG, "Found followed!");
            btnFollowUser.setChecked(true);
          } else {
            Log.v(TAG, "Not Found followed!");
          }
        }

      } else {
        Log.v(TAG, "Called by User Profile");
      }
    }

    if (btnFollowUser.isChecked() == true) {
      btnFollowUser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub
          Log.v(TAG, "Unfollow Succeed");
          List<ParseObject> toUnfollow = null;
          try {
            toUnfollow = ActivityDao.getFollowPeopleToRemove(listUser.get(position),
                User.getCurrentUser());
          } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          try {
            ParseObject.deleteAll(toUnfollow);
            Log.v(TAG, "Unfollow After Succeed");
            Toast.makeText(context, "Unfollowed", Toast.LENGTH_LONG).show();
          } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      });
    } else if (btnFollowUser.isChecked() == false) {
      btnFollowUser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // TODO Auto-generated method stub
          Log.v(TAG, "Follow Succeed");
          ActivityDao.createNewFollow(listUser.get(position), User.getCurrentUser(), "Unread");
          Log.v(TAG, "Follow After Succeed");
          Toast.makeText(context, "Followed", Toast.LENGTH_LONG).show();
        }
      });
    }
    return v;
  }
}
