package edu.tcfsh.arrivinglaterecordapp;


import java.util.Calendar;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;


public class SetRecordingDateActivity extends Activity {

	private DatePicker datePicker;
	private Button confirmButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_date_activity);
		initialize();
		initializeDatePicker();
		setListener();

	}

	private void initialize() {
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		confirmButton = (Button) findViewById(R.id.confirmButton);

	}
	
	private void initializeDatePicker() {
		Calendar cal=Calendar.getInstance();

		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH);
		int day=cal.get(Calendar.DAY_OF_MONTH);

		datePicker.updateDate(year, month, day);

	}
	
	private void setListener() {
		confirmButton.setOnClickListener(confirmButtonOnClickListener);
		
	}


	private Button.OnClickListener confirmButtonOnClickListener = new Button.OnClickListener(){
		@Override
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(SetRecordingDateActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			
			bundle.putInt("DayOfMonth", datePicker.getDayOfMonth());
			bundle.putInt("Month", datePicker.getMonth()+1);
			bundle.putInt("Year", datePicker.getYear());
			
			intent.putExtras(bundle);
			startActivity(intent);
			SetRecordingDateActivity.this.finish();
		}
	};
    
}
