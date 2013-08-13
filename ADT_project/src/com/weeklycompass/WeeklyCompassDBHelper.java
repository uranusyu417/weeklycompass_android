package com.weeklycompass;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeeklyCompassDBHelper extends SQLiteOpenHelper {
	
	private SQLiteDatabase db;

	public WeeklyCompassDBHelper(Context context, String _dbname, int _dbversion) {
		// dbversion should be consistent with attribute "android:versionCode"
		super(context, _dbname, null, _dbversion);
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		String CreateDBStr = 		
				"CREATE TABLE IF NOT EXISTS " +
				"roles " +
				"(role_id INTEGER PRIMARY KEY ASC AUTOINCREMENT, " +
				" role_name TEXT UNIQUE" +
				"); ";
		arg0.execSQL(CreateDBStr);
		
		CreateDBStr	=
				"CREATE TABLE IF NOT EXISTS " +
				"tasks " +
				"(task_id INTEGER PRIMARY KEY ASC AUTOINCREMENT, " +
				" task_title TEXT NOT NULL, " +
				" task_content TEXT, " +
				" task_status INTEGER DEFAULT 0, " +
				" role_id INTEGER REFERENCE roles" +
				"); " ;
		arg0.execSQL(CreateDBStr);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * crate week table to record roles&tasks belong to current week
	 * @param _tableName
	 */
	public void createWeeklyTasks(String _tableName)
	{
		String str = 
				"CREATE TABLE IF NOT EXISTS " +
				_tableName + " " +
				"(id_type INTEGER NOT NULL, " +
				" id INTEGER NOT NULL, " +
				" PRIMARY KEY (id_type, id)" +
				");";
		db.execSQL(str);
	}
	/**
	 * get table name for current week
	 * @return table name string "wkx_xxxx"
	 */
	public String getWeekTableName()
	{
		Calendar cal = Calendar.getInstance();
		return "wk" + Integer.toString(cal.get(Calendar.WEEK_OF_YEAR))
		+ "_" + Integer.toString(cal.get(Calendar.YEAR));
	}

	/**
	 * get Role objects belong to this week
	 * @param week_table_name
	 * @return ArrayList<Role>
	 */
	public ArrayList<Role> getRolesFromWeekTable(String week_table_name)
	{
		String str = "SELECT " +
				     "roles.role_id, roles.role_name " +
				     "FROM " +
				     "roles, " + week_table_name +" " +
				     "WHERE " +
				     week_table_name + ".id_type=1 AND " +
				     week_table_name + ".id=roles.role_id " +
				     "";
		ArrayList<Role> roles = new ArrayList<Role>();
		Cursor cursor = db.rawQuery(str, null);
		if(cursor.getCount() > 0)
		{		
			while(cursor.moveToNext())
			{
				Role _role = new Role();
				_role.RoleId = cursor.getInt(cursor.getColumnIndex("roles.role_id"));
				_role.RoleName = cursor.getString(cursor.getColumnIndex("roles.role_name"));
				roles.add(_role);
			}
		}
		cursor.close();
		return roles;
	}
	
	/**
	 * get Task objects belong to this week based on a given role id
	 * @param week_table_name
	 * @param _role_id
	 * @return ArrayList<Task>
	 */
	public ArrayList<Task> getTasksFromWeekTableBasedOnRoleId(String week_table_name, int _role_id)
	{
		String str = "SELECT " +
				     "tasks.task_id, tasks.task_title, tasks.task_content, tasks.task_status " +
				     "FROM " +
				     "tasks, " + week_table_name +" " +
				     "WHERE " +
				     week_table_name + ".id_type=0 AND " +
				     week_table_name + ".id=tasks.task_id AND " +
				     "tasks.role_id=" + _role_id;
		ArrayList<Task> tasks = new ArrayList<Task>();
		Cursor cursor = db.rawQuery(str, null);
		if(cursor.getCount()>0)
		{
			while(cursor.moveToNext())
			{
				Task _task = new Task();
				_task.TaskId = cursor.getInt(cursor.getColumnIndex("tasks.task_id"));
				_task.TaskTitle = cursor.getString(cursor.getColumnIndex("tasks.task_title"));
				_task.TaskContent = cursor.getString(cursor.getColumnIndex("tasks.task_content"));
				_task.TaskStatus = _task.IntToTaskState(cursor.getInt(cursor.getColumnIndex("tasks.task_status")));
				tasks.add(_task);
			}
		}
		cursor.close();
		return tasks;
	}
	
	/**
	 * get task object based task id
	 * @param taskid
	 * @return Task or null for not found
	 */
	public Task getTaskById(int taskid)
	{
		String str = "SELECT * FROM tasks WHERE task_id="+taskid;
		Cursor cursor = db.rawQuery(str, null);
		if(cursor.moveToNext())
		{
			Task _task = new Task();
			_task.TaskId = cursor.getInt(cursor.getColumnIndex("tasks.task_id"));
			_task.TaskTitle = cursor.getString(cursor.getColumnIndex("tasks.task_title"));
			_task.TaskContent = cursor.getString(cursor.getColumnIndex("tasks.task_content"));
			_task.TaskStatus = _task.IntToTaskState(cursor.getInt(cursor.getColumnIndex("tasks.task_status")));
			cursor.close();
			return _task;
		}
		else
		{
		cursor.close();
		return null;
		}
	}
	
	/**
	 * get all role objects from database
	 * @return ArrayList<Role>
	 */
	public ArrayList<Role> getAllRoles()
	{
		String str = "SELECT * from roles";
		Cursor cursor = db.rawQuery(str, null);
		ArrayList<Role> roles = new ArrayList<Role>();
		while(cursor.moveToNext())
		{
			Role _role = new Role();
			_role.RoleId = cursor.getInt(cursor.getColumnIndex("role_id"));
			_role.RoleName = cursor.getString(cursor.getColumnIndex("role_name"));
			roles.add(_role);
		}
		cursor.close();
		return roles;
	}
	
	/**
	 * get all task objects from database
	 * @return ArrayList<Task>
	 */
	public ArrayList<Task> getAllTasks()
	{
		String str = "SELECT * from tasks";
		Cursor cursor = db.rawQuery(str, null);
		ArrayList<Task> tasks = new ArrayList<Task>();
		while(cursor.moveToNext())
		{
			Task _task = new Task();
			_task.TaskId = cursor.getInt(cursor.getColumnIndex("task_id"));
			_task.TaskTitle = cursor.getString(cursor.getColumnIndex("task_title"));
			_task.TaskContent = cursor.getString(cursor.getColumnIndex("task_content"));
			_task.TaskStatus = _task.IntToTaskState(cursor.getInt(cursor.getColumnIndex("task_status")));
			tasks.add(_task);
		}
		cursor.close();
		return tasks;
	}
	
	/**
	 * get associated role by task id
	 * @param _taskid
	 * @return associated Role object or null for not found
	 */
	public Role getRoleByTaskId(int _taskid)
	{
		String str = "SELECT roles.role_id, roles.role_name " +
				     "FROM roles, tasks " +
				     "WHERE tasks.task_id="+_taskid+" AND tasks.role_id=roles.role_id";
		Cursor cursor = db.rawQuery(str, null);
		if(cursor.moveToNext())
		{
			Role _role = new Role();
			_role.RoleId = cursor.getInt(cursor.getColumnIndex("roles.role_id"));
			_role.RoleName = cursor.getString(cursor.getColumnIndex("roles.role_name"));
			cursor.close();
			return _role;
		}
		else
		{
			cursor.close();
		    return null;
		}
	}
	
	/**
	 * get role id by role name
	 * @param _role_name
	 * @return role id or 0 for not found
	 */
	public int getRoleIdByName(String _role_name)
	{
		String str = "SELECT roles.role_id" +
			         "FROM roles" +
			         "WHERE roles.role_name=\""+_role_name+"\"";
		Cursor cursor = db.rawQuery(str, null);
		if(cursor.moveToNext())
		{
			int _id = cursor.getInt(cursor.getColumnIndex("roles.role_id"));
			cursor.close();
			return _id;
		}
		else
		{
			cursor.close();
			return 0;
		}
	}
	
	/**
	 * update task info in database
	 * @param t Task object
	 * @param r associated Role object or null
	 */
	public void updateTaskInfo(Task t, Role r)
	{
		if(t == null)
		{
			return ;
		}
		ContentValues c = new ContentValues();
		c.put("task_title", t.TaskTitle);
		c.put("task_content", t.TaskContent);
		c.put("task_status", t.TaskStateToInt(t.TaskStatus));
		if(r != null)
		{		
			c.put("role_id", r.RoleId);
		}
		db.update("tasks", c, "task_id=?", new String[] {Integer.toString(t.TaskId)});
	}
	
	/**
	 * insert a new task into database
	 * @param t Task object
	 * @param r associated Role object
	 */
	public void insertNewTask(Task t, Role r)
	{
		if(t == null || r == null)
			return ;
		ContentValues c = new ContentValues();
		c.put("task_title", t.TaskTitle);
		c.put("task_content", t.TaskContent);
		c.put("task_status", t.TaskStateToInt(t.TaskStatus));
		c.put("role_id", r.RoleId);
		db.insert("tasks", null, c);
	}
	
	/**
	 * add a task object into week table, also add associated role into table
	 * @param _table weekly table name
	 * @param t Task object
	 */
	public void addTaskToWeeklyTable(String _table, Task t)
	{
		if(_table == null || t == null)
			return;
		ContentValues c = new ContentValues();
		c.put("id_type", 0);
		c.put("id", t.TaskId);
		db.insert(_table, null, c);
		Role r = getRoleByTaskId(t.TaskId);
		addRoleToWeeklyTable(_table, r);
	}
	
	public void addTasksToWeeklyTable(String _table, ArrayList<Task> ts)
	{
		for(int i=0;i<ts.size();i++)
		{
			addTaskToWeeklyTable(_table, ts.get(i));
		}
	}
	
	/**
	 * add a role to week table
	 * @param _table week table name
	 * @param r Role object
	 */
	public void addRoleToWeeklyTable(String _table, Role r)
	{
		if(_table == null || r == null)
			return;
		ContentValues c = new ContentValues();
		c.put("id_type", 1);
		c.put("id", r.RoleId);
		db.insert(_table, null, c);
	}
	
	public void addRolesToWeeklyTable(String _table, ArrayList<Role> rs)
	{
		for(int i=0;i<rs.size();i++)
		{
			addRoleToWeeklyTable(_table, rs.get(i));
		}
	}
	
	/**
	 * insert a new role into database
	 * @param role_name
	 */
	public void insertNewRole(String role_name)
	{
		if(role_name == null)
			return;
		ContentValues c = new ContentValues();
		c.put("role_name", role_name);
		db.insert("roles", null, c);
	}
}
