package skripsi.com.grubber.adapter;


import skripsi.com.grubber.profile.ProfileFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class UserProfileAdapter extends FragmentPagerAdapter{

	public UserProfileAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = new ProfileFragment();
		
		switch(position){
		case 0 :
			fragment = new ProfileFragment();
			break;
		}

		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

}
