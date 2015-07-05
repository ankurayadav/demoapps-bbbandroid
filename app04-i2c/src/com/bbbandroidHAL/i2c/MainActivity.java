package com.bbbandroidHAL.i2c;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bbbandroidHAL.i2c.R;

public class MainActivity extends Activity {

    private native int i2cOpenAdaptor(int adaptorNumber);
    private native boolean i2cSetSlave(int i2cFD, int adress);
    private native boolean i2cWriteByte(int i2cFD,byte add, byte data);
    private native boolean i2cWriteBytes(int i2cFD,byte add, int length, byte data[]);
    private native int i2cReadByte(int i2cFD, byte add);
    private native boolean i2cReadBytes(int i2cFD, byte add, int length, int data[]);
    private native void i2cClose(int i2cFD);
    
    static int i2cFD;
    
    static {
        System.loadLibrary("BBBAndroidHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView tv = (TextView) findViewById(R.id.textView1);
		
        /* Opening Sensor */
        
		i2cFD = i2cOpenAdaptor(1); 
		
		if(i2cFD!=-1)
		{
			if(i2cSetSlave(i2cFD, 0x53))
			{
				int value = i2cReadByte(i2cFD, (byte) 0x00);
				
				if(value !=-1 )
				{
					tv.setText("Connected to i2c-1 with FD :" + i2cFD + ", Slave :" + 0x53 + 
							", ChipID Address :0x00, ChipID :" + value
							+ "\n\nReading Raw I2C values without calibration.\n");
				}
				
			}
		}	
		
		/* Configure Sensor */
		
		i2cWriteByte(i2cFD, (byte) 0x2d, (byte) 0x08); /* Set POWER_CTL register */
		
		byte value[] ={0x0b} ;						 /* Just to demostrate WriteBytes. WriteByte can also be used */
		i2cWriteBytes(i2cFD, (byte) 0x31, 1, value); /* Set DATA_FORMAT register */
		
		/* Configuration Complete */
		
		
		/* Reading Sensor Data */
		
		int x_data0 = i2cReadByte(i2cFD, (byte) 0x32);
		int x_data1 = i2cReadByte(i2cFD, (byte) 0x33);
		int y_data0 = i2cReadByte(i2cFD, (byte) 0x34);
		int y_data1 = i2cReadByte(i2cFD, (byte) 0x35);
		int z_data0 = i2cReadByte(i2cFD, (byte) 0x36);
		int z_data1 = i2cReadByte(i2cFD, (byte) 0x37);
		
		/************************/
		
		x_data0 = (int) (((x_data1 << 8) | x_data0));
		y_data0 = (int) (((y_data1 << 8) | y_data0));
		z_data0 = (int) (((z_data1 << 8) | z_data0)); ;
		
		TextView tv1 = (TextView) findViewById(R.id.button_state);
		tv1.setText("x_data :" + x_data0 + ", y_data :" + y_data0 + ", z_data :" + z_data0);
		
	}
	
	public void onClickButtonPollStatus(View view)
	{
        /* Reading Sensor Data */
		
        int[] value = new int[6];
        i2cReadBytes(i2cFD,(byte) 0x32, 6, value);
		
		/************************/
		
        int x_data = value[1]; 
		x_data = (int) (((x_data << 8) | value[0]));
		
		int y_data = value[3];
		y_data = (int) (((y_data << 8) | value[2]));
		
		int z_data = value[5];
		z_data = (int) (((z_data << 8) | value[4]));
		
		TextView tv2 = (TextView) findViewById(R.id.button_state);
		tv2.setText("x_data :" + x_data + ", y_data :" + y_data + ", z_data :" + z_data);
		
	}
}
