package com.bbbandroid.uart;

import com.bbbandroid.uart.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    
    private native int uartOpen(int device, int bdrate);
    private native boolean uartWrite(int uartFD, int length, byte data[]);
    private native boolean uartRead(int uartFD, int length, byte data[]);
    private native void uartClose(int uartFD);
    
    int uartFD;
  
    static {
        System.loadLibrary("BBBAndroidHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView tv = (TextView) findViewById(R.id.ConnectionString);
		
		uartFD = uartOpen(4, 9600);
		
		tv.setText("UART:4, Buadrate: 9600, uartFD:" + uartFD);    
        
	}
	
	public void onClickSend(View view)
	{
		EditText et = (EditText)findViewById(R.id.editText1);
		byte[] send =  et.getText().toString().getBytes();
		
		uartWrite(uartFD, send.length+1,send);
	}
	
	public void onClickReceive(View view)
	{
		TextView tv = (TextView) findViewById(R.id.textView1);
		
		byte[] receive = new byte[20];
		byte[] temp = new byte[2];
		int i;
		for(i=0; i<20; i++)
		{
			uartRead(uartFD, 1, temp);
			receive[i]=temp[0];
			if(temp[0]==(byte) '\0')
			{
				break;
			}
		}
		
		String s = new String(receive);
		
		tv.setText(s); 
	}
	
	@Override
	protected void onDestroy()
	{
		uartClose(uartFD);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}