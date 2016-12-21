package com.ridhofkr.hanacaraka.connector.facebook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.ridhofkr.hanacaraka.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;

public class FacebookApp {
	private static String HANACARAKA_ICON = "http://hanacaraka.googlecode.com/svn/res/icon.png";
	private Facebook fb = null;
	private Context context;
	private String[] permissions;
	private Handler handler;
	private Activity act;
	private SessionListener sessionListener = new SessionListener();
	private String username;

	public FacebookApp(String id, Activity act, Context context,
			String[] permissions) {
		this.fb = new Facebook(id);
		FacebookSession.restore(fb, context);
		addAuthListener(sessionListener);
		addLogoutListener(sessionListener);
		this.act = act;
		this.permissions = permissions;
		this.context = context;
		this.handler = new Handler();
		this.username = "";
	}

	public void login() {
		if (!fb.isSessionValid()) {
			fb.authorize(act, permissions, new LoginDialogListener());
		}
	}

	public void logout() {
		onLogoutBegin();
		AsyncFacebookRunner asynRunner = new AsyncFacebookRunner(this.fb);
		asynRunner.logout(context, new LogoutRequestListener());
	}
	
	public String getUsername() {
		return username;
	}
	
	public void retrieveUsername() {
		if (fb.isSessionValid()) {
			Bundle params = new Bundle();
			try {
				String response = fb.request("me", params, "GET");
				Log.e("Facebook username", response);
				username = response;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.e("Facebook username", "not logged in");
		}
	}

	public void postMessageOnWall(String msg) {
		if (fb.isSessionValid()) {
			Bundle params = new Bundle();
			params.putString("message", msg);
			params.putString("name", act.getString(R.string.app_action));
			params.putString("caption", act.getString(R.string.app_name));
			params.putString("description", act.getString(R.string.app_desc));
			params.putString("picture", HANACARAKA_ICON);
			params.putString("type", "link");
			params.putString("link",
					"http://www.facebook.com/HanacarakaProject");
			try {
				String response = fb.request("me/feed", params, "POST");
				System.out.println(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			login();
		}
	}

	private final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			onLoginSuccess();
		}

		public void onFacebookError(FacebookError e) {
			onLoginError(e.getMessage());
		}

		public void onError(DialogError e) {
			onLoginError(e.getMessage());
		}

		public void onCancel() {
			onLoginError("Action Canceled");
		}
	}

	public class LogoutRequestListener extends BaseRequestListener {
		public void onComplete(String response, final Object state) {
			handler.post(new Runnable() {
				public void run() {
					onLogoutFinish();
				}
			});
		}
	}

	private class SessionListener implements AuthListener, LogoutListener {
		public void onAuthSucceed() {
			FacebookSession.save(fb, context);
		}

		public void onLogoutBegin() {

		}

		public void onLogoutFinish() {
			FacebookSession.clear(context);

		}

		public void onAuthFail(String error) {

		}
	}

	public Facebook getFacebook() {
		return this.fb;
	}

	private static LinkedList<AuthListener> auths = new LinkedList<AuthListener>();
	private static LinkedList<LogoutListener> logouts = new LinkedList<LogoutListener>();

	public static void addAuthListener(AuthListener listener) {
		auths.add(listener);
	}

	public static void removeAuthListener(AuthListener listener) {
		auths.remove(listener);
	}

	public static void addLogoutListener(LogoutListener listener) {
		logouts.add(listener);
	}

	public static void removeLogoutListener(LogoutListener listener) {
		logouts.remove(listener);
	}

	public static void onLoginSuccess() {
		for (AuthListener listener : auths) {
			listener.onAuthSucceed();
		}
	}

	public static void onLoginError(String error) {
		for (AuthListener listener : auths) {
			listener.onAuthFail(error);
		}
	}

	public static void onLogoutBegin() {
		for (LogoutListener listener : logouts) {
			listener.onLogoutBegin();
		}
	}

	public static void onLogoutFinish() {
		for (LogoutListener listener : logouts) {
			listener.onLogoutFinish();
		}
	}

	public static interface AuthListener {
		public void onAuthSucceed();

		public void onAuthFail(String error);
	}

	public static interface LogoutListener {
		public void onLogoutBegin();

		public void onLogoutFinish();
	}

	public abstract class BaseRequestListener implements RequestListener {
		public void onFacebookError(FacebookError e, final Object state) {
			Log.e("Facebook", e.getMessage());
			e.printStackTrace();
		}

		public void onFileNotFoundException(FileNotFoundException e,
				final Object state) {
			Log.e("Facebook", e.getMessage());
			e.printStackTrace();
		}

		public void onIOException(IOException e, final Object state) {
			Log.e("Facebook", e.getMessage());
			e.printStackTrace();
		}

		public void onMalformedURLException(MalformedURLException e,
				final Object state) {
			Log.e("Facebook", e.getMessage());
			e.printStackTrace();
		}
	}

	public abstract class BaseDialogListener implements DialogListener {
		public void onFacebookError(FacebookError e) {
			e.printStackTrace();
		}

		public void onError(DialogError e) {
			e.printStackTrace();
		}

		public void onCancel() {

		}
	}
}
