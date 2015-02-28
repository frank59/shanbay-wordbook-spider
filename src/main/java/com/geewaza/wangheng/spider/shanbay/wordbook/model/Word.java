package com.geewaza.wangheng.spider.shanbay.wordbook.model;

public class Word {
	private String word;
	private String expression;
	public Word(String word, String expression) {
		this.word = word;
		this.expression = expression;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return "["
				+ "word:" + word
				+ ", expression:" + expression
				+ "]";
	}
}
