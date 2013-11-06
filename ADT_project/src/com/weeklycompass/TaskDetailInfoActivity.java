package com.weeklycompass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailInfoActivity extends Activity {
	static final int VIEW_MODE = 1;
	static final int CREATE_NEW_MODE = 2;
	static final int EDIT_MODE = 3;
	int task_mode; // 1 - view 2 - create new 3 - edit
	Task temp_task = new Task();  //record temp data when edit task detail
	Spinner spinnerTaskStatus, spinnerRole;
	EditText editTextTaskTitle, editTextTaskContent;
	Button buttonEdit, buttonConfirm, buttonCancel;
	TextView textViewTaskId;
	
	private WeeklyCompassDBHelper dbhelper = WeeklyCompassDBHelper.getInstance();
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rock_detail_info);
		spinnerTaskStatus = (Spinner)findViewById(R.id.spinnerTaskStatus);
		spinnerRole = (Spinner)findViewById(R.id.spinnerRole);
		editTextTaskTitle = (EditText)findViewById(R.id.editTextTaskTitle);
		editTextTaskContent = (EditText)findViewById(R.id.editTextTaskContent);
		buttonEdit = (Button)findViewById(R.id.buttonEdit);
		buttonConfirm = (Button)findViewById(R.id.buttonConfirm);
		buttonCancel = (Button)findViewById(R.id.buttonCancel);
		textViewTaskId = (TextView)findViewById(R.id.textViewTaskId);
		
		//prepare task status
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.rock_state, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskStatus.setAdapter(adapter);
		
		//prepare roles
		prepareSpinnerRole();
		
		buttonEdit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				enterEditMode();
				
			}
			
		});
		
		buttonConfirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				updateInfoFromGUIToTempTaskObj();
				Role _role = (Role)spinnerRole.getSelectedItem();
				if(task_mode == EDIT_MODE) // edit mode
				{
				dbhelper.updateTaskInfo(temp_task, _role);			
				updateTaskInfo(textViewTaskId.getText().toString());
				}
				else if(task_mode == CREATE_NEW_MODE) // create new mode
				{
					dbhelper.insertNewTask(temp_task, _role);
					finish();
				}
				enterViewMode();
			}
			
		});
		
		buttonCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(task_mode == EDIT_MODE) // edit mode
				{
				enterViewMode();
				updateTaskInfo(textViewTaskId.getText().toString());
				}
				else if(task_mode == CREATE_NEW_MODE)
				{
					finish();
				}
			}
			
		});
		
		Bundle b = getIntent().getExtras();
		if(b != null)
		{		
			switch(b.getInt("TASK_MODE"))
			{
			case VIEW_MODE: //view info
				enterViewMode();
				updateTaskInfo(b.getString("TASK_ID"));
				break;
			case CREATE_NEW_MODE: //create new task
				enterCreateNewMode();
				break;
			case EDIT_MODE:
				enterEditMode();
				updateTaskInfo(b.getString("TASK_ID"));
				break;
				default:
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		prepareSpinnerRole();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.choose_window_menu, menu);
		menu.findItem(R.id.itemCreateNew).setTitle(R.string.new_role);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this, AddNewRoleActivity.class));
		return true;
	}

	/**
	 * called before view task info
	 */
	private void enterViewMode()
	{
		task_mode = VIEW_MODE;
		spinnerTaskStatus.setEnabled(false);
		spinnerRole.setEnabled(false);
		editTextTaskTitle.setEnabled(false);
		editTextTaskContent.setEnabled(false);
		buttonEdit.setVisibility(Button.VISIBLE);
		buttonConfirm.setVisibility(Button.INVISIBLE);
		buttonCancel.setVisibility(Button.INVISIBLE);
	}
	
	/**
	 * called before edit task
	 */
	private void enterEditMode()
	{
		task_mode = EDIT_MODE;
		spinnerTaskStatus.setEnabled(true);
		spinnerRole.setEnabled(true);
		editTextTaskTitle.setEnabled(true);
		editTextTaskContent.setEnabled(true);
		buttonEdit.setVisibility(Button.INVISIBLE);
		buttonConfirm.setVisibility(Button.VISIBLE);
		buttonCancel.setVisibility(Button.VISIBLE);
	}
	
	private void enterCreateNewMode()
	{
		task_mode = CREATE_NEW_MODE;
		spinnerTaskStatus.setEnabled(true);
		spinnerRole.setEnabled(true);
		editTextTaskTitle.setEnabled(true);
		editTextTaskContent.setEnabled(true);
		buttonEdit.setVisibility(Button.INVISIBLE);
		buttonConfirm.setVisibility(Button.VISIBLE);
		buttonCancel.setVisibility(Button.VISIBLE);
	}
	
	/**
	 * get task info from database based on task id
	 * @param _taskid
	 */
	private void updateTaskInfo(String _taskid)
	{
		Task t = dbhelper.getTaskById(Integer.valueOf(_taskid));
		textViewTaskId.setText(Integer.toString(t.TaskId));
		editTextTaskTitle.setText(t.TaskTitle);
		editTextTaskContent.setText(t.TaskContent);
		spinnerTaskStatus.setSelection(t.TaskStateToInt(t.TaskStatus));
		Role associateRole = dbhelper.getRoleByTaskId(Integer.valueOf(_taskid));
		if(associateRole != null)
		{
			ArrayAdapter<Role> adp = (ArrayAdapter<Role>)spinnerRole.getAdapter();
			spinnerRole.setSelection(adp.getPosition(associateRole));
		}
		updateInfoFromGUIToTempTaskObj();
	}
	
	/**
	 * get all role names from database and add to spinner
	 */
	private void prepareSpinnerRole()
	{
		ArrayAdapter<Role> role_adp = new ArrayAdapter<Role>(this, 
				android.R.layout.simple_spinner_item,
				dbhelper.getAllRoles());
		role_adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerRole.setAdapter(role_adp);
		if(spinnerRole.getCount()==0)
		{
			Toast.makeText(this, R.string.no_role, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * get all data from gui into temp Task object
	 */
	private void updateInfoFromGUIToTempTaskObj()
	{
		if(task_mode == EDIT_MODE) //edit mode
		{
		temp_task.TaskId = Integer.valueOf(textViewTaskId.getText().toString());
		}
		temp_task.TaskTitle = editTextTaskTitle.getText().toString();
		temp_task.TaskContent = editTextTaskContent.getText().toString();
		temp_task.TaskStatus = temp_task.IntToTaskState(spinnerTaskStatus.getSelectedItemPosition());
	}
}
