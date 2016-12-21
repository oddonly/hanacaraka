package com.ridhofkr.hanacaraka.model;

public class Rules {
	public String name;
	public String letter;
	public String explanation;
	public String example;
	public String exampleText;

	public Rules(String name, String letter, String explanation,
			String exampleText, String example) {
		this.name = name;
		this.explanation = explanation;
		this.example = example;
		this.exampleText = exampleText;
		this.letter = letter;
	}
}
