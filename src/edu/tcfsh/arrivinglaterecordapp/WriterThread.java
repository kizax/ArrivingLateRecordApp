package edu.tcfsh.arrivinglaterecordapp;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriterThread extends Thread {

	private WritableWorkbook writableWorkbook;
	private WritableSheet writableSheet;
	private ArrayList<StudentRecord> studentRecordList;
	private Date date;

	public WriterThread(File attendanceRecord,
			ArrayList<StudentRecord> studentRecordList, Date date) {
		try {
			this.writableWorkbook = Workbook.createWorkbook(attendanceRecord);
			this.writableSheet = writableWorkbook.createSheet(
					"attendanceRecord", 0);
			this.studentRecordList = studentRecordList;
			this.date = date;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		super.run();
		try {

			long timeInMilliseconds = date.getTime();

			String dateStr = String.format("%1$tY年%1$tb%1$td日 %1$tA 遲到紀錄",
					timeInMilliseconds);

			Label dateLabel = new Label(0, 0, dateStr);
			writableSheet.addCell(dateLabel);

			Label timeLabel = new Label(0, 1, "登記時間");
			writableSheet.addCell(timeLabel);

			Label gradeAndClassAndNumLabel = new Label(1, 1, "年班號");
			writableSheet.addCell(gradeAndClassAndNumLabel);

			Label studentIdLabel = new Label(2, 1, "學號");
			writableSheet.addCell(studentIdLabel);

			Label studentNameLabel = new Label(3, 1, "姓名");
			writableSheet.addCell(studentNameLabel);

			int index = 2;
			for (StudentRecord s : studentRecordList) {

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");

				String dateString = sdf.format(s.getDate());

				Label time = new Label(0, index, dateString);
				writableSheet.addCell(time);

				int gradeAndClassAndNum = s.getGradeNum() * 10000
						+ s.getClassNum() * 100 + s.getNum();
				Number number = new Number(1, index, gradeAndClassAndNum);
				writableSheet.addCell(number);

				int studentId = s.getStudentId();
				number = new Number(2, index, studentId);
				writableSheet.addCell(number);

				Label nameLabel = new Label(3, index, s.getStudentName());
				writableSheet.addCell(nameLabel);

				index++;
			}

			if (writableWorkbook != null) {
				synchronized (writableWorkbook) {
					// All sheets and cells added. Now write out the workbook
					writableWorkbook.write();
					writableWorkbook.close();
				}
			}

		} catch (IOException e) {
			System.out.println("Read failed");
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
