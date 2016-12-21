package com.ridhofkr.hanacaraka.model;

public class Letters {
	public String name;
	public String letter;
	public String couplingRule;
	public String coupling;
	public String wordExample;
	public String letterExample;
	public String soundExample;
	public String animationExample;
	public int category;

	public Letters(String aksara, String text, String sandhangan,
			String textSandhangan, String sample, String sampleText,
			int category) {
		this.letter = aksara;
		this.soundExample = text.toLowerCase().replace('/', '_')  + "_" + category;
		if (category == 5) {
			this.soundExample = "s" + this.soundExample;
		}
		this.animationExample = text.replace('/', '_');
		this.couplingRule = textSandhangan;
		this.name = text;
		this.wordExample = sampleText;
		this.letterExample = sample;
		this.coupling = "?" + sandhangan;
		this.category = category;
	}
}
