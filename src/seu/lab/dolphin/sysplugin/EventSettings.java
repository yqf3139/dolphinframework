package seu.lab.dolphin.sysplugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import seu.lab.dolphin.sysplugin.ShellUtils.CommandResult;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class EventSettings {
	public static int EVENT_ID = 7;
	public static String[] endData = new String[2];

	static String TAG = "EventSettings";
	static ShellUtils shell = new ShellUtils();
	static List<String> recordCommandsList = new ArrayList<String>();

	static{
		recordCommandsList.add("getevent -q -t -c 50");
	}
	
	public class ScreenSetter extends Thread{
		@Override
		public void run() {
			Log.i(TAG, "ScreenSetter run");

			setupScreen();
			
//			List<String> stopCommandsList = new LinkedList<>();
//			String command = "sendevent /dev/input/event"+EventSettings.EVENT_ID;
//			String string1 = command+endData[0];
//			String string2 = command+endData[1];
//			for(int i=0;i<50;i++){
//				stopCommandsList.add(string1);
//				stopCommandsList.add(string2);
//			}
//			final ShellUtils catshell = new ShellUtils();
//			new Thread(){
//				public void run() {
//					Log.i(TAG, "catshell run");
//					catshell.execCommand("cat /dev/input/event"+EventSettings.EVENT_ID+" > /storage/sdcard0/project_dolphin/event_stop",
//							ShellUtils.COMMAND_DOLPHIN,false,true);
//					Log.i(TAG, "catshell stop");
//
//				}
//			}.start();
//			try {
//				sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Log.i(TAG, "putshell run");
//			ShellUtils putshell = new ShellUtils();
//			putshell.execCommand(stopCommandsList, ShellUtils.COMMAND_DOLPHIN);
//			catshell.loop = false;
//			synchronized (catshell.lockInteger) {
//				catshell.lockInteger.notifyAll();
//			}
//			Log.i(TAG, "putshell stop");

			Log.i(TAG, "ScreenSetter stop");
		}
	}
	
	public static void setupScreen(){
		CommandResult result = shell.execCommand(recordCommandsList, ShellUtils.COMMAND_DOLPHIN);
		int size = result.successMsg.size();
		for (int i = 0; i < result.successMsg.size(); i++) {
			Log.i(TAG, "successMsg:"+result.successMsg.get(i));
		}		
		for (int i = 0; i < result.errorMsg.size(); i++) {
			Log.i(TAG, "errorMsg:"+result.errorMsg.get(i));
		}
		if(size < 1)return;
		
		String tmp,time,data,event; // ,prefix,mid,num
		Double lastTime,thisTime;
		int interval;

		int[] event_count = new int[20];
		
		tmp = result.successMsg.get(0);
		if(tmp.length()<37){
			Log.e(TAG, "tmp.length(): "+tmp.length());
			return;
		}
		event = tmp.substring(tmp.indexOf(':')-1,tmp.indexOf(':'));
		event_count[Integer.parseInt(event)]++;
		time = tmp.substring(1, tmp.indexOf(']'));
		data = tmp.substring(tmp.indexOf(':')+1);
		thisTime = Double.parseDouble(time);
		lastTime = thisTime;

		for (int i = 1; i < result.successMsg.size(); i++) {
			tmp = result.successMsg.get(i);
			
			event_count[Integer.parseInt(event)]++;
			event = tmp.substring(tmp.indexOf(':')-1,tmp.indexOf(':'));
			time = tmp.substring(1, tmp.indexOf(']'));
			data = tmp.substring(tmp.indexOf(':')+1);
			thisTime = Double.parseDouble(time);
			interval = (int)((thisTime-lastTime)*1000000);
			lastTime = thisTime;
			if(interval > 10000 && i > 10){
				endData[0] = result.successMsg.get(i-2).substring(tmp.indexOf(':')+1);
				endData[1] = result.successMsg.get(i-1).substring(tmp.indexOf(':')+1);
				break;
			}
		}
		EVENT_ID = 0;
		for (int i = 1; i < event_count.length; i++) {
			if(event_count[EVENT_ID] < event_count[i])
				EVENT_ID = i;
		}
		Log.i(TAG, "event id: "+EVENT_ID);
		Log.i(TAG, "endData: "+endData[0]);
		Log.i(TAG, "endData: "+endData[1]);
		
//		File stop_event = new File("/storage/sdcard0/project_dolphin/stop_events");
//		try {
//			FileWriter fWriter = new FileWriter(stop_event);
//			for(int i=0;i<50;i++){
//				fWriter.write("0 "+endData[0]+"\n");
//				fWriter.write("0 "+endData[1]+"\n");
//			}
//			fWriter.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
