package com.bbbandroid.spi;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bbbandroid.spi.R;

public class MainActivity extends Activity {
	
    private native int spiOpen(int bus, int device, int speed, int mode, int bpw);
    private native int spiWriteByte(int spiFD, byte data);
    private native void spiClose(int spiFD);
    
    int fd ;
    byte value;
    
    static {
        System.loadLibrary("BBBAndroidHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fd = spiOpen(1, 0, 10000, 3, 8);
		
		value = (byte) 0b00000000;
		spiWriteByte(fd, value);

	}
	
	public void onClickButtonPollStatus(View view)
	{
		EditText et = (EditText)findViewById(R.id.editText1);
		int send = Integer.parseInt(et.getText().toString());
		
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
		
		spiWriteByte(fd, value);
	}
	
	@Override
	protected void onDestroy()
	{
		spiClose(fd);
	}
	
}
