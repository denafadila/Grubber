package skripsi.com.grubber.profile;

import skripsi.com.grubber.MainActivity;
import skripsi.com.grubber.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
  private final static String TAG = ProfileFragment.class.getSimpleName();
  public static final String USER_ID = "userId";
  public static final String USER_USERNAME = "username";

  // Declare
  private String mUserId = null;
  private String userName = null;
  private String fullName = null;
  private TextView tvFullName = null;
  private TextView tvUserName = null;
  private TextView tvAboutMe = null;
  private ParseUser user;
  private ProgressBar mProgressBar;
  private ImageButton mPhoto = null;
  private String aboutMe = null;
  private ToggleButton mFollow;
  private Button mLogOut;

  private ParseUser isMe;
  private boolean isProfileLoaded = false;
  private boolean isCountLoaded = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);

    isMe = ParseUser.getCurrentUser();
    user = ParseUser.getCurrentUser();
    userName = user.getString("username").toString();
    fullName = user.getString("fullName").toString();
    aboutMe = user.getString("aboutMe").toString();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // TODO Auto-generated method stub

    View v = inflater.inflate(R.layout.fragment_user_profile, null);

    // initialize
    tvFullName = (TextView) v.findViewById(R.id.tvFullName);
    tvFullName.setText(fullName);
    tvUserName = (TextView) v.findViewById(R.id.tvUserName);
    tvUserName.setText(userName);
    tvAboutMe = (TextView) v.findViewById(R.id.tvAboutMe);
    tvAboutMe.setText(aboutMe);
    //
    // mProgressBar = (ProgressBar) v.findViewById(R.id.pbSpinner);
    mPhoto = (ImageButton) v.findViewById(R.id.ibProfilePhoto);
    mPhoto.setClickable(false);

    mLogOut = (Button) v.findViewById(R.id.btnLogOut);
    mLogOut.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.d("LogOut Button", "Clicked");
        ParseUser.logOut();
        Intent i = new Intent(getActivity().getBaseContext(), MainActivity.class);
        startActivity(i);
        getActivity().finish();

      }
    });
    mFollow = (ToggleButton) v.findViewById(R.id.btnFollow);
    Log.v(TAG, "isMe ->" + isMe);
    if (isMe == user) {
      mFollow.setVisibility(View.GONE);
    }
    return v;
  }
}
