package com.ridhofkr.hanacaraka;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class AboutUsActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */

		setContentView(R.layout.aboutus);

		TextView text = (TextView) findViewById(R.id.headertext);
		text.setText("tentang aplikasi");
		text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.question,
				0);
	}

	public void backButtonListener(View v) {
		super.onBackPressed();
	}
}
