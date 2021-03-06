package edu.tcfsh.arrivinglaterecordapp;

import java.util.Comparator;
import java.util.Date;

public class StudentRecord implements Comparator {
	private int gradeNum;
	private int classNum;
	private int studentId;
	private String studentName;
	private int num;
	private Date date;

	public StudentRecord(int gradeNum, int classNum, int num, int studentId,
			String studentName) {
		this.gradeNum = gradeNum;
		this.classNum = classNum;
		this.num = num;
		this.studentId = studentId;
		this.studentName = studentName;

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStudentNum() {
		String record = String.format("%1$d%2$02d%3$02d", gradeNum, classNum,
				num);

		return record;
	}

	public boolean matchStudentID(String expression) {
		String regep = expression.replace('*', '.');
		return Integer.toString(studentId).matches(regep);
	}

	public boolean matchStudentName(String expression) {
		String regep = expression.replace('*', '.');
		return studentName.matches(regep);
	}

	public boolean matchStudentNum(String expression) {
		String regep = expression.replace('*', '.');
		return getStudentNum().matches(regep);
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

	@Override
	public String toString() {
		return getGradeNum() + " 年 " + getClassNum() + " 班 " + getNum()
				+ " 號  " + getStudentId() + "  " + getStudentName();

	}

	@Override
	public int compare(Object object1, Object object2) {
		StudentRecord studentRecord1 = (StudentRecord) object1;
		StudentRecord studentRecord2 = (StudentRecord) object2;

		if (studentRecord1.getStudentId() > studentRecord2.getStudentId()) {
			return 1;
		} else if (studentRecord1.getStudentId() < studentRecord2
				.getStudentId()) {
			return -1;
		} else if (studentRecord1.getStudentId() == studentRecord2
				.getStudentId()) {
			return 0;
		}
		return -1;

	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof StudentRecord) {
			StudentRecord studentRecord = (StudentRecord) o;
			if (this.getStudentId() == studentRecord.getStudentId()) {
				return true;
			}
		}
		return false;
	}

}
