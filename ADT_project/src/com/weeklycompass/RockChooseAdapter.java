/**
 * used for rock choosing list view
 * show rock title, corresponding role, and check box
 */
package com.weeklycompass;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @author ebbbyyy
 *
 */
public class RockChooseAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<Task> mTasks;
	HashMap<Integer, Boolean> mSelected;
	
	public RockChooseAdapter(Context context, ArrayList<Task> tasks)
	{
		mContext = context;
		mTasks = tasks;
		mSelected = new HashMap<Integer, Boolean>();
		for(int i=0;i<mTasks.size();i++)
		{
			mSelected.put(i, false);
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mTasks.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return mTasks.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = View.inflate(mContext, R.layout.rock_choose_listview_item, null);
		TextView title = (TextView)view.findViewById(R.id.textViewListViewItemTitle);
		TextView role = (TextView)view.findViewById(R.id.textViewListViewItemRole);
		CheckBox check = (CheckBox)view.findViewById(R.id.checkBoxRockSelection);
		Task task = mTasks.get(position);
		
		title.setText(task.TaskTitle);
		
		Role r = WeeklyCompassDBHelper.getInstance().getRoleByTaskId(task.TaskId);
		if(r != null)
		{
			role.setText(r.RoleName);
		}
		else
		{
			role.setText("");
		}
		
		if(WeekPlanSession.getInstance().isTaskOfCurrentWeek(task))
		{
			check.setChecked(true);
			mSelected.put(position, true);
		}
		else
		{
			check.setChecked(false);
		}
		
		check.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox)v;
				if(cb.isChecked())
				{
					mSelected.put(position, true);
				}
				else
				{
					mSelected.put(position, false);
				}
			}});
		
		return view;
	}

	/**
	 * @return the mSelected
	 */
	public HashMap<Integer, Boolean> getmSelected() {
		return mSelected;
	}
}
