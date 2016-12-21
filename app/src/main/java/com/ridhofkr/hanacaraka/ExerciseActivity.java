package com.ridhofkr.hanacaraka;

import java.util.ArrayList;
import java.util.List;

import com.ridhofkr.hanacaraka.controller.ExerciseController;
import com.ridhofkr.hanacaraka.model.LetterProblem;
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
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
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

public class ExerciseActivity extends Activity {
	private Animation in;
	private Animation out;
	private LinearLayout chooser;
	private LinearLayout readLetter;
	private LinearLayout writeLetter;
	private LinearLayout readWord;
	private LinearLayout sayWord;
	private TextView header;
	private int mode;
	private GestureLibrary lib;
	private ExerciseController ctrl;
	private Integer[] randlproblems;
	private Integer[] randwproblems;
	private Integer[] randgproblems;
	private Integer[] randsproblems;
	private int clproblems;
	private int cwproblems;
	private int cgproblems;
	private int csproblems;
	private static final int MAX_EXERCISE = 10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* remove title bar and notification bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* end remove title bar and notification bar */

		setContentView(R.layout.exercise);

		lib = GestureLibraries.fromRawResource(this, R.raw.aksara);
		if (!lib.load()) {
			loadRawFailed();
			finish();
		}

		header = (TextView) findViewById(R.id.headertext);
		header.setText("latihan");
		header.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit, 0);

		chooser = (LinearLayout) findViewById(R.id.exercisechooser);
		readLetter = (LinearLayout) findViewById(R.id.exercisereadletter);
		readLetter.setVisibility(View.GONE);
		writeLetter = (LinearLayout) findViewById(R.id.exercisewriteletter);
		writeLetter.setVisibility(View.GONE);
		readWord = (LinearLayout) findViewById(R.id.exercisereadword);
		readWord.setVisibility(View.GONE);
		sayWord = (LinearLayout) findViewById(R.id.exercisesayword);
		sayWord.setVisibility(View.GONE);

		in = new AlphaAnimation(0.0f, 1.0f);
		in.setDuration(200);
		out = new AlphaAnimation(1.0f, 0.0f);
		out.setDuration(200);
		mode = 0;

		ctrl = new ExerciseController();

		Button readLetterBtn = (Button) findViewById(R.id.btnexercisereadletter);
		readLetterBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ctrl.makeListOfLetterProblem();
				randlproblems = ctrl.random(ctrl.lproblems.size());
				clproblems = 0;
				chooser.startAnimation(out);
				chooser.setVisibility(View.GONE);
				header.startAnimation(out);
				readLetter.startAnimation(in);
				readLetter.setVisibility(View.VISIBLE);
				header.setText("belajar baca huruf");
				header.startAnimation(in);
				mode = 1;
				nextProblem(true);
			}
		});

		Button writeLetterBtn = (Button) findViewById(R.id.btnexerciewriteletter);
		writeLetterBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ctrl.makeListOfGestureProblem();
				randgproblems = ctrl.random(ctrl.gproblems.size());
				cgproblems = 0;
				chooser.startAnimation(out);
				chooser.setVisibility(View.GONE);
				header.startAnimation(out);
				writeLetter.startAnimation(in);
				writeLetter.setVisibility(View.VISIBLE);
				header.setText("belajar tulis huruf");
				header.startAnimation(in);
				mode = 2;
				nextProblem(true);
			}
		});

		Button readWordBtn = (Button) findViewById(R.id.btnexercisereadword);
		readWordBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ctrl.makeListOfWordProblem();
				randwproblems = ctrl.random(ctrl.wproblems.size());
				cwproblems = 0;
				chooser.startAnimation(out);
				chooser.setVisibility(View.GONE);
				header.startAnimation(out);
				readWord.startAnimation(in);
				readWord.setVisibility(View.VISIBLE);
				header.setText("belajar baca kata");
				header.startAnimation(in);
				mode = 3;
				nextProblem(true);
			}
		});

		Button sayWordBtn = (Button) findViewById(R.id.btnexercisesayword);
		sayWordBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				ctrl.makeListOfSayProblem();
				randsproblems = ctrl.random(ctrl.sproblems.size());
				csproblems = 0;
				chooser.startAnimation(out);
				chooser.setVisibility(View.GONE);
				header.startAnimation(out);
				sayWord.startAnimation(in);
				sayWord.setVisibility(View.VISIBLE);
				header.setText("belajar ucap kata");
				header.startAnimation(in);
				mode = 4;
				nextProblem(true);
			}
		});

		Button answer1 = (Button) findViewById(R.id.submitexercisereadletter);
		answer1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String answer = ((EditText) findViewById(R.id.readletteredit))
						.getText().toString().toLowerCase();
				String right = ctrl.lproblems.get(randlproblems[clproblems]).answer
						.toLowerCase();
				Log.e("exercise", answer + " and " + right);
				boolean result = ctrl.answerChecking(answer, right);
				showAlertDialog(result);
			}
		});

		Button answer2 = (Button) findViewById(R.id.submitexercisereadword);
		answer2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String answer = ((EditText) findViewById(R.id.readwordedit))
						.getText().toString().toLowerCase();
				String right = ctrl.wproblems.get(randwproblems[cwproblems]).answer
						.toLowerCase();
				Log.e("exercise", answer + " and " + right);
				boolean result = ctrl.answerChecking(answer, right);
				showAlertDialog(result);
			}
		});

		Button answer3 = (Button) findViewById(R.id.submitexercisesayword);
		answer3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				checkRecognizer();
			}
		});

		GestureOverlayView gesture = (GestureOverlayView) findViewById(R.id.writelettergesture);
		gesture.addOnGesturePerformedListener(new OnGesturePerformedListener() {
			public void onGesturePerformed(GestureOverlayView gesture,
					Gesture in) {
				ArrayList<Prediction> pre = lib.recognize(in);
				String answer = "";
				String right = ctrl.gproblems.get(randgproblems[cgproblems]).answer;
				if (pre.size() > 0) {
					Prediction p = pre.get(0);
					if (p.score > 3.0) {
						answer = p.name;
					}
				}
				Log.e("exercise", answer + " and " + right);
				boolean result = ctrl.answerChecking(answer, right);
				showAlertDialog(result);
			}
		});
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
			startActivityForResult(intent, 1234);
		} else {
			showVoiceDialog("Gagal Recognize",
					"Tidak ada aplikasi Voice Recognition");
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1234 && resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			WordProblem p = ctrl.sproblems.get(randsproblems[csproblems]);
			boolean result = ctrl.recordAndRecognize(matches, p.answer);
			showAlertDialog(result);
		} else {
			showVoiceDialog("Gagal Recognize", "Tidak ada koneksi internet");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		switch (mode) {
		case 0:
			super.onBackPressed();
			return;
		case 1:
			clproblems = 0;
			readLetter.startAnimation(out);
			readLetter.setVisibility(View.GONE);
			break;
		case 2:
			cgproblems = 0;
			writeLetter.startAnimation(out);
			writeLetter.setVisibility(View.GONE);
			break;
		case 3:
			cwproblems = 0;
			readWord.startAnimation(out);
			readWord.setVisibility(View.GONE);
			break;
		case 4:
			csproblems = 0;
			sayWord.startAnimation(out);
			sayWord.setVisibility(View.GONE);
			break;
		}
		mode = 0;
		header.startAnimation(out);
		chooser.startAnimation(in);
		chooser.setVisibility(View.VISIBLE);
		header.setText("latihan");
		header.startAnimation(in);
	}

	public void backButtonListener(View v) {
		this.onBackPressed();
	}

	private void showVoiceDialog(String title, String msg) {
		final AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle(title);
		alert.setMessage(msg);
		alert.setButton("Kembali", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alert.dismiss();
			}
		});
		alert.show();
	}

	private void showFinishAlert() {
		final AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle("Selamat! :)");
		switch (mode) {
		case 1:
			alert.setMessage("Anda telah menyelesaikan modul latihan baca huruf");
			break;
		case 2:
			alert.setMessage("Anda telah menyelesaikan modul latihan tulis huruf");
			break;
		case 3:
			alert.setMessage("Anda telah menyelesaikan modul latihan baca kata");
			break;
		case 4:
			alert.setMessage("Anda telah menyelesaikan modul latihan ucap kata");
			break;
		}
		alert.setButton("Kembali", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				alert.dismiss();
				onBackPressed();
			}
		});
		alert.show();
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

	private void showAlertDialog(boolean result) {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		if (result == false) {
			alertDialog.setTitle("Maaf, :(");
			alertDialog.setMessage("Jawaban Anda salah :(");
			alertDialog.setButton("Coba Lagi",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							alertDialog.dismiss();
							nextProblem(true);
						}
					});
			alertDialog.setButton2("Lanjut",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							alertDialog.dismiss();
							nextProblem(false);
						}
					});
		} else {
			alertDialog.setTitle("Selamat! :)");
			alertDialog.setMessage("Jawaban Anda benar :)");
			alertDialog.setButton("Lanjut",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							alertDialog.dismiss();
							nextProblem(false);
						}
					});
		}
		alertDialog.show();
	}

	private void nextProblem(boolean repeat) {
		if (!repeat) {
			switch (mode) {
			case 1:
				++clproblems;
				break;
			case 2:
				++cgproblems;
				break;
			case 3:
				++cwproblems;
				break;
			case 4:
				++csproblems;
				break;
			}
		}
		TextView text;
		switch (mode) {
		case 1:
			if (clproblems == MAX_EXERCISE) {
				showFinishAlert();
			} else {
				LetterProblem p = ctrl.lproblems.get(randlproblems[clproblems]);
				EditText edit = (EditText) findViewById(R.id.readletteredit);
				edit.setText("");
				text = (TextView) findViewById(R.id.readlettertext);
				text.setText(p.letter);
				text.setTypeface(HanacarakaActivity.HANACARAKA_FONT);
				TextView cat = (TextView) findViewById(R.id.categoryreadletter);
				switch (p.category) {
				case 1:
					cat.setText("Kategori: Aksara Dasar");
					break;
				case 2:
					cat.setText("Kategori: Aksara Swara");
					break;
				case 3:
					cat.setText("Kategori: Aksara Rekan");
					break;
				case 4:
					cat.setText("Kategori: Aksara Murda");
					break;
				case 5:
					cat.setText("Kategori: Aksara Wilangan");
					break;
				}
			}
			break;
		case 2:
			if (cgproblems == MAX_EXERCISE) {
				showFinishAlert();
			} else {
				LetterProblem p = ctrl.gproblems.get(randgproblems[cgproblems]);
				text = (TextView) findViewById(R.id.writelettertext);
				text.setText("Tulis aksara \"" + p.answer + "\"");
			}
			break;
		case 3:
			if (cwproblems == MAX_EXERCISE) {
				showFinishAlert();
			} else {
				WordProblem p = ctrl.wproblems.get(randwproblems[cwproblems]);
				EditText edit = (EditText) findViewById(R.id.readwordedit);
				edit.setText("");
				text = (TextView) findViewById(R.id.readwordtext);
				text.setText(p.word);
				text.setTypeface(HanacarakaActivity.HANACARAKA_FONT);
			}
			break;
		case 4:
			if (csproblems == MAX_EXERCISE) {
				showFinishAlert();
			} else {
				WordProblem p = ctrl.sproblems.get(randsproblems[csproblems]);
				text = (TextView) findViewById(R.id.saywordtext);
				text.setText(p.word);
				text.setTypeface(HanacarakaActivity.HANACARAKA_FONT);
			}
			break;
		}
	}
}
