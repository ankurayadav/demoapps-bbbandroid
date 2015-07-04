package com.bbbandroidHAL.i2c;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bbbandroidHAL.i2c.R;

public class MainActivity extends Activity {
	public final static String PACKT_TAG = "com.packt";

    private native int i2cOpenAdaptor(int adaptorNumber);
    private native boolean i2cSetSlave(int i2cFD, int adress);
    private native boolean i2cSetAddress(int i2cFD, byte i);
    private native boolean i2cWriteByte(int i2cFD, byte data);
    private native boolean i2cWriteBytes(int i2cFD, byte data[]);
    private native int i2cReadByte(int i2cFD);
    private native boolean i2cReadBytes(int i2cFD, byte data[]);
    private native void i2cClose(int i2cFD);

    static {
        System.loadLibrary("packtHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView tv = (TextView) findViewById(R.id.button_state);
        tv.setText("Slider State: UNKNOWN");
		
        /* Opening Sensor */
        
		int i2cFD = i2cOpenAdaptor(1); 
		
		if(i2cFD!=-1)
		{
			if(i2cSetSlave(i2cFD, 0x53))
			{
				if(i2cSetAddress(i2cFD, (byte) 0x00))
				{
					int value = i2cReadByte(i2cFD);
				
					tv.setText("Connected to i2c-1 with FD :" + i2cFD + " Slave :" + 0x53 + 
							" ChipID Address :0x00 ChipID :" + value);
				}
				
			}
		}	
		
		
	}
	
	public void onClickButtonPollStatus(View view)
	{
		TextView tv = (TextView) findViewById(R.id.button_state);
        tv.setText("Slider State: UNKNOWN");
        
    	//int value = readADC(5);
    		
    	//String status = Integer.toString(value);
    		
        //tv.setText("Slider State: " + status);
	}
}
