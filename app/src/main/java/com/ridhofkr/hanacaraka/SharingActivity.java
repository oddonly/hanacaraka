package com.ridhofkr.hanacaraka;

import java.io.IOException;
import java.net.MalformedURLException;

import com.ridhofkr.hanacaraka.connector.facebook.FacebookApp;
import com.ridhofkr.hanacaraka.connector.plurk.PlurkApp;
import com.ridhofkr.hanacaraka.connector.plurk.PlurkApp.PDialogListener;
import com.ridhofkr.hanacaraka.connector.twitter.TwitterApp;
import com.ridhofkr.hanacaraka.connector.twitter.TwitterApp.TwDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SharingActivity extends Activity {
	private static final String CONSUMER_KEY = "6vzu0yCz7kkSMG1v4NNOg";
	private static final String CONSUMER_SECRET = "xbRMUQQMb0ookaLpbQ5MLHCC3dy54leY5ub9FAdcU";
	private static final String PCONSUMER_KEY = "MCwUENwG6n0I";
	private static final String PCONSUMER_SECRET = "qXTkVps6lyXNEfq8cBus3GCaQeQR7Uhs";
	private static final String FACEBOOK_APPID = "200681036708571";
	private static final String[] FACEBOOK_PERMISSION = { "publish_stream",
			"user_about_me" };
	private static final String TAG = "Hanacaraka";
	private static String sharingText;
	private TwitterApp twitter;
	private PlurkApp plurk;
	private FacebookApp facebook;
	private TextView header;
	private String score;
	private String time;
	private int level;
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (msg.arg1 == 1) {
					Toast.makeText(getBaseContext(),
							"Pencapaian berhasil dipublikasikan!",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getBaseContext(),
							"Pencapaian gagal dipublikasikan!",
							Toast.LENGTH_LONG).show();
				}
			} else if (msg.what == 2) {
				if (msg.arg1 == 1) {

				} else {
					Toast.makeText(getBaseContext(), "Login gagal!",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.evaluation_result);

		score = EvaluationActivity.SCORE;
		time = EvaluationActivity.TIME;
		level = EvaluationActivity.LEVEL;
		header = (TextView) findViewById(R.id.headertext);
		header.setText("sharing");
		header.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.clipboard_edit, 0);

		sharingText = "Saya telah menyelesaikan evaluasi pada level " + level
				+ " dalam " + time + " detik dengan nilai: " + score
				+ " - http://www.facebook.com/HanacarakaProject";

		String infoText = "Anda telah menyelesaikan evaluasi pada level "
				+ level + " dalam " + time + " detik dengan nilai: ";

		TextView timeResult = (TextView) findViewById(R.id.timeresult);
		timeResult.setText(infoText);
		TextView scoreResult = (TextView) findViewById(R.id.scoreresult);
		scoreResult.setText(score);

		facebook = new FacebookApp(FACEBOOK_APPID, this,
				getApplicationContext(), FACEBOOK_PERMISSION);
		twitter = new TwitterApp(this, CONSUMER_KEY, CONSUMER_SECRET);
		plurk = new PlurkApp(this, PCONSUMER_KEY, PCONSUMER_SECRET);

		Button sharefb = (Button) findViewById(R.id.sharefacebook);
		Button sharetwitter = (Button) findViewById(R.id.sharetwitter);
		Button shareplurk = (Button) findViewById(R.id.shareplurk);

		shareplurk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				plurk.setListener(pDialog);
				if (isNetworkAvailable()) {
					if (plurk.hasAccessToken()) {
						plurk.updateStatus(sharingText);
					} else {
						plurk.authorize();
					}
				} else {
					Log.e("network_state", "unavailable");
					showNetworkOptions();
				}
			}
		});

		sharetwitter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				twitter.setListener(twDialog);
				if (isNetworkAvailable()) {
					if (twitter.hasAccessToken()) {
						twitter.updateStatus(sharingText);
					} else {
						twitter.authorize();
					}
				} else {
					Log.e("network_state", "unavailable");
					showNetworkOptions();
				}
			}
		});

		sharefb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					postMessage();
				} else {
					Log.e("network_state", "unavailable");
					showNetworkOptions();
				}
			}
		});

		clearCredentials();
	}

	private void showNetworkOptions() {
		final AlertDialog ad = new AlertDialog.Builder(SharingActivity.this)
				.create();
		ad.setTitle("Informasi");
		ad.setMessage("Fitur ini membutuhkan koneksi internet.");
		ad.setButton("Batal", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				ad.dismiss();
			}
		});
		ad.setButton2("Aktifkan Internet",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.setClassName("com.android.phone",
								"com.android.phone.Settings");
						startActivity(intent);
					}
				});
		ad.show();
	}

	public void backButtonListener(View v) {
		super.onBackPressed();
		finish();
	}

	private TwDialogListener twDialog = new TwDialogListener() {
		public void onError(String value) {
			Log.e("twitter", value);
			handler.sendMessage(handler.obtainMessage(2, 0, 0));
		}

		public void onComplete(String value) {
			twitter.updateStatus(sharingText);
		}
	};

	private PDialogListener pDialog = new PDialogListener() {
		public void onError(String value) {
			Log.e("plurk", value);
			handler.sendMessage(handler.obtainMessage(2, 0, 0));
		}

		public void onComplete(String value) {
			plurk.updateStatus(sharingText);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		facebook.getFacebook().authorizeCallback(requestCode, resultCode, data);
	}

	public void postMessage() {
		if (facebook.getFacebook().isSessionValid()) {
			postMessageInThread();
		} else {
			FacebookApp.AuthListener listener = new FacebookApp.AuthListener() {
				public void onAuthSucceed() {
					postMessageInThread();
				}

				public void onAuthFail(String error) {
					handler.sendMessage(handler.obtainMessage(2, 0, 0));
				}
			};
			FacebookApp.addAuthListener(listener);
			facebook.login();
		}
	}

	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {
				try {
					facebook.postMessageOnWall(sharingText);
					handler.sendMessage(handler.obtainMessage(1, 1, 0));
				} catch (Exception e) {
					Log.e(TAG, "Error sending message", e);
					handler.sendMessage(handler.obtainMessage(1, 0, 0));
				}
			}
		};
		t.start();
	}

	private void clearCredentials() {
		try {
			facebook.getFacebook().logout(getApplicationContext());
			twitter.resetAccessToken();
			plurk.resetAccessToken();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo active = cm.getActiveNetworkInfo();
		return active != null;
	}
}