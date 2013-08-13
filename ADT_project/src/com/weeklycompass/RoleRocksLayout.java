package com.weeklycompass;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RoleRocksLayout extends LinearLayout implements OnItemClickListener
{
	
	TextView textViewRoleName;
	TextView textViewRoleId;
	ListView listViewRocks;

	public RoleRocksLayout(Context context) {
		super(context);
		postInit(context);
	}

	public RoleRocksLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		postInit(context);
	}
	
	private void postInit(Context context)
	{
		this.addView(inflate(context, R.layout.role_task_view, (ViewGroup)this.getChildAt(0)));
		textViewRoleName = (TextView)findViewById(R.id.textViewRoleName);
		textViewRoleId = (TextView)findViewById(R.id.textViewRoleId);
		listViewRocks = (ListView)findViewById(R.id.listViewRocks);
		TextView tv = new TextView(context);
		tv.setText(R.string.rock);
		listViewRocks.addHeaderView(tv, null, false);
		listViewRocks.setOnItemClickListener(this);
	}
	
	void setRoleInfo(Role role)
	{
		textViewRoleId.setText(Integer.toString(role.RoleId));
		textViewRoleName.setText(role.RoleName);
	}
	
	void setRocks(ArrayList<Task> rocks)
	{
		ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<rocks.size();i++)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("textViewRockId", Integer.toString(rocks.get(i).TaskId));
			map.put("textViewRockTitle", rocks.get(i).TaskTitle);
			listItems.add(map);
		}
		listViewRocks.setAdapter(new SimpleAdapter(getContext(), 
				                                   listItems, 
				                                   R.layout.rock_item, 
				                                   new String[] {"textViewRockId", "textViewRockTitle"}, 
				                                   new int[] {R.id.textViewRockId, R.id.textViewRockTitle}));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		LinearLayout ll = (LinearLayout)arg1;
		TextView tvid = (TextView)ll.getChildAt(0);
		Intent i = new Intent(getContext().getApplicationContext(), TaskDetailInfoActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle b = new Bundle();
		b.putInt("TASK_MODE", TaskDetailInfoActivity.VIEW_MODE);
		b.putString("TASK_ID", tvid.getText().toString());
		i.putExtras(b);
		getContext().startActivity(i);
	}

}
