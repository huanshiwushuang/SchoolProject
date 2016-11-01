package com.guohao.entity;

public class ExamPaper {
	private Boolean isDelete;
	private int totalScore;
	private int passScore;
	private int mode;
	private String createAdmin;
	private long createTime;
	private Boolean isSelectType;
	private String name;
	private String classNo;
	private String course;
	private int id;
	private long beginTime;
	private long endTime;
	private int examTime;
	private String descript;
	
	public ExamPaper(Boolean isDelete, int totalScore, int passScore, int mode, String createAdmin, long createTime,
			Boolean isSelectType, String name, String classNo, String course, int id, long beginTime, long endTime,
			int examTime, String descript) {
		super();
		this.isDelete = isDelete;
		this.totalScore = totalScore;
		this.passScore = passScore;
		this.mode = mode;
		this.createAdmin = createAdmin;
		this.createTime = createTime;
		this.isSelectType = isSelectType;
		this.name = name;
		this.classNo = classNo;
		this.course = course;
		this.id = id;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.examTime = examTime;
		this.descript = descript;
	}
	
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public int getPassScore() {
		return passScore;
	}
	public void setPassScore(int passScore) {
		this.passScore = passScore;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public String getCreateAdmin() {
		return createAdmin;
	}
	public void setCreateAdmin(String createAdmin) {
		this.createAdmin = createAdmin;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public Boolean getIsSelectType() {
		return isSelectType;
	}
	public void setIsSelectType(Boolean isSelectType) {
		this.isSelectType = isSelectType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassNo() {
		return classNo;
	}
	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getExamTime() {
		return examTime;
	}
	public void setExamTime(int examTime) {
		this.examTime = examTime;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
}
