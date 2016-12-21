package com.ridhofkr.hanacaraka;

import java.util.ArrayList;

import com.ridhofkr.hanacaraka.model.Rules;
import com.ridhofkr.hanacaraka.controller.RuleController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListingRulesActivity extends Activity {
	public static String CURRENT_RULE;
	private RuleController ctrl;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */

		setContentView(R.layout.listingrulecard);

		TextView text = (TextView) findViewById(R.id.headertext);
		text.setText("daftar aturan");
		text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.book_open_writing, 0);

		ctrl = new RuleController();
		ctrl.makeListOfRule();

		ListingRuleAdapter adapter = new ListingRuleAdapter(this,
				R.layout.listingrulecard_item, ctrl.rules);
		
		ListView lv = (ListView) findViewById(R.id.listingrulecard);
		lv.setAdapter(adapter);
	}

	public void backButtonListener(View v) {
		super.onBackPressed();
	}

	private class ListingRuleAdapter extends ArrayAdapter<Rules> {
		private ArrayList<Rules> data;
		private Context context;
		private int layoutResourceId;

		public ListingRuleAdapter(Context c, int l, ArrayList<Rules> data) {
			super(c, l, data);
			this.data = data;
			this.context = c;
			this.layoutResourceId = l;
		}

		@Override
		public View getView(int p, View c, ViewGroup a) {
			LRListHolder alist;
			Rules ra = data.get(p);
			if (c == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				c = inflater.inflate(layoutResourceId, a, false);
				alist = new LRListHolder();
				alist.ruleName = (TextView) c.findViewById(R.id.lrtext);
				c.setTag(alist);
			} else {
				alist = (LRListHolder) c.getTag();
			}
			alist.ruleName.setText(ra.name);
			c.setOnClickListener(new ListingRuleListener(ra.name));
			return c;
		}
	}

	private class ListingRuleListener implements View.OnClickListener {
		private String text;

		public ListingRuleListener(String text) {
			this.text = text;
		}

		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), RuleCardActivity.class);
			CURRENT_RULE = this.text;
			startActivity(intent);
		}
	}

	static class LRListHolder {
		TextView ruleName;
	}
}
