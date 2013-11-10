package com.weeklycompass;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RockRoleChosenActivity extends Activity {
	/* Obsolete roles are automatically added when adding rocks
	 * static final int SELECT_ROLE_MODE = 1;*/
	static final int SELECT_ROCK_MODE = 2;
	/**
	 * 1 - select role 
	 * 2 - select rock
	 */
	int chooseMode;
	TextView textViewTip;
	ListView listViewItems;
	Button buttonConfirm;
	Button buttonAddNew;
	
	private WeeklyCompassDBHelper dbhelper;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbhelper = WeeklyCompassDBHelper.getInstance();
		
		setContentView(R.layout.rock_role_chosen);
		textViewTip = (TextView)findViewById(R.id.textViewTip);
		listViewItems = (ListView)findViewById(R.id.listViewItems);
		buttonConfirm = new Button(this);
		buttonConfirm.setText(R.string.add_to_this_week);
		listViewItems.addFooterView(buttonConfirm, null, false);
		buttonAddNew = (Button)findViewById(R.id.buttonAddNew);
		
		listViewItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		buttonConfirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {			
				
				switch(chooseMode)
				{
				/*case SELECT_ROLE_MODE: // select role
					SparseBooleanArray _selected = listViewItems.getCheckedItemPositions();
					ArrayList<Role> temp_roles = new ArrayList<Role>();
					for(int i=0; i<listViewItems.getCount(); i++)
					{
						if(_selected.get(i))
						{
							temp_roles.add((Role)listViewItems.getItemAtPosition(i));
						}
					}
					dbhelper.addRolesToWeeklyTable(
							dbhelper.getWeekTableName(), temp_roles);
					break;*/
				case SELECT_ROCK_MODE: //select rock
					HeaderViewListAdapter tt = (HeaderViewListAdapter)listViewItems.getAdapter();
					RockChooseAdapter adp = (RockChooseAdapter)tt.getWrappedAdapter();
					for(int i=0; i<adp.getCount(); i++)
					{
						if(adp.getmSelected().get(i) == true)
						{
							Task t = (Task)adp.getItem(i);
							if(!WeekPlanSession.getInstance().isTaskOfCurrentWeek(t))
							{
								// a new selected big rock
								dbhelper.addTaskToWeeklyTable(dbhelper.getWeekTableName(), t);
							}
						}
						else
						{
							Task t = (Task)adp.getItem(i);
							if(WeekPlanSession.getInstance().isTaskOfCurrentWeek(t))
							{
								// an old rock is unselected
								dbhelper.removeBigRockFromWeeklyTableById(t.TaskId);
							}
						}
					}
					break;
					default:
				}
				WeekPlanSession.getInstance().refreshData();
				finish();
			}
			
		});
		
		buttonAddNew.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				switch(chooseMode)
				{
				/*case SELECT_ROLE_MODE:
					launchNewRoleGUI();
					break;*/
				case SELECT_ROCK_MODE:
					launchNewRockGUI();
					break;
					default:
				}				
			}
			
		});
		
		Bundle b = getIntent().getExtras();
		chooseMode = b.getInt("CHOOSE_MODE");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		switch(chooseMode)
		{
		/*case SELECT_ROLE_MODE:
			enterRoleMode();
			break;*/
		case SELECT_ROCK_MODE:
			enterRockMode();
			break;
			default:
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.choose_window_menu, menu);
		switch(chooseMode)
		{
		/*case SELECT_ROLE_MODE:
			menu.findItem(R.id.itemCreateNew).setTitle(R.string.new_role);
			break;*/
		case SELECT_ROCK_MODE:
			menu.findItem(R.id.itemCreateNew).setTitle(R.string.new_rock);
			break;
			default:
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(chooseMode)
		{
		/*case SELECT_ROLE_MODE:
			launchNewRoleGUI();
			break;*/
		case SELECT_ROCK_MODE:
			launchNewRockGUI();
			break;
		default:
			return false;
		}
		return true;
	}

	/**
	 * load ListView with Tasks
	 */
	private void enterRockMode()
	{
		textViewTip.setText(getString(R.string.choose_rock));
		ArrayList<Task> rocks = dbhelper.getAllTasks();
		RockChooseAdapter dp = new RockChooseAdapter(this, rocks);
		listViewItems.setAdapter(dp);
		buttonAddNew.setText(getString(R.string.new_rock));
		if(listViewItems.getCount()==0)
		{
			Toast.makeText(this, R.string.no_rocks, Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this, R.string.create_rock_tip, Toast.LENGTH_LONG).show();
		}		
	}
	
	/**
	 * load ListView with Roles
	 */
	/*private void enterRoleMode()
	{
		textViewTip.setText(getString(R.string.choose_role));
		ArrayList<Role> roles = dbhelper.getAllRoles();
		ArrayAdapter<Role> adp = new ArrayAdapter<Role>(this, android.R.layout.simple_list_item_multiple_choice, roles);
		listViewItems.setAdapter(adp);
		buttonAddNew.setText(getString(R.string.new_role));
		if(listViewItems.getCount()==0)
		{
			Toast.makeText(this, R.string.no_role, Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this, R.string.create_role_tip, Toast.LENGTH_LONG).show();
		}
	}
	
	private void launchNewRoleGUI()
	{
		startActivity(new Intent(this, AddNewRoleActivity.class));
	}*/
	
	private void launchNewRockGUI()
	{
		Bundle b = new Bundle();
		b.putInt("TASK_MODE", TaskDetailInfoActivity.CREATE_NEW_MODE);
		Intent i = new Intent(this, TaskDetailInfoActivity.class);
		i.putExtras(b);
		startActivity(i);
	}
}
