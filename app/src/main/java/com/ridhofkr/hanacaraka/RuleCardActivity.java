package com.ridhofkr.hanacaraka;

import java.util.ArrayList;

import com.ridhofkr.hanacaraka.controller.RuleController;
import com.ridhofkr.hanacaraka.model.Rules;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class RuleCardActivity extends Activity {
	private RuleController ctrl;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */

		setContentView(R.layout.rulecard);
		ctrl = new RuleController();

		TextView text = (TextView) findViewById(R.id.headertext);
		text.setText("informasi aturan");
		text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.information_circle, 0);

		ctrl.makeListOfRule();
		int p = 0;
		for (int i = 0; i < ctrl.rules.size(); i++) {
			if (ctrl.rules.get(i).name
					.equals(ListingRulesActivity.CURRENT_RULE))
				p = i;
		}
		IRPagerAdapter adapter = new IRPagerAdapter(ctrl.rules);
		ViewPager pager = (ViewPager) findViewById(R.id.irviewpager);
		pager.setAdapter(adapter);
		pager.setCurrentItem(p);
	}

	public void backButtonListener(View v) {
		super.onBackPressed();
	}

	private class IRPagerAdapter extends PagerAdapter {
		private ArrayList<Rules> cards;

		public IRPagerAdapter(ArrayList<Rules> cards) {
			this.cards = cards;
		}

		@Override
		public int getCount() {
			return cards.size();
		}

		public Object instantiateItem(View c, int p) {
			LayoutInflater inflater = (LayoutInflater) c.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.rulecard_item, null);
			ctrl.ruleImageRendering(view, cards.get(p));
			((ViewPager) c).addView(view, 0);
			return view;
		}

		@Override
		public void destroyItem(View v, int c, Object o) {
			((ViewPager) v).removeView((View) o);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}
