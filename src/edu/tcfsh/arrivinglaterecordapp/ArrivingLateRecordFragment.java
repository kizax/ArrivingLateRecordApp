package edu.tcfsh.arrivinglaterecordapp;

import java.util.ArrayList;

import com.example.effectivenavigation.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class ArrivingLateRecordFragment extends Fragment {
	private ListView myList;
	private Button commitButton;
	private ArrivingLateRecordArrayAdapter adapter;
	private ArrayList<StudentRecord> arrivingLateRecordList;

	public ArrivingLateRecordFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.arriving_late_fragment,
				container, false);
		initialize(rootView);
		setListener();
		adapter.notifyDataSetChanged();

		return rootView;
	}

	@Override
	public void onStart() {
		adapter.notifyDataSetChanged();
		Log.d("POS", "onStart");
		super.onStart();

	}

	@Override
	public void onResume() {
		adapter.notifyDataSetChanged();
		Log.d("POS", "onResume");
		super.onResume();

	}


	 public void updateList(StudentRecord s) {
		 adapter.updateList(s);
		 adapter.notifyDataSetChanged();
	
	 }

	View.OnClickListener commitButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//
			// String expression = regexEditText.getText().toString();
			// statusText.setText("您的查詢：" + expression);
			// regexEditText.setText("");
			//
			// searchResultList.clear();
			// switch (expression.length()) {
			// case 2:
			// case 3:
			// case 4:
			// for (StudentRecord rec : studentRecordList) {
			// if (rec.matchStudentName(expression)) {
			// searchResultList.add(rec);
			// }
			// }
			// break;
			// case 5:
			// for (StudentRecord rec : studentRecordList) {
			// if (rec.matchStudentNum(expression)) {
			// searchResultList.add(rec);
			// }
			// }
			// break;
			// case 6:
			// for (StudentRecord rec : studentRecordList) {
			// if (rec.matchStudentID(expression)) {
			// searchResultList.add(rec);
			// }
			// }
			// break;
			// }
			//
			// adapter.notifyDataSetChanged();

		}
	};

	private void setListener() {
		commitButton.setOnClickListener(commitButtonListener);
	}

	private void initialize(View rootView) {

		myList = (ListView) rootView.findViewById(R.id.arrivingLatelist);
		commitButton = (Button) rootView.findViewById(R.id.commitButton);
		
		arrivingLateRecordList = new ArrayList<StudentRecord>();
		adapter = new ArrivingLateRecordArrayAdapter(myList.getContext(), arrivingLateRecordList);
		myList.setAdapter(adapter);

	}

}