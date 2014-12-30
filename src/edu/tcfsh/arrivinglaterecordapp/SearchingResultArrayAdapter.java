package edu.tcfsh.arrivinglaterecordapp;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import edu.tcfsh.arrivinglaterecordapp.R;
import edu.tcfsh.arrivinglaterecordapp.SearchingStudentFragment.OnSearchingResultSelectedListener;

public class SearchingResultArrayAdapter extends ArrayAdapter<StudentRecord> {
	private final Context context;
	private final ArrayList<StudentRecord> searchingResultList;
	OnSearchingResultSelectedListener mCallback;

	public SearchingResultArrayAdapter(Context context,
			ArrayList<StudentRecord> searchResultList,
			OnSearchingResultSelectedListener mCallback) {
		super(context, R.layout.student_data_item, searchResultList);
		this.context = context;
		this.searchingResultList = searchResultList;
		this.mCallback = mCallback;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		StudentRecordViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.student_data_item, parent,
					false);

			holder = new StudentRecordViewHolder();

			holder.gradeText = (TextView) convertView
					.findViewById(R.id.gradeNum);
			holder.classText = (TextView) convertView
					.findViewById(R.id.classNum);
			holder.numText = (TextView) convertView.findViewById(R.id.num);
			holder.studentIdText = (TextView) convertView
					.findViewById(R.id.studentIdText);
			holder.studentNameText = (TextView) convertView
					.findViewById(R.id.studentNameText);
			holder.addButton = (Button) convertView
					.findViewById(R.id.addButton);

			convertView.setTag(holder);

		} else {
			holder = (StudentRecordViewHolder) convertView.getTag();
		}

		holder.addButton.setTag(position);

		holder.gradeText.setText(String.valueOf(searchingResultList.get(
				position).getGradeNum()));

		String classText = String.format("%1$02d",
				searchingResultList.get(position).getClassNum());
		holder.classText.setText(classText);

		String numText = String.format("%1$02d",
				searchingResultList.get(position).getNum());
		holder.numText.setText(numText);

		holder.studentIdText.setText(String.valueOf(searchingResultList.get(
				position).getStudentId()));

		holder.studentNameText.setText(String.valueOf(searchingResultList.get(
				position).getStudentName()));

		View.OnClickListener AddButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int position = (Integer) v.getTag();
				StudentRecord studentRecord = searchingResultList.get(position);
				studentRecord.setDate(new Date());
				mCallback.onStudentRecordSelected(studentRecord);

			}
		};

		holder.addButton.setOnClickListener(AddButtonListener);

		return convertView;
	}
}
