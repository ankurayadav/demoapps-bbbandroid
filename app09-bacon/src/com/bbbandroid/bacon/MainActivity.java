package com.bbbandroid.bacon;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bbbandroid.bacon.R;

public class MainActivity extends Activity {
	
	
    public static HardwareTask hwTask ;

    static {
        System.loadLibrary("BBBAndroidHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}
	
    @Override
    public void onPause() {
        super.onPause();

        // Release the hardware when the app is paused
        if (hwTask != null) {
        	hwTask.cancel(true);
        	hwTask = null;
        }
    }
    
	public void onClickPollDevices(View view)
	{
		hwTask.PollDevices();	
	}
    
	public void onResume(){
		super.onResume();
		//	Create	our	background	hardware	communication	thread
		hwTask = new HardwareTask();
		hwTask.pollHardware(this);
		
	}
	
	
}
