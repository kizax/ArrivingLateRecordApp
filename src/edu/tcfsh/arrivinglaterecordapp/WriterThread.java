package edu.tcfsh.arrivinglaterecordapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriterThread extends Thread {

	private static final int totalGradeNum = 3;
	private static final int totalClassNum = 25;
	private WritableWorkbook writableWorkbook;
	private WritableSheet arivingLateRecordSheet;
	private WritableSheet statisticalTableSheet;
	private ArrayList<StudentRecord> studentRecordList;
	private Date date;
	private int[][] statisticalTable = new int[totalGradeNum][totalClassNum];

	public WriterThread(File attendanceRecord,
			ArrayList<StudentRecord> studentRecordList, Date date) {
		try {
			this.writableWorkbook = Workbook.createWorkbook(attendanceRecord);
			this.arivingLateRecordSheet = writableWorkbook.createSheet(
					"ariving late record", 0);
			this.statisticalTableSheet = writableWorkbook.createSheet(
					"statistical table", 0);
			this.studentRecordList = studentRecordList;

			for (StudentRecord studentRecord : studentRecordList) {
				int gradeNum = studentRecord.getGradeNum();
				int classNum = studentRecord.getClassNum();
				statisticalTable[gradeNum - 1][classNum - 1]++;
			}

			this.date = date;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		super.run();
		try {

			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat
					.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

			Label classLabel = new Label(0, 0, "班級", cellFormat);
			statisticalTableSheet.addCell(classLabel);

			Label arivingLateStudentNumLabel = new Label(1, 0, "遲到學生數",
					cellFormat);
			statisticalTableSheet.addCell(arivingLateStudentNumLabel);

			int statisticalTableRowCount = 1;
			for (int rowCount = 0; rowCount < totalGradeNum; rowCount++) {
				for (int columnCount = 0; columnCount < totalClassNum; columnCount++) {

					int gradeAndClass = (rowCount + 1) * 100
							+ (columnCount + 1);
					Number number = new Number(0, statisticalTableRowCount,
							gradeAndClass, cellFormat);
					statisticalTableSheet.addCell(number);

					int arivingLateStudentNum = statisticalTable[rowCount][columnCount];
					number = new Number(1, statisticalTableRowCount,
							arivingLateStudentNum, cellFormat);
					statisticalTableSheet.addCell(number);

					statisticalTableRowCount++;
				}
			}

			int arivingLateStudentColumnIndex = 0;
			CellView arivingLateStudentColumnCell = arivingLateRecordSheet
					.getColumnView(arivingLateStudentColumnIndex);
			arivingLateStudentColumnCell.setAutosize(true);
			arivingLateRecordSheet.setColumnView(arivingLateStudentColumnIndex,
					arivingLateStudentColumnCell);

			long timeInMilliseconds = date.getTime();

			String dateStr = String.format("%1$tY年%1$tb%1$td日 %1$tA 遲到紀錄",
					timeInMilliseconds);

			Label dateLabel = new Label(0, 0, dateStr);
			arivingLateRecordSheet.addCell(dateLabel);

			Label recordingTimeLabel = new Label(0, 1, "登記時間", cellFormat);
			arivingLateRecordSheet.addCell(recordingTimeLabel);

			Label gradeAndClassAndNumLabel = new Label(1, 1, "年班號", cellFormat);
			arivingLateRecordSheet.addCell(gradeAndClassAndNumLabel);

			Label studentIdLabel = new Label(2, 1, "學號", cellFormat);
			arivingLateRecordSheet.addCell(studentIdLabel);

			Label studentNameLabel = new Label(3, 1, "姓名", cellFormat);
			arivingLateRecordSheet.addCell(studentNameLabel);

			int arrivingLateRecordRowCount = 2;

			ArrayList<StudentRecord> copyOfStudentRecordList = (ArrayList<StudentRecord>) studentRecordList
					.clone();
			Collections.reverse(copyOfStudentRecordList);
			for (StudentRecord studentRecord : copyOfStudentRecordList) {

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd EEE HH:mm:ss");

				String dateString = sdf.format(studentRecord.getDate());

				Label timeLabel = new Label(0, arrivingLateRecordRowCount,
						dateString, cellFormat);

				arivingLateRecordSheet.addCell(timeLabel);

				int gradeAndClassAndNum = studentRecord.getGradeNum() * 10000
						+ studentRecord.getClassNum() * 100
						+ studentRecord.getNum();
				Number number = new Number(1, arrivingLateRecordRowCount,
						gradeAndClassAndNum, cellFormat);
				arivingLateRecordSheet.addCell(number);

				int studentId = studentRecord.getStudentId();
				number = new Number(2, arrivingLateRecordRowCount, studentId,
						cellFormat);
				arivingLateRecordSheet.addCell(number);

				Label nameLabel = new Label(3, arrivingLateRecordRowCount,
						studentRecord.getStudentName(), cellFormat);
				arivingLateRecordSheet.addCell(nameLabel);

				arrivingLateRecordRowCount++;
			}

			for (int columnCount = 1; columnCount < 4; columnCount++) {
				CellView cell = arivingLateRecordSheet
						.getColumnView(columnCount);
				int width = 0;
				switch (columnCount) {
				case 1:
					width = 10 * 256;
				case 2:
					width = 10 * 256;
				case 3:
					width = 10 * 256;
				}
				cell.setSize(width);
				arivingLateRecordSheet.setColumnView(columnCount, cell);

			}

			int timeColumnIndex = 0;
			CellView cell = arivingLateRecordSheet
					.getColumnView(timeColumnIndex);
			cell.setAutosize(true);
			arivingLateRecordSheet.setColumnView(timeColumnIndex, cell);

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
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
}
