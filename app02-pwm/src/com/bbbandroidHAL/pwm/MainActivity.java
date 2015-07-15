package com.bbbandroidHAL.pwm;

import com.bbbandroidHAL.pwm.R;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private native boolean pwmSetPeriod(int channel, int period_ns);
	private native int pwmGetPeriod(int channel);
	private native boolean pwmSetDutyCycle(int channel, int duration_ns);
	private native int pwmGetDutyCycle(int channel);
	private native boolean pwmRun(int channel);
	private native boolean pwmStop(int channel);
	private native int pwmRunCheck(int channel);
	
	TextView tv;
	
    static {
        System.loadLibrary("BBBAndroidHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView) findViewById(R.id.button_state);
        tv.setText("PWM0 State: UNKNOWN");
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	public void onClickButtonPollStatus(View view)
	{
		int period = pwmGetPeriod(0);
		int dutycycle = pwmGetDutyCycle(0);
		int runcheck = pwmRunCheck(0);
		
		tv = (TextView) findViewById(R.id.button_state);
        tv.setText("PWM0 State: Period :" + period + ", Duty Cycle :" + dutycycle + ", Runcheck :" + runcheck);
	}
	
	public void onClickSetPeriodDutyCycle(View view)
	{
		EditText et = (EditText)findViewById(R.id.editText1);
		int period = Integer.parseInt(et.getText().toString());
		
		et = (EditText)findViewById(R.id.editText2);
		int dutycycle = Integer.parseInt(et.getText().toString());
		
		pwmSetPeriod(0, period);
		pwmSetDutyCycle(0, dutycycle);
		
		//tv = (TextView) findViewById(R.id.button_state);
		//tv.setText("PWM0 State: Period :" + period + ", Duty Cycle :" + dutycycle);
		
	}
	
	public void onClickButtonLightOn(View view)
	{
		pwmRun(0);
	}
	
	public void onClickButtonLightOff(View view)
	{
		pwmStop(0);
	}
	
	@Override
	protected void onDestroy()
	{

	}
	
}