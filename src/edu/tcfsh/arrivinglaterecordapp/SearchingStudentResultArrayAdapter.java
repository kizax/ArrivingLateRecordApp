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
import edu.tcfsh.arrivinglaterecordapp.SearchStudentFragment.OnHeadlineSelectedListener;

public class SearchingStudentResultArrayAdapter extends
		ArrayAdapter<StudentRecord> {
	private final Context context;
	private final ArrayList<StudentRecord> searchResultList;
	private final ArrayList<StudentRecord> arrivingLateRecordList;
	OnHeadlineSelectedListener mCallback;

	public SearchingStudentResultArrayAdapter(Context context,
			ArrayList<StudentRecord> searchResultList,
			ArrayList<StudentRecord> arrivingLateRecordList,
			OnHeadlineSelectedListener mCallback) {
		super(context, R.layout.num_item_listview, searchResultList);
		this.context = context;
		this.searchResultList = searchResultList;
		this.arrivingLateRecordList = arrivingLateRecordList;
		this.mCallback = mCallback;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.num_item_listview, parent,
					false);

			holder = new ViewHolder();

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
			holder = (ViewHolder) convertView.getTag();
		}

		holder.addButton.setTag(position);

		holder.gradeText.setText(String.valueOf(searchResultList.get(position)
				.getGradeNum()));

		String classText = String.format("%1$02d",
				searchResultList.get(position).getClassNum());
		holder.classText.setText(classText);

		String numText = String.format("%1$02d", searchResultList.get(position)
				.getNum());
		holder.numText.setText(numText);

		holder.studentIdText.setText(String.valueOf(searchResultList.get(
				position).getStudentId()));

		holder.studentNameText.setText(String.valueOf(searchResultList.get(
				position).getStudentName()));

		View.OnClickListener AddButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int pos = (Integer) v.getTag();
				Log.d("POS", "POS: " + pos);

				StudentRecord studentRecord = searchResultList.get(pos);
				Log.d("POS", "ID: " + studentRecord.getStudentId());

				studentRecord.setDate(new Date());
				arrivingLateRecordList.add(studentRecord);

				notifyDataSetChanged();

				mCallback.onArticleSelected(studentRecord);

			}
		};

		holder.addButton.setOnClickListener(AddButtonListener);

		return convertView;
	}
}
