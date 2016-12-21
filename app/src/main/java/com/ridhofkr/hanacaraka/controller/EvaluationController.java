package com.ridhofkr.hanacaraka.controller;

import java.util.ArrayList;
import java.util.Collections;

import com.ridhofkr.hanacaraka.HanacarakaActivity;
import com.ridhofkr.hanacaraka.model.LetterProblem;
import com.ridhofkr.hanacaraka.model.Problem;
import com.ridhofkr.hanacaraka.model.WordProblem;

public class EvaluationController {
	public ArrayList<Problem> problems;
	public int level;
	public float score;
	public long time[];
	public static final int MAX_PROBLEM = 5;
	public static long MAX_TIME;

	public EvaluationController(int level) {
		makeProblemSet(level);
		MAX_TIME = android.os.SystemClock.uptimeMillis() + 4*MAX_PROBLEM*1000L*120L;
	}

	public String countScore() {
		return Integer.toString((int) Math.ceil(100.0 * score
				/ (MAX_PROBLEM*4)));
	}

	public void startTime() {
		time[0] = System.currentTimeMillis();
	}

	public void stopTime() {
		time[1] = System.currentTimeMillis();
	}

	public String getElapsedTime() {
		stopTime();
		return Long.toString((time[1] - time[0]) / 1000L);
	}

	public void makeProblemSet(int level) {
		this.level = level;
		problems = new ArrayList<Problem>();
		ArrayList<LetterProblem> writeLetter = new ArrayList<LetterProblem>(
				HanacarakaActivity.DAO.loadLetterProblem(level, 1));
		ArrayList<WordProblem> readWord = new ArrayList<WordProblem>(
				HanacarakaActivity.DAO.loadWordProblem(level, 1));
		ArrayList<LetterProblem> readLetter = new ArrayList<LetterProblem>(
				HanacarakaActivity.DAO.loadLetterProblem());
		for (int i = 0; i < readLetter.size(); i++) {
			if (readLetter.get(i).level != level) {
				readLetter.remove(i);
			}
		}
		ArrayList<WordProblem> sayWord = new ArrayList<WordProblem>(
				HanacarakaActivity.DAO.loadWordProblem(level, 2));
		Collections.shuffle(readLetter);
		Collections.shuffle(readWord);
		Collections.shuffle(writeLetter);
		Collections.shuffle(sayWord);
		for (int i = 0; i < MAX_PROBLEM; i++) {
			problems.add(readLetter.get(i));
			problems.add(writeLetter.get(i));
			problems.add(readWord.get(i));
			problems.add(sayWord.get(i));
		}
		time = new long[2];
		score = 0.0f;
	}

	public boolean recordAndRecognize(ArrayList<String> matches, String answer) {
		for (int i = 0; i < matches.size(); i++) {
			if (matches.get(i).equals(answer)) {
				return true;
			}
		}
		return false;
	}

	public boolean answerChecking(String answer, String right) {
		if (right.equals("fa/va")
				&& (answer.equals("fa") || answer.equals("va"))) {
			return true;
		}
		return answer.equals(right);
	}

	public Integer[] random(int size) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		Collections.shuffle(list);
		return list.toArray(new Integer[list.size()]);
	}
}