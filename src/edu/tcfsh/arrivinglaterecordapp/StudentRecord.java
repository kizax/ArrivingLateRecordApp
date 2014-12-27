package edu.tcfsh.arrivinglaterecordapp;

public class StudentRecord {
	private int gradeNum;
	private int classNum;
	private int studentId;
	private String studentName;
	private int num;

	public StudentRecord(int gradeNum, int classNum, int num, int studentId,
			String studentName) {
		this.gradeNum = gradeNum;
		this.classNum = classNum;
		this.num = num;
		this.studentId = studentId;
		this.studentName = studentName;
	}

	public String getStudentNum() {
		String record = String.format("%1$d%2$02d%3$02d", gradeNum, classNum,
				num);

		return record;
	}

	public boolean matchStudentID(String expression) {
		String regep = expression.replace('*', '.');
		return  Integer.toString(studentId).matches(regep);
	}
	
	public boolean matchStudentName(String expression) {
		String regep = expression.replace('*', '.');
		return  studentName.matches(regep);
	}
	
	public boolean matchStudentNum(String expression) {
		String regep = expression.replace('*', '.');
		return  getStudentNum().matches(regep);
	}


	public int getGradeNum() {
		return gradeNum;
	}

	public void setGradeNum(int gradeNum) {
		this.gradeNum = gradeNum;
	}

	public int getClassNum() {
		return classNum;
	}

	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
