package edu.tcfsh.arrivinglaterecordapp;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.effectivenavigation.R;

public class ArrivingLateRecordArrayAdapter extends ArrayAdapter<StudentRecord> {
	private final Context context;
	private final ArrayList<StudentRecord> arrivingLateRecordList;

	public ArrivingLateRecordArrayAdapter(Context context,
			ArrayList<StudentRecord> arrivingLateRecordList) {
		super(context, R.layout.arriving_late_record_item_listview, arrivingLateRecordList);
		this.context = context;
		this.arrivingLateRecordList = arrivingLateRecordList;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ArrivingLateViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.arriving_late_record_item_listview, parent,
					false);

			holder = new ArrivingLateViewHolder();

			holder.gradeText = (TextView) convertView
					.findViewById(R.id.gradeNum);
			holder.classText = (TextView) convertView
					.findViewById(R.id.classNum);
			holder.numText = (TextView) convertView.findViewById(R.id.num);
			holder.studentIdText = (TextView) convertView
					.findViewById(R.id.studentIdText);
			holder.studentNameText = (TextView) convertView
					.findViewById(R.id.studentNameText);
			holder.deleteButton = (Button) convertView
					.findViewById(R.id.deleteButton);

			convertView.setTag(holder);

		} else {
			holder = (ArrivingLateViewHolder) convertView.getTag();
		}

		holder.deleteButton.setTag(position);

		holder.gradeText.setText(String.valueOf(arrivingLateRecordList.get(position)
				.getGradeNum()));

		String classText = String.format("%1$02d",
				arrivingLateRecordList.get(position).getClassNum());
		holder.classText.setText(classText);

		String numText = String.format("%1$02d", arrivingLateRecordList
				.get(position).getNum());
		holder.numText.setText(numText);

		holder.studentIdText.setText(String.valueOf(arrivingLateRecordList.get(
				position).getStudentId()));

		holder.studentNameText.setText(String.valueOf(arrivingLateRecordList.get(
				position).getStudentName()));

		View.OnClickListener deleteButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int pos = (Integer) v.getTag();
				Log.d("POS", "POS: " + pos);

				StudentRecord studentRecord = arrivingLateRecordList.get(pos);
				Log.d("POS", "ID: " + studentRecord.getStudentId());
				
				arrivingLateRecordList.remove(studentRecord);
				
				notifyDataSetChanged();

			}
		};

		holder.deleteButton.setOnClickListener(deleteButtonListener);

		return convertView;
	}
}
