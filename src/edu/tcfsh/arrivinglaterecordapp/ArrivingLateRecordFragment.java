package edu.tcfsh.arrivinglaterecordapp;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import edu.tcfsh.arrivinglaterecordapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ArrivingLateRecordFragment extends Fragment {
	private ListView arrivingLateRecordListView;
	private TextView titleText;
	private Button saveFileButton;
	private ArrivingLateRecordArrayAdapter arrivingLateRecordArrayAdapter;
	private ArrayList<StudentRecord> arrivingLateRecordList;
	private int dayOfMonth;
	private int month;
	private int year;
	private File arrivingLateRecord;
	OnArrivingLateRecordSelectedListener mCallback;

	public ArrivingLateRecordFragment(int dayOfMonth, int month, int year) {
		this.dayOfMonth = dayOfMonth;
		this.month = month;
		this.year = year;
	}

	public interface OnArrivingLateRecordSelectedListener {
		public void onSaveFileButtonSelected(String msg);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnArrivingLateRecordSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.arriving_late_record_fragment,
				container, false);
		initialize(rootView);
		initializeText();
		setListener();
		initializeFileWriter();
		return rootView;
	}

	private void initialize(View rootView) {
		arrivingLateRecordListView = (ListView) rootView
				.findViewById(R.id.arrivingLateRecordList);
		titleText = (TextView) rootView.findViewById(R.id.titleText);
		saveFileButton = (Button) rootView.findViewById(R.id.saveFileButton);
		arrivingLateRecordList = new ArrayList<StudentRecord>();
		arrivingLateRecordArrayAdapter = new ArrivingLateRecordArrayAdapter(
				arrivingLateRecordListView.getContext(),
				arrivingLateRecordList, mCallback);
		arrivingLateRecordListView.setAdapter(arrivingLateRecordArrayAdapter);
	}

	private void initializeText() {

		String title = "";
		String givenDateString = year + "." + month + "." + dayOfMonth;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date date = sdf.parse(givenDateString);
			long timeInMilliseconds = date.getTime();

			title = String.format("%1$tY年%1$tb%1$td日 %1$tA 遲到紀錄",
					timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		titleText.setText(title);
	}

	View.OnClickListener saveFileButtonOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			saveArrivingLateRecordFile();
		}
	};

	private void setListener() {
		saveFileButton.setOnClickListener(saveFileButtonOnClickListener);
	}
	

	private void writerTask() {
		String recordingDateStr = year + "." + month + "." + dayOfMonth;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date recordingDate = sdf.parse(recordingDateStr);
			WriterThread writerThread = new WriterThread(arrivingLateRecord,
					arrivingLateRecordList, recordingDate);
			writerThread.start();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private void initializeFileWriter() {

		// 指定xls存檔檔名
		String attendanceRecordFileName = getAttendanceRecordFileName();

		File SDCardpath = Environment.getExternalStorageDirectory();
		arrivingLateRecord = new File(SDCardpath.getAbsolutePath()
				+ "/arriving late record/" + attendanceRecordFileName);

		// 檢查路徑是否存在
		if (!arrivingLateRecord.getParentFile().exists()) {
			arrivingLateRecord.getParentFile().mkdirs();

		}

		try {
			if (arrivingLateRecord.exists()) {

				// 如已有檔案存在，則讀入檔案資料
				Workbook workbook = Workbook.getWorkbook(arrivingLateRecord);
				Sheet sheet = workbook.getSheet(0);

				int rowCount = 2;

				while (rowCount < sheet.getRows()) {

					String timeLabel = "";
					int gradeAndClassAndNum = 0;
					int studentId = 0;
					String studentName = "";
					Cell timeCell = sheet.getCell(0, rowCount);
					if (timeCell.getType() == CellType.LABEL) {
						LabelCell lc = (LabelCell) timeCell;
						timeLabel = (String) lc.getString();
					}

					Cell gradeAndClassAndNumCell = sheet.getCell(1, rowCount);
					if (gradeAndClassAndNumCell.getType() == CellType.NUMBER) {
						NumberCell nc = (NumberCell) gradeAndClassAndNumCell;
						gradeAndClassAndNum = (int) nc.getValue();
					}

					Cell studentIdCell = sheet.getCell(2, rowCount);
					if (studentIdCell.getType() == CellType.NUMBER) {
						NumberCell nc = (NumberCell) studentIdCell;
						studentId = (int) nc.getValue();
					}

					Cell studentNameCell = sheet.getCell(3, rowCount);
					if (studentNameCell.getType() == CellType.LABEL) {
						LabelCell lc = (LabelCell) studentNameCell;
						studentName = (String) lc.getString();
					}

					StudentRecord studentRecord = new StudentRecord(
							gradeAndClassAndNum / 10000,
							(gradeAndClassAndNum % 10000) / 100,
							(gradeAndClassAndNum % 100), studentId, studentName);

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd EEE HH:mm:ss");

					Date date;
					date = sdf.parse(timeLabel);

					studentRecord.setDate(date);
					arrivingLateRecordList.add(studentRecord);

					rowCount++;
				}

			} else {
				// 如檔案不存在，則建立檔案
				try {
					arrivingLateRecord.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private String getAttendanceRecordFileName() {

		String attendanceRecordFileName = "";
		String recordingDateStr = year + "." + month + "." + dayOfMonth;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

		try {
			Date recordingDate = sdf.parse(recordingDateStr);
			long timeInMilliseconds = recordingDate.getTime();

			attendanceRecordFileName = String.format(
					"%1$tY年%1$tb%1$td日 %1$tA 遲到紀錄.xls", timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return attendanceRecordFileName;
	}

	@Override
	public void onStart() {
		arrivingLateRecordArrayAdapter.notifyDataSetChanged();
		super.onStart();

	}

	public boolean updateList(StudentRecord studentRecord) {
		if (arrivingLateRecordArrayAdapter.updateList(studentRecord)) {
			arrivingLateRecordArrayAdapter.notifyDataSetChanged();
			arrivingLateRecordListView.smoothScrollToPosition(0);
			return true;
		} else {
			return false;
		}
	}
	
	public void saveArrivingLateRecordFile(){
		writerTask();
		mCallback.onSaveFileButtonSelected("檔案已儲存!");
	}

}