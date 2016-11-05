package com.guohao.entity;

public class ExamTi {
	private String examTiType;
	private String content;
	private String selectAnswers;
	private String chooseAnswer;
	
	public ExamTi() {}
	
	public ExamTi(String examTiType, String content, String selectAnswers, String chooseAnswer) {
		super();
		this.examTiType = examTiType;
		this.content = content;
		this.selectAnswers = selectAnswers;
		this.chooseAnswer = chooseAnswer;
	}



	public String getExamTiType() {
		return examTiType;
	}

	public void setExamTiType(String examTiType) {
		this.examTiType = examTiType;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSelectAnswers() {
		return selectAnswers;
	}
	public void setSelectAnswers(String selectAnswers) {
		this.selectAnswers = selectAnswers;
	}

	public String getChooseAnswer() {
		return chooseAnswer;
	}

	public void setChooseAnswer(String chooseAnswer) {
		this.chooseAnswer = chooseAnswer;
	}
	
}
