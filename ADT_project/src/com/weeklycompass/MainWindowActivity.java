package com.weeklycompass;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainWindowActivity extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weekly_compass_mainwindow);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		setupDate();
		loadRocks();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_window_menu, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		startToChoose(item.getItemId());
		return true;
	}
	
	/**
	 * determine select role or rocks
	 * @param resId selected item resource id
	 */
	private void startToChoose(int resId)
	{
		Bundle b = new Bundle();
		switch(resId)
		{
		case R.id.itemSelectRole:
			b.putInt("CHOOSE_MODE", RockRoleChosenActivity.SELECT_ROLE_MODE);
			break;
		case R.id.itemSelectRock:
			b.putInt("CHOOSE_MODE", RockRoleChosenActivity.SELECT_ROCK_MODE);
			break;
			default:
		}
		Intent i = new Intent(this, RockRoleChosenActivity.class);
		i.putExtras(b);
		startActivity(i);
	}

	/**
	 * display today's date
	 */
	private void setupDate()
	{
		TextView date = (TextView)findViewById(R.id.textViewDate);
		Calendar cal = Calendar.getInstance();
		//year
		String dateStr = "  Date:"+
		    cal.get(Calendar.YEAR)+"-";
		//month
		switch(cal.get(Calendar.MONTH))
		{
		case Calendar.JANUARY:
			dateStr += "1";
			break;
		case Calendar.FEBRUARY:
			dateStr+="2";
			break;
		case Calendar.MARCH:
			dateStr+="3";
			break;
		case Calendar.APRIL:
			dateStr+="4";
			break;
		case Calendar.MAY:
			dateStr+="5";
			break;
		case Calendar.JUNE:
			dateStr+="6";
			break;
		case Calendar.JULY:
			dateStr+="7";
			break;
		case Calendar.AUGUST:
			dateStr+="8";
			break;
		case Calendar.SEPTEMBER:
			dateStr+="9";
			break;
		case Calendar.OCTOBER:
			dateStr+="10";
			break;
		case Calendar.NOVEMBER:
			dateStr+="11";
			break;
		case Calendar.DECEMBER:
			dateStr+="12";
			break;
			default:
		}
		//day of month
		dateStr +="-"+cal.get(Calendar.DAY_OF_MONTH)+"  ";
		//day of week
		switch(cal.get(Calendar.DAY_OF_WEEK))
		{
		case Calendar.SUNDAY:
			dateStr += "Sunday";
			break;
		case Calendar.MONDAY:
			dateStr += "Monday";
			break;
		case Calendar.TUESDAY:
			dateStr += "Tuesday";
			break;
		case Calendar.WEDNESDAY:
			dateStr += "Wednesday";
			break;
		case Calendar.THURSDAY:
			dateStr += "Thursday";
			break;
		case Calendar.FRIDAY:
			dateStr += "Friday";
			break;
		case Calendar.SATURDAY:
			dateStr += "Saturday";
			break;
			default:
		}
		date.setText(dateStr);
	}
	
	/**
	 * read this week's big rocks and display them grouped by role
	 */
	private void loadRocks()
	{
		WeeklyCompassDBHelper dbhelper = WeeklyCompassDBHelper.getInstance();
		if(dbhelper!=null)
		{
			ArrayList<Role> weeklyRoles = dbhelper.getRolesFromWeekTable(
					dbhelper.getWeekTableName());
			LinearLayout l = (LinearLayout)findViewById(R.id.linearLayoutRocks);
			l.removeAllViews();
			if(weeklyRoles.size()>0)
			{
				for(int i=0; i<weeklyRoles.size(); i++)
				{
					RoleRocksLayout rrl = new RoleRocksLayout(getApplicationContext());
					rrl.setRoleInfo(weeklyRoles.get(i));
					rrl.setRocks(
							dbhelper.getTasksFromWeekTableBasedOnRoleId(
									dbhelper.getWeekTableName(), 
									weeklyRoles.get(i).RoleId)
								);				
					l.addView(rrl);
				}
			}
			else
			{
				showEmptyList();
			}
		}
		else
		{
			showEmptyList();
		}
	}
	
	/**
	 * no big rocks in this week
	 */
	private void showEmptyList()
	{
		LinearLayout l = (LinearLayout)findViewById(R.id.linearLayoutRocks);
		l.removeAllViews();
		Toast.makeText(this, R.string.no_big_rocks, Toast.LENGTH_LONG).show();
	}
}
