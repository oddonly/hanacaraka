package com.ridhofkr.hanacaraka.connector.plurk;

import twitter4j.http.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PlurkSession {
	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String PLURK_AUTH_KEY = "auth_key";
	private static final String PLURK_AUTH_SECRET_KEY = "auth_secret_key";
	private static final String PLURK_USER_NAME = "user_name";
	private static final String SHARED = "Plurk_Preferences";

	public PlurkSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		editor = sharedPref.edit();
	}

	public void storeAccessToken(AccessToken accessToken, String username) {
		editor.putString(PLURK_AUTH_KEY, accessToken.getToken());
		editor.putString(PLURK_AUTH_SECRET_KEY, accessToken.getTokenSecret());
		editor.putString(PLURK_USER_NAME, username);
		editor.commit();
	}
	
	public void resetAccessToken() {
		editor.putString(PLURK_AUTH_KEY, null);
		editor.putString(PLURK_AUTH_SECRET_KEY, null);
		editor.putString(PLURK_USER_NAME, null);
		editor.commit();
	}
	
	public String getUsername() {
		return sharedPref.getString(PLURK_USER_NAME, "");
	}
	
	public AccessToken getAccessToken() {
		String token = sharedPref.getString(PLURK_AUTH_KEY, null);
		String tokenSecret = sharedPref.getString(PLURK_AUTH_SECRET_KEY, null);
		if (token != null && tokenSecret != null) {
			return new AccessToken(token, tokenSecret);
		} else {
			return null;
		}
	}
}
