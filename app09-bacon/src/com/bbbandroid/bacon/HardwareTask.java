package com.bbbandroid.bacon;

import com.bbbandroid.bacon.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class HardwareTask extends AsyncTask<Void, Void, Boolean>{
	
	private native boolean openGPIO();
	private native void closeGPIO();
    private native boolean readGPIO(int header, int pin);
    
	private native boolean pwmSetPeriod(int channel, int period_ns);
	private native boolean pwmSetDutyCycle(int channel, int duration_ns);
	private native boolean pwmRun(int channel);
	private native boolean pwmStop(int channel);
	
    private native int readADC(int channel);
    
    private native int spiOpen(int bus, int device, int speed, int mode, int bpw);
    private native int spiWriteByte(int spiFD, byte data);
    private native void spiClose(int spiFD);
    
	private native int usbInit();
	private native boolean usbGetDevices(int ids[][], byte strings[][][]);
	private native void usbClose();
    
	Activity mCallerActivity = null;
    boolean isDone = false;
    boolean button = false;
    int dutycycle;
    int spiFD ;
    byte spiValue ;
    TextView tv;
    int count;
    String usbDevices;
    
	@Override
	protected void onPreExecute() {		
		isDone = false;
		
        if(openGPIO() == false) {
        	Log.e("HardwareTask", "Unable to open GPIO.");
        }
        
		spiFD = spiOpen(1, 0, 10000, 3, 8);
		
		tv = (TextView) mCallerActivity.findViewById(R.id.ConnectionString);
		
		count = usbInit();
		tv.setText("Number of Usb Devices Found : " + count);
		
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		pwmSetPeriod(0, 1000000);
		pwmSetPeriod(1, 1000000);
		pwmSetPeriod(2, 1000000);
		
        while (!isDone && !isCancelled())
        {
        	dutycycle = 200*readADC(5);
    		pwmSetDutyCycle(0, dutycycle);
    		pwmSetDutyCycle(1, dutycycle);
    		pwmSetDutyCycle(2, dutycycle);
    		
    		spiValue = sendvalue(dutycycle/100000);
    		spiWriteByte(spiFD, spiValue);
        	
        	if(readGPIO(8, 19) == false)
        	{
        		if(button==false)
        		{
            		pwmRun(0);
            		pwmRun(1);
            		pwmRun(2);
            		
        			button = true;
        		}
        		else
        		{
            		pwmStop(0);
            		pwmStop(1);
            		pwmStop(2);
            		
        			button = false;
        		}
            	Log.e("HardwareTask", "BUTTON pressed");
        	}
        	
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }

		return false;
	}
	
	
	public void pollHardware(Activity act)	{
		mCallerActivity = act;
		execute();
	}
	
    @Override
    protected void onPostExecute(Boolean result){
    	closeGPIO();
    	spiClose(spiFD);
    	usbClose();
    }
	
    @Override
    protected void onCancelled() {
        Log.i("HardwareTask", "Cancelled.");
        isDone = true;
    }
	
    static byte sendvalue(int send)
    {
    	byte value;
    	
    	switch(send)
		{
			case 0: value = (byte) 0b11000000; break;
			case 1: value = (byte) 0b11001111; break;
			case 2: value = (byte) 0b10100100; break;
			case 3: value = (byte) 0b10110000; break;
			case 4: value = (byte) 0b10011001; break;
			case 5: value = (byte) 0b10010010; break;
			case 6: value = (byte) 0b10000010; break;
			case 7: value = (byte) 0b11111000; break;
			case 8: value = (byte) 0b10000000; break;
			case 9: value = (byte) 0b10010000; break;
			default: value = (byte) 0b10000110;
		}
    	return value;
    }
    
	public void PollDevices() {
		
		int ids[][] = new int[count][8];
		byte strings[][][] = new byte[count][3][256];
		String temp;
		
		usbGetDevices(ids, strings);	
		
		usbDevices = new String();
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
		
		publishProgress();
	}
	
    @Override
    protected void onProgressUpdate(Void... values) {
		tv = (TextView) mCallerActivity.findViewById(R.id.textView1);
		tv.setText("Usb devices found are :\n" + usbDevices);
    }
    
}
