package com.ridhofkr.hanacaraka;

import java.util.ArrayList;

import com.ridhofkr.hanacaraka.model.Letters;
import com.ridhofkr.hanacaraka.controller.LetterController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListingLettersActivity extends Activity {
	public static String CURRENT_TEXT;
	public static int CURRENT_CATEGORY;
	private LetterController ctrl;
	private ArrayList<Letters> category;
	private Animation in;
	private Animation out;
	private LinearLayout chooser;
	private GridView grid;
	private TextView header;
	private boolean state;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */

		setContentView(R.layout.listinglettercard);

		header = (TextView) findViewById(R.id.headertext);
		header.setText("daftar huruf");
		header.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.text_size, 0);

		ctrl = new LetterController();
		ctrl.makeListOfLetter();

		chooser = (LinearLayout) findViewById(R.id.chooserlistinglettercard);
		grid = (GridView) findViewById(R.id.gridlistinglettercard);
		grid.setVisibility(View.GONE);
		state = true;

		in = new AlphaAnimation(0.0f, 1.0f);
		in.setDuration(200);
		out = new AlphaAnimation(1.0f, 0.0f);
		out.setDuration(200);

		Button dasar = (Button) findViewById(R.id.aksaradasar);
		dasar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showGrid(1);
			}
		});

		Button swara = (Button) findViewById(R.id.aksaraswara);
		swara.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showGrid(2);
			}
		});

		Button rekan = (Button) findViewById(R.id.aksararekan);
		rekan.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showGrid(3);
			}
		});

		Button murda = (Button) findViewById(R.id.aksaramurda);
		murda.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showGrid(4);
			}
		});

		Button wilangan = (Button) findViewById(R.id.aksarawilangan);
		wilangan.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				showGrid(5);
			}
		});
	}

	public void backButtonListener(View v) {
		this.onBackPressed();
	}
	
	public void onBackPressed() {
		if (state == false) {
			// TODO kembali ke menu utama
			state = true;
			category = null;
			grid.startAnimation(out);
			grid.setVisibility(View.GONE);
			grid.setAdapter(null);
			header.startAnimation(out);
			header.setText("daftar huruf");
			header.startAnimation(in);
			chooser.startAnimation(in);
			chooser.setVisibility(View.VISIBLE);
		} else {
			super.onBackPressed();
		}
	}

	private ArrayList<Letters> getLetters(int c) {
		ArrayList<Letters> cat = new ArrayList<Letters>();
		for (int i = 0; i < ctrl.letters.size(); i++) {
			if (ctrl.letters.get(i).category == c) {
				cat.add(ctrl.letters.get(i));
			}
		}
		return cat;
	}

	private void showGrid(int category) {
		state = false;
		this.category = getLetters(category);
		grid.setAdapter(new GridAdapter(this, this.category));

		chooser.startAnimation(out);
		chooser.setVisibility(View.GONE);
		header.startAnimation(out);
		switch (category) {
		case 1:
			header.setText("aksara dasar");
			break;
		case 2:
			header.setText("aksara swara");
			break;
		case 3:
			header.setText("aksara rekan");
			break;
		case 4:
			header.setText("aksara murda");
			break;
		case 5:
			header.setText("aksara wilangan");
			break;
		}
		header.startAnimation(in);
		grid.startAnimation(in);
		grid.setVisibility(View.VISIBLE);
	}

	public class GridAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<Letters> letters;

		public GridAdapter(Context context, ArrayList<Letters> letters) {
			this.context = context;
			this.letters = letters;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View gridView;
			if (convertView == null) {
				final Letters let = letters.get(position);
				gridView = new View(context);
				gridView = inflater.inflate(R.layout.listinglettercard_item,
						null);
				TextView text = (TextView) gridView.findViewById(R.id.gridtext);
				text.setText(let.name);
				Button btn = (Button) gridView.findViewById(R.id.gridaksara);
				btn.setText(let.letter);
				btn.setTypeface(HanacarakaActivity.HANACARAKA_FONT);
				btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CURRENT_CATEGORY = let.category;
						CURRENT_TEXT = let.name;
						Intent intent = new Intent(v.getContext(),
								LetterCardActivity.class);
						startActivity(intent);
					}
				});
			} else {
				gridView = (View) convertView;
			}
			return gridView;
		}

		public int getCount() {
			return letters.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
	}
}
