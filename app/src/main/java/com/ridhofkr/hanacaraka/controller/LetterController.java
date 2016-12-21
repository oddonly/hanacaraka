package com.ridhofkr.hanacaraka.controller;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ridhofkr.hanacaraka.HanacarakaActivity;
import com.ridhofkr.hanacaraka.R;
import com.ridhofkr.hanacaraka.model.Letters;

import java.io.InputStream;
import java.util.ArrayList;

public class LetterController {
	public ArrayList<Letters> letters;
	private MediaPlayer player;

	public LetterController() {
		player = new MediaPlayer();
	}

	public void letterImageRendering(View v, Letters card) {
		TextView title = (TextView) v.findViewById(R.id.cardinfotitle);
		title.setText(card.name);
		TextView aksara = (TextView) v.findViewById(R.id.cardinfoaksara);
		aksara.setText(card.letter);
		aksara.setTypeface(HanacarakaActivity.HANACARAKA_FONT);

		TextView pasangan = (TextView) v.findViewById(R.id.cardinfopasangan);
		TextView sandhangan = (TextView) v
				.findViewById(R.id.cardinfosandhangan);
		LinearLayout layout = (LinearLayout) v
				.findViewById(R.id.layoutpasangan);
		if (!card.couplingRule.equals("")) {
			layout.setVisibility(View.VISIBLE);
			pasangan.setText(card.coupling);
			pasangan.setTypeface(HanacarakaActivity.HANACARAKA_FONT);
			sandhangan.setText(card.couplingRule);
		} else {
			layout.setVisibility(View.GONE);
		}
		LinearLayout lexample = (LinearLayout) v
				.findViewById(R.id.layoutcontoh);
		TextView example = (TextView) v.findViewById(R.id.cardinfoexample);
		TextView exampleText = (TextView) v
				.findViewById(R.id.cardinfoexampletext);
		if (card.letterExample.equals("")) {
			lexample.setVisibility(View.GONE);
		} else {
			lexample.setVisibility(View.VISIBLE);
			example.setText(card.letterExample);
			example.setTypeface(HanacarakaActivity.HANACARAKA_FONT);
			exampleText.setText(card.wordExample);
		}
	}

	public void makeListOfLetter() {
		letters = HanacarakaActivity.DAO.loadLetters();
	}

	public void playMusic(Context c, String name) {
		int id = getSoundID(c, name);
		try {
			AssetFileDescriptor afd = c.getResources().openRawResourceFd(id);
			player.reset();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			player.prepare();
			player.start();
		} catch (Exception e) {
			Log.e("player", e.getMessage());
			e.printStackTrace();
		}
	}

	public View playAnimation(Context c, String name, int category) {
		InputStream stream = null;
		try {
			name = name.replace('/', '_');
			stream = c.getAssets().open(
					"animations/" + category + "/" + name + ".gif");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new GifView(c, stream);
	}

	private int getSoundID(Context c, String name) {
		return c.getResources().getIdentifier(name, "raw", c.getPackageName());
	}

	public class GifView extends View {
		private Movie movie;
		private InputStream is = null;
		private long movieStart;

		public GifView(Context context, InputStream stream) {
			super(context);

			this.is = stream;
			movie = Movie.decodeStream(is);
			movieStart = android.os.SystemClock.uptimeMillis();
			setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
		}

		protected void onDraw(Canvas canvas) {
			canvas.drawColor(Color.TRANSPARENT);
			super.onDraw(canvas);
			long now = android.os.SystemClock.uptimeMillis();
			int relTime = (int) ((now - movieStart) % movie.duration());
			movie.setTime(relTime);
			movie.draw(canvas, this.getWidth() / 4, this.getHeight() / 16);
			this.invalidate();
		}
	}
}
