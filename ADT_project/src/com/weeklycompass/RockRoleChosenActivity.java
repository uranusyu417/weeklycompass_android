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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RockRoleChosenActivity extends Activity {
	static final int SELECT_ROLE_MODE = 1;
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

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rock_role_chosen);
		textViewTip = (TextView)findViewById(R.id.textViewTip);
		listViewItems = (ListView)findViewById(R.id.listViewItems);
		buttonConfirm = (Button)findViewById(R.id.buttonConfirm);
		buttonAddNew = (Button)findViewById(R.id.buttonAddNew);
		
		listViewItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		buttonConfirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				SparseBooleanArray _selected = listViewItems.getCheckedItemPositions();
				
				switch(chooseMode)
				{
				case SELECT_ROLE_MODE: // select role
					ArrayList<Role> temp_roles = new ArrayList<Role>();
					for(int i=0; i<listViewItems.getCount(); i++)
					{
						if(_selected.get(i))
						{
							temp_roles.add((Role)listViewItems.getItemAtPosition(i));
						}
					}
					MainActivity.dbhelper.addRolesToWeeklyTable(
							MainActivity.dbhelper.getWeekTableName(), temp_roles);
					break;
				case SELECT_ROCK_MODE: //select rock
					ArrayList<Task> temp_rocks = new ArrayList<Task>();
					for(int i=0; i<listViewItems.getCount(); i++)
					{
						if(_selected.get(i))
						{
							temp_rocks.add((Task)listViewItems.getItemAtPosition(i));
						}
					}
					MainActivity.dbhelper.addTasksToWeeklyTable(
							MainActivity.dbhelper.getWeekTableName(), temp_rocks);
					break;
					default:
				}
				finish();
			}
			
		});
		
		buttonAddNew.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				switch(chooseMode)
				{
				case SELECT_ROLE_MODE:
					launchNewRoleGUI();
					break;
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
		case SELECT_ROLE_MODE:
			enterRoleMode();
			break;
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
		case SELECT_ROLE_MODE:
			menu.findItem(R.id.itemCreateNew).setTitle(R.string.new_role);
			break;
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
		case SELECT_ROLE_MODE:
			launchNewRoleGUI();
			break;
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
		ArrayList<Task> rocks = MainActivity.dbhelper.getAllTasks();
		ArrayAdapter<Task> adp = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_multiple_choice, rocks);
		listViewItems.setAdapter(adp);
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
	private void enterRoleMode()
	{
		textViewTip.setText(getString(R.string.choose_role));
		ArrayList<Role> roles = MainActivity.dbhelper.getAllRoles();
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
	}
	
	private void launchNewRockGUI()
	{
		Bundle b = new Bundle();
		b.putInt("TASK_MODE", TaskDetailInfoActivity.CREATE_NEW_MODE);
		Intent i = new Intent(this, TaskDetailInfoActivity.class);
		i.putExtras(b);
		startActivity(i);
	}
}
