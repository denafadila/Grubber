package skripsi.com.grubber.profile;

import skripsi.com.grubber.R;
import skripsi.com.grubber.adapter.UserProfileAdapter;
import skripsi.com.grubber.model.User;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class ProfileActivity extends FragmentActivity {

	private static final String TAG = ProfileActivity.class.getSimpleName();
	public static final String USER_OBJECT_ID = "objectId";
	public static final String USER_USERNAME = "username";
	private UserProfileAdapter mAdapter;
	private ViewPager mPager;
	private String mUserProfileObjectId;

	@Override
	protected void onCreate(Bundle savedInstaceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstaceState);
		setContentView(R.layout.profile_activity);


		String requestedUpId = getIntent().getStringExtra(USER_OBJECT_ID);
		Log.d(TAG, String.format(
				"ProfileActivity is called for UserProfile %s", requestedUpId));
		String userName = getIntent().getStringExtra(USER_USERNAME);
		Log.d(TAG, String.format("ProfileActivity is called for Screenname %s",
				userName));
		User currentUp = User.getCurrentUser();
		boolean isMe = currentUp.getObjectId().equals(requestedUpId)
				|| (currentUp.getUserName() != null && currentUp
						.getUserName().equalsIgnoreCase(userName));

		// initialize view pager
		mAdapter = new UserProfileAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		// MyProfile Tab
		Bundle profileBundle = new Bundle();
		profileBundle.putString(ProfileFragment.USER_ID, requestedUpId);
		profileBundle.putString(ProfileFragment.USER_USERNAME, userName);
		String profileTabTitle = isMe ? getResources().getString(
				R.string.grubber_profile_tab_myprofile)
				: getResources().getString(
						R.string.grubber_profile_tab_profile);

	}
}
