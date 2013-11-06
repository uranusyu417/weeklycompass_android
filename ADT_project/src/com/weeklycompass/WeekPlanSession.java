package com.weeklycompass;

import java.util.ArrayList;

public class WeekPlanSession {
	
	private ArrayList<Role> SelectedRoles;
	private ArrayList<Task> SelectedRocks;

	public ArrayList<Role> getSelectedRoles() {
		return SelectedRoles;
	}

	public ArrayList<Task> getSelectedRocks() {
		return SelectedRocks;
	}

	private static WeekPlanSession instance;
	
	public static WeekPlanSession getInstance()
	{
		if(instance == null)
		{
			instance = new WeekPlanSession();
			instance.SelectedRoles = WeeklyCompassDBHelper.getInstance().getSelectedRolesOfCurrentWeek();
			instance.SelectedRocks = WeeklyCompassDBHelper.getInstance().getSelectedRocksOfCurrentWeek();
		}
		return instance;
	}
	
	public boolean isTaskOfCurrentWeek(Task t)
	{
		return SelectedRocks.contains(t);
	}
	
	public void refreshData()
	{
		if(instance != null)
		{
			instance.SelectedRoles = WeeklyCompassDBHelper.getInstance().getSelectedRolesOfCurrentWeek();
			instance.SelectedRocks = WeeklyCompassDBHelper.getInstance().getSelectedRocksOfCurrentWeek();
		}
	}
	
}
