package com.packt.gpio;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static String PACKT_TAG = "com.packt";
	
	private native boolean openGPIO();
	private native void closeGPIO();
    private native boolean readGPIO(int header, int pin);
    private native void writeGPIO(int header, int pin, int val);

    static {
        System.loadLibrary("packtHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView tv = (TextView) findViewById(R.id.button_state);
        tv.setText("Button State: UNKNOWN");
        
        if(openGPIO() == false) {
        	Log.e(PACKT_TAG, "Unable to open GPIO.");
        	finish();
        }
        
/* blink LED
        while(1==1)
        { 	
        writeGPIO(9, 14, 0);
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        writeGPIO(9, 14, 1);
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
 */
        while(1==1)
        {
        	//if(readGPIO(8, 19) == true)
        	{
        		writeGPIO(9, 11, 1);
        	}
        	//else
        	try {
    			Thread.sleep(1000);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

        	{
        		writeGPIO(9, 11, 0);
        	}
            
            try {
    			Thread.sleep(1000);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        
	}

	@Override
	protected void onDestroy()
	{
		closeGPIO();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClickButtonPollStatus(View view)
	{
		String status = readGPIO(9, 13) == true ? "ON" : "OFF";
		TextView tv = (TextView) findViewById(R.id.button_state);
        tv.setText("Button State: " + status);
	}
	
	public void onClickButtonLightOn(View view)
	{
		//writeGPIO(9, 11, 1);
		writeGPIO(8, 7, 1);
	}
	
	public void onClickButtonLightOff(View view)
	{
		//writeGPIO(9, 11, 0);
		writeGPIO(8, 7, 0);
	}
}
