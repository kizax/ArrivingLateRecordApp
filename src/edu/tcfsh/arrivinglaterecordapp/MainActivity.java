/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.tcfsh.arrivinglaterecordapp;

import edu.tcfsh.arrivinglaterecordapp.R;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener,
		SearchingStudentFragment.OnSearchingResultSelectedListener,
		ArrivingLateRecordFragment.OnArrivingLateRecordSelectedListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private Bundle bundle;
	private int dayOfMonth;
	private int month;
	private int year;

	ViewPager mViewPager;
	static ArrivingLateRecordFragment arrivingLateRecordFragment;
	Builder savingFileAlertDialog;
	Builder leavingActivityDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		getBundle();
		arrivingLateRecordFragment = new ArrivingLateRecordFragment(dayOfMonth,
				month, year);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		
		leavingActivityDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		leavingActivityDialog.setTitle("提示");
		leavingActivityDialog.setMessage("確定離開?");
		leavingActivityDialog.setPositiveButton("確定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						savingFileAlertDialog.show();

					}
				});
		leavingActivityDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		
		
		savingFileAlertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		savingFileAlertDialog.setTitle("提示");
		savingFileAlertDialog.setMessage("離開前是否要儲存遲到紀錄?");
		savingFileAlertDialog.setPositiveButton("確定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						arrivingLateRecordFragment.saveArrivingLateRecordFile();
						finish();
					}
				});
		savingFileAlertDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				});


		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);

					}

				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

	}

	@Override
	public void onStudentRecordSelected(StudentRecord studentRecord) {
		if (arrivingLateRecordFragment.updateList(studentRecord)) {
			showToast(studentRecord.toString() + " 遲到!");
		} else {
			showToast(studentRecord.toString() + " \n已在遲到紀錄中");
		}

	}

	private void getBundle() {
		bundle = this.getIntent().getExtras();
		dayOfMonth = bundle.getInt("DayOfMonth");
		month = bundle.getInt("Month");
		year = bundle.getInt("Year");
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				// The first section of the app is the most interesting -- it
				// offers
				// a launchpad into the other demonstrations in this example
				// application.
				return new SearchingStudentFragment();

			case 1:
				// The first section of the app is the most interesting -- it
				// offers
				// a launchpad into the other demonstrations in this example
				// application.
				return arrivingLateRecordFragment;

			default:
				// The other sections of the app are dummy placeholders.
				return new SearchingStudentFragment();
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
			case 0:
				return "學號/班號查詢";

			case 1:
				// The first section of the app is the most interesting -- it
				// offers
				// a launchpad into the other demonstrations in this example
				// application.
				return "遲到紀錄";

			default:
				// The other sections of the app are dummy placeholders.
				return "dummy";
			}

		}
	}

	private void showToast(String msg) {
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	public void onBackPressed() {
		leavingActivityDialog.show();
	}

	@Override
	public void onSaveFileButtonSelected(String msg) {
		showToast(msg);
	}

}
