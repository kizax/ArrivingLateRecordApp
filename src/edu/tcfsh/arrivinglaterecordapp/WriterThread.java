package edu.tcfsh.arrivinglaterecordapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat
					.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

			long timeInMilliseconds = date.getTime();

			String dateStr = String.format("%1$tY年%1$tb%1$td日 %1$tA 遲到紀錄",
					timeInMilliseconds);

			Label dateLabel = new Label(0, 0, dateStr);
			writableSheet.addCell(dateLabel);

			Label recordingTimeLabel = new Label(0, 1, "登記時間", cellFormat);
			writableSheet.addCell(recordingTimeLabel);

			Label gradeAndClassAndNumLabel = new Label(1, 1, "年班號", cellFormat);
			writableSheet.addCell(gradeAndClassAndNumLabel);

			Label studentIdLabel = new Label(2, 1, "學號", cellFormat);
			writableSheet.addCell(studentIdLabel);

			Label studentNameLabel = new Label(3, 1, "姓名", cellFormat);
			writableSheet.addCell(studentNameLabel);

			int index = 2;
			for (StudentRecord studentRecord : studentRecordList) {

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd EEE HH:mm:ss");

				String dateString = sdf.format(studentRecord.getDate());

				Label timeLabel = new Label(0, index, dateString, cellFormat);

				writableSheet.addCell(timeLabel);

				int gradeAndClassAndNum = studentRecord.getGradeNum() * 10000
						+ studentRecord.getClassNum() * 100
						+ studentRecord.getNum();
				Number number = new Number(1, index, gradeAndClassAndNum,
						cellFormat);
				writableSheet.addCell(number);

				int studentId = studentRecord.getStudentId();
				number = new Number(2, index, studentId, cellFormat);
				writableSheet.addCell(number);

				Label nameLabel = new Label(3, index,
						studentRecord.getStudentName(), cellFormat);
				writableSheet.addCell(nameLabel);

				index++;
			}

			for (int columnCount = 1; columnCount < 4; columnCount++) {
				CellView cell = writableSheet.getColumnView(columnCount);
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
				writableSheet.setColumnView(columnCount, cell);
				index++;
			}

			int columnCount = 0;
			CellView cell = writableSheet.getColumnView(columnCount);
			cell.setAutosize(true);
			writableSheet.setColumnView(columnCount, cell);

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
