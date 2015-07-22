package com.bbbandroid.can;

import com.bbbandroid.can.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class HardwareTask extends AsyncTask<Void, Void, Boolean>{

	private native int canOpenRaw(String port);
	private native boolean canSendBytes(int canFD, int length, byte data[]);
	private native byte[] canReadBytes(int canFD);
	private native void canClose(int canFD);
	
	int canFD;
	Activity mCallerActivity = null;
	boolean isDone = false;
	TextView tv ;
	
	@Override
	protected void onPreExecute() {		
		Log.i("HardwareTask", "onPreExecute");
		isDone = false;
		
		tv = (TextView) mCallerActivity.findViewById(R.id.ConnectionString);
		
		String port = "vcan0";
		canFD = canOpenRaw(port);
		
		tv.setText("CAN :vcan0, canFD:" + canFD);
		
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		tv = (TextView) mCallerActivity.findViewById(R.id.textView1);
		
		byte[] receive;
		
        while (!isDone && !isCancelled()) {
        	
        	Log.i("HardwareTask", "inloop");
        	
        	receive = canReadBytes(canFD);
        	
    		Log.i("HardwareTask", "forendinloop");
    		
    		String s = new String(receive);
    		tv.setText(s); 
    		
        	Log.i("HardwareTask", "nextloop");
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
		Log.i("HardwareTask", "outside");
        
		return false;
	}
	
	public void pollHardware(Activity	act)	{
		mCallerActivity = act;
		execute();
	}
	
	
	
    @Override
    protected void onCancelled() {
        Log.i("HardwareTask", "Cancelled.");
        isDone = true;
    }
	public void demo()
	{
		
	}
    @Override
    protected void onPostExecute(Boolean result){
    	canClose(canFD);
    }
	public void SendBytes(int length, byte[] send) {
		// TODO Auto-generated method stub
		canSendBytes(canFD, length, send);
	}

}
