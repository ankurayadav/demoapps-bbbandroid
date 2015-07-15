package com.bbbandroid.spi;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bbbandroid.spi.R;

public class MainActivity extends Activity {
	
    private native int spiOpen(int bus, int device, int speed, int mode, int bpw);
    private native int spiWriteByte(int spiFD, byte data);
    private native void spiClose(int spiFD);

    static {
        System.loadLibrary("BBBAndroidHAL");
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		int fd = spiOpen(1, 0, 10000, 3, 8);
		
		byte value = 0b01010101;
		
		spiWriteByte(fd, value);
		
		spiClose(fd);
	}
	
}
