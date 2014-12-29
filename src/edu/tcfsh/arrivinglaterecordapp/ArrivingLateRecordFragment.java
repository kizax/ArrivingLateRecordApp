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
import edu.tcfsh.arrivinglaterecordapp.SearchStudentFragment.OnHeadlineSelectedListener;
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
	private ListView myList;
	private TextView titleText;
	private Button commitButton;
	private ArrivingLateRecordArrayAdapter adapter;
	private ArrayList<StudentRecord> arrivingLateRecordList;
	private int dayOfMonth;
	private int month;
	private int year;
	private File arrivingLateRecord;
	OnDeleteSelectedListener mCallback;

	public ArrivingLateRecordFragment(int dayOfMonth, int month, int year) {
		this.dayOfMonth = dayOfMonth;
		this.month = month;
		this.year = year;
	}

	// Container Activity must implement this interface
	public interface OnDeleteSelectedListener {
		public void onDeleteSelected(String msg);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnDeleteSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.arriving_late_fragment,
				container, false);
		initialize(rootView);
		initializeText();
		setListener();
		initializeFileWriter();
		return rootView;
	}

	private void writerTask() {
		String title = "";
		String givenDateString = year + "." + month + "." + dayOfMonth;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date date = sdf.parse(givenDateString);
			WriterThread writerThread = new WriterThread(arrivingLateRecord,
					arrivingLateRecordList, date);
			writerThread.start();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private String getAttendanceRecordFileName() {

		String attendanceRecordFileName = "";
		String dateStr = year + "." + month + "." + dayOfMonth;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

		try {
			Date date = sdf.parse(dateStr);
			long timeInMilliseconds = date.getTime();

			attendanceRecordFileName = String.format(
					"%1$tY年%1$tb%1$td日 %1$tA 遲到紀錄.xls", timeInMilliseconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return attendanceRecordFileName;
	}

	private void initializeFileWriter() {

		// 指定xls存檔檔名
		String attendanceRecordFileName = getAttendanceRecordFileName();

		File SDCardpath = Environment.getExternalStorageDirectory();
		arrivingLateRecord = new File(SDCardpath.getAbsolutePath()
				+ "/arriving late record/" + attendanceRecordFileName);
		// File copyOfAttendanceRecord = new File(SDCardpath.getAbsolutePath()
		// + "/attendance Record/" + attendanceRecordFileName + "_copy");

		Log.d("kizax", SDCardpath.getAbsolutePath() + "/arriving late record/"
				+ attendanceRecordFileName);

		// 檢查路徑是否存在
		if (!arrivingLateRecord.getParentFile().exists()) {
			arrivingLateRecord.getParentFile().mkdirs();

		}

		//
		try {
			if (arrivingLateRecord.exists()) {
				Workbook workbook = Workbook.getWorkbook(arrivingLateRecord);
				Sheet sheet = workbook.getSheet(0);

				int rowCount = 2;

				int a = sheet.getRows();
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

					StudentRecord s = new StudentRecord(
							gradeAndClassAndNum / 10000,
							(gradeAndClassAndNum % 10000) / 100,
							(gradeAndClassAndNum % 100), studentId, studentName);

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");

					Date date;
					date = sdf.parse(timeLabel);

					s.setDate(date);
					arrivingLateRecordList.add(0, s);

					rowCount++;
				}

			} else {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	@Override
	public void onStart() {
		adapter.notifyDataSetChanged();
		super.onStart();

	}

	@Override
	public void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();

	}

	public void updateList(StudentRecord s) {
		adapter.updateList(s);
		adapter.notifyDataSetChanged();

		myList.smoothScrollToPosition(0);
	}

	View.OnClickListener commitButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			writerTask();
			mCallback.onDeleteSelected("檔案已儲存");

		}
	};

	private void setListener() {
		commitButton.setOnClickListener(commitButtonListener);
	}

	private void initialize(View rootView) {

		myList = (ListView) rootView.findViewById(R.id.arrivingLatelist);
		titleText = (TextView) rootView.findViewById(R.id.titleText);
		commitButton = (Button) rootView.findViewById(R.id.commitButton);
		arrivingLateRecordList = new ArrayList<StudentRecord>();
		adapter = new ArrivingLateRecordArrayAdapter(myList.getContext(),
				arrivingLateRecordList, mCallback);
		myList.setAdapter(adapter);

	}

}