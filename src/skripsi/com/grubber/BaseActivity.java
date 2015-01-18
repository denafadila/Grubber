package skripsi.com.grubber;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class BaseActivity extends ActionBarActivity {
	
	TextView title_app;
	ActionBar actionBar;
	View actionBar_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
	
	 	LayoutInflater inflater = LayoutInflater.from(this);
	 	actionBar_view = inflater.inflate(R.layout.action_bar, null);
	
	 	//Typeface font_style = Typeface.createFromAsset(getAssets(), "fonts/jaapokki_regular.ttf");
	 	title_app = (TextView) actionBar_view.findViewById(R.id.title_app);
	 	//title_app.setTypeface(font_style);
	 	
	
	 	
	}
	
	public void setTitle(String title)
	{
		title_app.setText(title);
	}
	
	public void show(){
		actionBar.setCustomView(actionBar_view);
	 	actionBar.setDisplayShowCustomEnabled(true);
	}
	
}
