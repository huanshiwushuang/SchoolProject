package com.guohao.entity;

public class ExamRecord {
	private String stuNumber;
	private String score;
	private String examName;
	private String course;
	private String beginTime;
	private String endTime;
	private String isPass;
	private String examTime;
	
	public ExamRecord() {}
	
	public ExamRecord(String stuNumber, String score, String examName, String course, String beginTime, String endTime,
			String isPass, String examTime) {
		super();
		this.stuNumber = stuNumber;
		this.score = score;
		this.examName = examName;
		this.course = course;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.isPass = isPass;
		this.examTime = examTime;
	}
	public String getStuNumber() {
		return stuNumber;
	}
	public void setStuNumber(String stuNumber) {
		this.stuNumber = stuNumber;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getIsPass() {
		return isPass;
	}
	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}
	public String getExamTime() {
		return examTime;
	}
	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}
}
