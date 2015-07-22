package com.bbbandroid.can;

import com.bbbandroid.can.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

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
	
	public void onClickSend(View view)
	{
		EditText et = (EditText)findViewById(R.id.editText1);
		byte[] send =  et.getText().toString().getBytes();
		
		hwTask.SendBytes(send.length,send);
		
	}
	
	public void onResume(){
		super.onResume();
		//	Create	our	background	hardware	communication	thread
		hwTask = new HardwareTask();
		hwTask.pollHardware(this);
		
	}
	
	@Override
	protected void onDestroy()
	{
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}