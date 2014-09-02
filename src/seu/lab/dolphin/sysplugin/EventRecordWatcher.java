package seu.lab.dolphin.sysplugin;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class EventRecordWatcher extends Thread{
	
	static String TAG = "EventRecordStopper";
	private EventRecorder recorder;
	static ShellUtils shell = new ShellUtils();
	static List<String> stopCommandsList = new ArrayList<String>();
	static List<String> confirmCommandList = new ArrayList<String>();
	
	static String command = "sendevent /dev/input/event1 ";
	
	static{
		stopCommandsList.add("cat /storage/sdcard0/event_stop > /dev/input/event1");
//		String string1 = command+" 0000 0002 00000000";
//		String string2 = command+" 0000 0000 00000000";
//		for(int i=0;i<50;i++){
//			stopCommandsList.add(string1);
//			stopCommandsList.add(string2);
//		}
//		confirmCommandList.add(string1);
//		confirmCommandList.add(string2);
//		confirmCommandList.add(string1);
//		confirmCommandList.add(string2);
	}
	
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
			sleep(recorder.recordSeconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		shell.execCommand(stopCommandsList, ShellUtils.COMMAND_DOLPHIN);
		if(recorder.isAlive()){
			recorder.stopGracefully();
			shell.execCommand(stopCommandsList, ShellUtils.COMMAND_DOLPHIN);
		}
		Log.i(TAG, "stop");
	}
}
