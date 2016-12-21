package com.ridhofkr.hanacaraka.controller;

import java.util.ArrayList;
import java.util.Collections;

import com.ridhofkr.hanacaraka.HanacarakaActivity;
import com.ridhofkr.hanacaraka.model.LetterProblem;
import com.ridhofkr.hanacaraka.model.WordProblem;

public class ExerciseController {
	public ArrayList<LetterProblem> lproblems;
	public ArrayList<WordProblem> wproblems;
	public ArrayList<LetterProblem> gproblems;
	public ArrayList<WordProblem> sproblems;

	public ExerciseController() {

	}

	public void makeListOfLetterProblem() {
		lproblems = HanacarakaActivity.DAO.loadLetterProblem();
	}

	public void makeListOfWordProblem() {
		wproblems = HanacarakaActivity.DAO.loadWordProblem(1);
	}

	public void makeListOfGestureProblem() {
		gproblems = HanacarakaActivity.DAO.loadLetterProblem(1);
	}

	public void makeListOfSayProblem() {
		sproblems = HanacarakaActivity.DAO.loadWordProblem(2);
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

	public boolean recordAndRecognize(ArrayList<String> matches, String answer) {
		for (int i = 0; i < matches.size(); i++) {
			if (matches.get(i).equals(answer)) {
				return true;
			}
		}
		return false;
	}
}
