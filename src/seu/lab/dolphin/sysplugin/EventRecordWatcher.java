package seu.lab.dolphin.sysplugin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import seu.lab.dolphin.utility.ShellUtils;

import android.util.Log;

public class EventRecordWatcher extends Thread{
	
	static String TAG = "EventRecordStopper";
	private EventRecorder recorder;
	static ShellUtils shell = new ShellUtils();
		
	public EventRecordWatcher(EventRecorder recorder){
		this.recorder = recorder;
	}
	
	@Override
	public void run() {
		if(recorder == null)return;
		if(recorder.isAlive())return;
		
		Log.i(TAG, "start");
		
		recorder.start();
		try {
			sleep((recorder.recordSeconds+1)*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "after sleep");

		recorder.stopNow();
		Log.i(TAG, "stop");
	}
}
