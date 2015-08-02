package com.bbbandroid.usb;

import com.bbbandroid.usb.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private native int usbInit();
	private native boolean usbGetDevices(int ids[][], byte strings[][][]);
	private native void usbClose();
	
	int count;
	
    TextView tv ;
    
    static {
        System.loadLibrary("BBBAndroidHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView) findViewById(R.id.ConnectionString);
		
		count = usbInit();
		tv.setText("Number of Usb Devices Found : " + count);
		
	}
	
	public void onClickPollDevices(View view)
	{
		tv = (TextView) findViewById(R.id.textView1);
		int ids[][] = new int[count][8];
		byte strings[][][] = new byte[count][3][256];
		String temp;
		
		usbGetDevices(ids, strings);	
		
		String usbDevices = new String();
		usbDevices = "";
		
		for(int i=0; i<count; i++)
		{
			usbDevices = usbDevices + "\n" + Integer.toHexString(ids[i][0]) + ":" + Integer.toHexString(ids[i][1]) + 
					" (bus " +Integer.toString(ids[i][2]) + ", device " + Integer.toString(ids[i][3]) + ")";

			if(ids[i][4]!=-1)
			{
				usbDevices = usbDevices + " path: " + Integer.toString(ids[i][4]);
				for (int j = 1; ids[i][4+j]!=-1; j++)
				{
					usbDevices = usbDevices + "." + Integer.toString(ids[i][4+j]);
				}
			}

			if(strings[i][0][0] != '\0')
			{
				temp = new String(strings[i][0]);
				usbDevices = usbDevices + "\nManufacturer is: " + temp + "\n";
			}
			else
			{
				usbDevices = usbDevices + "\nCoudn't get Manufacturer string\n";
			}

			if(strings[i][1][0] != '\0')
			{
				temp = new String(strings[i][1]);
				usbDevices = usbDevices + "Product is: " + temp + "\n";
			}
			else
			{
				usbDevices = usbDevices + "Coudn't get Product string\n";
			}

			if(strings[i][2][0] != '\0')
			{
				temp = new String(strings[i][2]);
				usbDevices = usbDevices + "SerialNumber is: " + temp + "\n";
			}
			else
			{
				usbDevices = usbDevices + "Coudn't get SerialNumber string\n";
			}

		}
		
		tv.setText("Usb devices found are :\n" + usbDevices);
	}
	
	@Override
	protected void onDestroy()
	{
		usbClose();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}