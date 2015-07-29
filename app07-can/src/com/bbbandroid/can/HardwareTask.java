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
	String s;
	Activity mCallerActivity = null;
	boolean isDone = false;
	TextView tv ;
	
	@Override
	protected void onPreExecute() {		
		isDone = false;
		
		tv = (TextView) mCallerActivity.findViewById(R.id.ConnectionString);
		
		String port = "vcan0";
		canFD = canOpenRaw(port);
		
		tv.setText("CAN :vcan0, canFD:" + canFD);
		
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		byte[] receive;
		
        while (!isDone && !isCancelled() && (canFD!=-1)) {
        	
        	receive = canReadBytes(canFD);
    		
    		if(receive!=null)
    		{
    			s = new String(receive);
    			
        		Log.i("HardwareTask", "Received Data : "+s);
        		publishProgress();
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
    protected void onCancelled() {
        Log.i("HardwareTask", "Cancelled.");
        isDone = true;
    }
	public void demo()
	{
		
	}
    @Override
    protected void onPostExecute(Boolean result){
    	if(canFD!=-1)
    	{
    		canClose(canFD);
    	}
    }
	public void SendBytes(int length, byte[] send) {
		// TODO Auto-generated method stub
		canSendBytes(canFD, length, send);
	}
	
    @Override
    protected void onProgressUpdate(Void... values) {
    	tv = (TextView) mCallerActivity.findViewById(R.id.textView1);
		tv.setText("Received Data : " + s); 
    }

}
