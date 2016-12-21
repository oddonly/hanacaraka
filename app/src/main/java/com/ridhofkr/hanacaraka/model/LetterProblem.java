package com.ridhofkr.hanacaraka.model;

public class LetterProblem extends Problem {
	public String letter;
	public String answer;
	
	public LetterProblem(String letter, String answer, int level, int category) {
		this.letter = letter;
		this.answer = answer;
		this.level = level;
		this.category = category;
	}
}
