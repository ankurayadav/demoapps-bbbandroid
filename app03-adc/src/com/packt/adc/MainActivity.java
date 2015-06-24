package com.packt.adc;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.packt.adc.R;

public class MainActivity extends Activity {
	public final static String PACKT_TAG = "com.packt";
	
    private native int readADC(int channel);

    static {
        System.loadLibrary("packtHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView tv = (TextView) findViewById(R.id.button_state);
        tv.setText("Slider State: UNKNOWN");
        
    	int value = readADC(5);
    		
    	String status = Integer.toString(value);
    		
        tv.setText("Slider State: " + status);

	}
	
	public void onClickButtonPollStatus(View view)
	{
		TextView tv = (TextView) findViewById(R.id.button_state);
        tv.setText("Slider State: UNKNOWN");
        
    	int value = readADC(5);
    		
    	String status = Integer.toString(value);
    		
        tv.setText("Slider State: " + status);
	}
}
