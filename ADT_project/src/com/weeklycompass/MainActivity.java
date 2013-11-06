package com.weeklycompass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	ProgressBar loadingpb = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadingpb = (ProgressBar)findViewById(R.id.progressBarLoading);          
        
        try {
        	// dbversion should be consistent with attribute "android:versionCode"
            int dbversion = 0;
            String dbname = getString(R.string.DB_NAME);
        	dbversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        	WeeklyCompassDBHelper dbhelper = WeeklyCompassDBHelper.Initialize(getApplicationContext(), dbname, dbversion);
			if( dbhelper != null)
			{
				dbhelper.createWeeklyTasks(dbhelper.getWeekTableName());
			}
			startActivityForResult(new Intent(getApplicationContext(), MainWindowActivity.class), 0);
		}catch (Exception e){
		}
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		WeeklyCompassDBHelper dbhelper = WeeklyCompassDBHelper.getInstance();
		if(dbhelper!=null)
		{
			dbhelper.close();
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}
}