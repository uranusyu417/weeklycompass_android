package com.weeklycompass;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddNewRoleActivity extends Activity {
	Button buttonConfirm;
	EditText editTextRoleName;
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_role);
		buttonConfirm = (Button)findViewById(R.id.buttonConfirm);
		editTextRoleName = (EditText)findViewById(R.id.editTextRoleName);
		
		buttonConfirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				WeeklyCompassDBHelper dbhelper = WeeklyCompassDBHelper.getInstance();
				if(dbhelper != null)
				{
					dbhelper.insertNewRole(editTextRoleName.getText().toString());
				}
				finish();
			}
			
		});
	}
}
