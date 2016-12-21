package com.ridhofkr.hanacaraka;

import java.util.ArrayList;
import java.util.List;

import com.ridhofkr.hanacaraka.controller.EvaluationController;
import com.ridhofkr.hanacaraka.model.LetterProblem;
import com.ridhofkr.hanacaraka.model.Problem;
import com.ridhofkr.hanacaraka.model.WordProblem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EvaluationActivity extends Activity {
	private Animation in;
	private Animation out;
	private LinearLayout chooser;
	private LinearLayout readLetter;
	private LinearLayout writeLetter;
	private LinearLayout readWord;
	private LinearLayout sayWord;
	private TextView header;
	private GestureLibrary lib;
	private EvaluationController ctrl;
	private int mode;
	private int counter;
	public static String SCORE;
	public static String TIME;
	public static int LEVEL;
	private AlertDialog alert;
	private Handler timeHandler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */

		setContentView(R.layout.evaluation);

		lib = GestureLibraries.fromRawResource(this, R.raw.aksara);
		if (!lib.load()) {
			loadRawFailed();
			finish();
		}

		alert = new AlertDialog.Builder(this).create();

		header = (TextView) findViewById(R.id.headertext);
		header.setText("evaluasi");
		header.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.clipboard_edit, 0);

		chooser = (LinearLayout) findViewById(R.id.levelevaluation);

		readLetter = (LinearLayout) findViewById(R.id.evalreadletter);
		readLetter.setVisibility(View.GONE);

		writeLetter = (LinearLayout) findViewById(R.id.evalwriteletter);
		writeLetter.setVisibility(View.GONE);

		readWord = (LinearLayout) findViewById(R.id.evalreadword);
		readWord.setVisibility(View.GONE);

		sayWord = (LinearLayout) findViewById(R.id.evalsayword);
		sayWord.setVisibility(View.GONE);

		in = new AlphaAnimation(0.0f, 1.0f);
		in.setDuration(200);
		out = new AlphaAnimation(1.0f, 0.0f);
		out.setDuration(200);

		Button l1 = (Button) findViewById(R.id.levelone);
		l1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				ctrl = new EvaluationController(1);
				counter = -1;
				chooser.startAnimation(out);
				chooser.setVisibility(View.GONE);
				header.startAnimation(out);
				header.setText("level 1");
				header.startAnimation(in);
				ctrl.startTime();
				nextProblem();
				startTimer();
			}
		});

		Button l2 = (Button) findViewById(R.id.leveltwo);
		l2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ctrl = new EvaluationController(2);
				counter = -1;
				chooser.startAnimation(out);
				chooser.setVisibility(View.GONE);
				header.startAnimation(out);
				header.setText("level 2");
				header.startAnimation(in);
				ctrl.startTime();
				nextProblem();
				startTimer();
			}
		});

		Button l3 = (Button) findViewById(R.id.levelthree);
		l3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ctrl = new EvaluationController(3);
				counter = -1;
				chooser.startAnimation(out);
				chooser.setVisibility(View.GONE);
				header.startAnimation(out);
				header.setText("level 3");
				header.startAnimation(in);
				ctrl.startTime();
				nextProblem();
				startTimer();
			}
		});

		Button answer1 = (Button) findViewById(R.id.submitexercisereadletter);
		answer1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String answer = ((EditText) findViewById(R.id.readletteredit))
						.getText().toString().toLowerCase();
				Problem prob = ctrl.problems.get(counter);
				String right = "";
				if (prob instanceof LetterProblem) {
					right = ((LetterProblem) prob).answer.toLowerCase();
				} else if (prob instanceof WordProblem) {
					right = ((WordProblem) prob).answer.toLowerCase();
				}
				Log.e("exercise", answer + " and " + right);
				boolean result = ctrl.answerChecking(answer, right);
				if (result)
					ctrl.score++;
				nextProblem();
			}
		});

		Button answer2 = (Button) findViewById(R.id.submitexercisereadword);
		answer2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String answer = ((EditText) findViewById(R.id.readwordedit))
						.getText().toString().toLowerCase();
				Problem prob = ctrl.problems.get(counter);
				String right = "";
				if (prob instanceof LetterProblem) {
					right = ((LetterProblem) prob).answer.toLowerCase();
				} else if (prob instanceof WordProblem) {
					right = ((WordProblem) prob).answer.toLowerCase();
				}
				Log.e("exercise", answer + " and " + right);
				boolean result = ctrl.answerChecking(answer, right);
				if (result)
					ctrl.score++;
				nextProblem();
			}
		});

		Button answer3 = (Button) findViewById(R.id.submitexercisesayword);
		answer3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				checkRecognizer();
			}
		});

		GestureOverlayView gesture = (GestureOverlayView) findViewById(R.id.writelettergesture);
		gesture.addOnGesturePerformedListener(new OnGesturePerformedListener() {
			public void onGesturePerformed(GestureOverlayView gesture,
					Gesture in) {
				ArrayList<Prediction> pre = lib.recognize(in);
				String answer = "";
				Problem prob = ctrl.problems.get(counter);
				String right = "";
				if (prob instanceof LetterProblem) {
					right = ((LetterProblem) prob).answer.toLowerCase();
				} else if (prob instanceof WordProblem) {
					right = ((WordProblem) prob).answer.toLowerCase();
				}
				if (pre.size() > 0) {
					Prediction p = pre.get(0);
					if (p.score > 3.0) {
						answer = p.name;
					}
				}
				Log.e("exercise", answer + " and " + right);
				boolean result = ctrl.answerChecking(answer.toLowerCase(),
						right);
				if (result)
					ctrl.score++;
				nextProblem();
			}
		});
	}

	private void startTimer() {
		timeHandler = new Handler();
		timeHandler.postAtTime(new Runnable() {
			public void run() {
				if (ctrl == null) {

				} else {
					showDialog("Waktu habis",
							"Waktu masksimal untuk mengerjakan evaluasi habis",
							true);
				}
			}
		}, EvaluationController.MAX_TIME);
	}

	public void finish() {
		if (ctrl != null) {
			SCORE = ctrl.countScore();
			TIME = ctrl.getElapsedTime();
			LEVEL = ctrl.level;
			alert.dismiss();
			Intent intent = new Intent(this, SharingActivity.class);
			startActivity(intent);
		}
		super.finish();
	}

	private void checkRecognizer() {
		PackageManager pm = getPackageManager();
		List<ResolveInfo> act = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (act.size() != 0) {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ucapkan kata");
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
			startActivityForResult(intent, 1235);
		} else {
			showDialog("Gagal Recognize",
					"Tidak ada aplikasi Voice Recognition", false);
			// TODO kalau tidak ada aplikasi maunya gimana?
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1235 && resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			WordProblem p = (WordProblem) ctrl.problems.get(counter);
			boolean result = ctrl.recordAndRecognize(matches, p.answer);
			if (result)
				ctrl.score++;
			nextProblem();
		} else {
			showDialog("Gagal Recognize", "Tidak ada koneksi internet", false);
			// TODO kalau tidak ada internet maunya gimana?
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showDialog(String title, String msg, final boolean finish) {
		alert.setTitle(title);
		alert.setMessage(msg);
		alert.setButton("Kembali", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alert.dismiss();
				if (finish) {
					EvaluationActivity.this.finish();
				} else {
					nextProblem();
				}
			}
		});
		alert.show();
	}

	public void backButtonListener(View v) {
		this.onBackPressed();
	}

	@Override
	public void onBackPressed() {
		if (ctrl == null) {
			super.onBackPressed();
		} else {
			switch (mode) {
			case 1:
				readLetter.startAnimation(out);
				readLetter.setVisibility(View.GONE);
				break;
			case 2:
				writeLetter.startAnimation(out);
				writeLetter.setVisibility(View.GONE);
				break;
			case 3:
				readWord.startAnimation(out);
				readWord.setVisibility(View.GONE);
				break;
			case 4:
				sayWord.startAnimation(out);
				sayWord.setVisibility(View.GONE);
				break;
			}

			ctrl = null;
			header.startAnimation(out);
			chooser.setAnimation(in);
			chooser.setVisibility(View.VISIBLE);
			header.setText("evaluasi");
			header.startAnimation(in);
		}
	}

	private void loadRawFailed() {
		final AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle("Kesalahan!");
		alert.setMessage("Basis data gesture gagal dimuat. Silakan install ulang aplikasi.");
		alert.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				alert.dismiss();
			}
		});
	}

	private void changeProblem(int before, int after) {
		switch (before) {
		case 1:
			readLetter.startAnimation(out);
			readLetter.setVisibility(View.GONE);
			header.startAnimation(out);
			break;
		case 2:
			writeLetter.startAnimation(out);
			writeLetter.setVisibility(View.GONE);
			header.startAnimation(out);
			break;
		case 3:
			readWord.startAnimation(out);
			readWord.setVisibility(View.GONE);
			header.startAnimation(out);
			break;
		case 4:
			sayWord.startAnimation(out);
			sayWord.setVisibility(View.GONE);
			header.startAnimation(out);
			break;
		}
		switch (after) {
		case 1:
			readLetter.startAnimation(in);
			readLetter.setVisibility(View.VISIBLE);
			header.setText("baca huruf");
			header.startAnimation(in);
			mode = 1;
			break;
		case 2:
			writeLetter.startAnimation(in);
			writeLetter.setVisibility(View.VISIBLE);
			header.setText("tulis huruf");
			header.startAnimation(in);
			mode = 2;
			break;
		case 3:
			readWord.startAnimation(in);
			readWord.setVisibility(View.VISIBLE);
			header.setText("baca kata");
			header.startAnimation(in);
			mode = 3;
			break;
		case 4:
			sayWord.startAnimation(in);
			sayWord.setVisibility(View.VISIBLE);
			header.setText("ucap kata");
			header.startAnimation(in);
			mode = 4;
		}
	}

	private void nextProblem() {
		counter++;
		TextView text;
		if (counter == ctrl.problems.size()) { // jumlah soal
			this.finish();
			return;
		}
		Problem prob = ctrl.problems.get(counter);
		if (counter % 4 == 0) { // baca huruf
			EditText edit = (EditText) findViewById(R.id.readletteredit);
			edit.setText("");
			text = (TextView) findViewById(R.id.readlettertext);
			text.setText(((LetterProblem) prob).letter);
			text.setTypeface(HanacarakaActivity.HANACARAKA_FONT);
			text = (TextView) findViewById(R.id.categoryreadletter);
			switch (prob.category) {
			case 1:
				text.setText("Kategori: Aksara Dasar");
				break;
			case 2:
				text.setText("Kategori: Aksara Swara");
				break;
			case 3:
				text.setText("Kategori: Aksara Rekan");
				break;
			case 4:
				text.setText("Kategori: Aksara Murda");
				break;
			case 5:
				text.setText("Kategori: Aksara Wilangan");
				break;
			}
			changeProblem(mode, 1);
		} else if (counter % 4 == 1) { // tulis huruf
			text = (TextView) findViewById(R.id.writelettertext);
			text.setText("Tulis aksara \"" + ((LetterProblem) prob).answer
					+ "\"");
			changeProblem(mode, 2);
		} else if (counter % 4 == 2) { // baca kata
			EditText edit = (EditText) findViewById(R.id.readwordedit);
			edit.setText("");
			text = (TextView) findViewById(R.id.readwordtext);
			text.setText(((WordProblem) prob).word);
			text.setTypeface(HanacarakaActivity.HANACARAKA_FONT);
			changeProblem(mode, 3);
		} else { // ucap kata
			text = (TextView) findViewById(R.id.saywordtext);
			text.setText(((WordProblem) prob).word);
			text.setTypeface(HanacarakaActivity.HANACARAKA_FONT);
			changeProblem(mode, 4);
		}
	}
}
