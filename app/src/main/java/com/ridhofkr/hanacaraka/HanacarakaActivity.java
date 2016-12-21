package com.ridhofkr.hanacaraka;

import java.security.MessageDigest;

import com.ridhofkr.hanacaraka.controller.DataAccessObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class HanacarakaActivity extends Activity {
	public static Typeface HANACARAKA_FONT;
	public static DataAccessObject DAO;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */

		/* set layout to informationcard */
		setContentView(R.layout.main);

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.ridhofkr.hanacaraka", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String a = new String(
						Base64.encode(md.digest(), Base64.DEFAULT));
				Log.e("facebook_key_hash", a);
			}
		} catch (Exception e) {

		}

		HANACARAKA_FONT = Typeface.createFromAsset(getAssets(),
				"fonts/hanacaraka.ttf");
		DAO = new DataAccessObject(this);

		/* switch between activity if a button presses */
		Button icButton = (Button) findViewById(R.id.btninfocard);
		icButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						ListingLettersActivity.class);
				startActivity(intent);
			}
		});

		Button ruleButton = (Button) findViewById(R.id.btninforule);
		ruleButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						ListingRulesActivity.class);
				startActivity(intent);
			}
		});

		Button exerciseButton = (Button) findViewById(R.id.btnexercise);
		exerciseButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						ExerciseActivity.class);
				startActivity(intent);
			}
		});

		Button evalButton = (Button) findViewById(R.id.btnexam);
		evalButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						EvaluationActivity.class);
				startActivity(intent);
			}
		});

		Button aboutButton = (Button) findViewById(R.id.btnabout);
		aboutButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						AboutUsActivity.class);
				startActivity(intent);
			}
		});

		Button exitButton = (Button) findViewById(R.id.btnexit);
		exitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
}