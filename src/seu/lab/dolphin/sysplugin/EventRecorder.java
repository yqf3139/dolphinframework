package seu.lab.dolphin.sysplugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.R.integer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import seu.lab.dolphin.sysplugin.ShellUtils.CommandResult;
import seu.lab.dolphinframework.MainActivity;

public class EventRecorder extends Thread{

	public int recordSeconds = 5;
	static String TAG = "EventRecorder";
	ShellUtils shell = new ShellUtils();
	static List<String> recordCommandsList = new ArrayList<String>();
	public static List<String> lastList = new LinkedList<String>();
	static String eventName = "event1";

	static{
		recordCommandsList.add("getevent -q -t -c 10000");
	}
	
	public EventRecorder(int seconds) {
		recordSeconds = seconds;
	}
	
	@Override
	public void run() {
        Handler handler1 = new Handler(Looper.getMainLooper());  
        handler1.post(new Runnable(){
            public void run(){  
                Toast.makeText(
                		MainActivity.mContext, 
                		"record start",
                		Toast.LENGTH_SHORT).show();  
            }  
        });
		
		CommandResult result = shell.execCommand(recordCommandsList, ShellUtils.COMMAND_SU);
		int size = result.successMsg.size();
		Log.i(TAG, "result: "+result.result);
		Log.i(TAG, "size: "+size);
		lastList.clear();
		if(size < 1)return;
		
		String tmp,time,data; // ,prefix,mid,num
		Double lastTime,thisTime;
		int interval;

		tmp = result.successMsg.get(0);
		time = tmp.substring(1, 16);
		data = tmp.substring(37);
		thisTime = Double.parseDouble(time);
		lastTime = thisTime;
		Log.i(TAG, 0 + " " + data);
		lastList.add(0 + " " + data);
		for (int i = 1; i < result.successMsg.size(); i++) {
			tmp = result.successMsg.get(i);
//			prefix = tmp.substring(37,41);
//			mid = tmp.substring(42,46);
//			num = tmp.substring(47);
			time = tmp.substring(1, 16);
			data = tmp.substring(37);
			thisTime = Double.parseDouble(time);
			interval = (int)((thisTime-lastTime)*1000000);
			lastTime = thisTime;
			if(interval > 1000000)break;
			if(interval < 1000)interval = 0;
			else interval /= 4;
//			Integer integermid = Integer.parseInt(mid, 16);
//			Integer integernum = Integer.parseInt(num, 16);
			
//			Log.i(TAG, thisTime.doubleValue()+" "+prefix+" "+integermid.intValue()+" "+integernum.intValue());
//			lastList.add(prefix+" "+integermid.intValue()+" "+integernum.intValue());
			Log.i(TAG, interval + " " + data);
			lastList.add(interval + " " + data);
		}
		Log.i(TAG, "errorMsg: "+result.errorMsg);
		
        Handler handler2 = new Handler(Looper.getMainLooper());  
        handler2.post(new Runnable(){
            public void run(){  
                Toast.makeText(	
                		MainActivity.mContext, 
                		"record done",
                		Toast.LENGTH_SHORT).show();  
            }  
        });
		
		try {
			File sdcard = Environment.getExternalStorageDirectory();
			FileWriter fWriter = new FileWriter(new File(sdcard,"project_dolphin/last_events"));
			for (int i = 0; i < lastList.size(); i++) {
				fWriter.write(lastList.get(i)+"\n");
			}
			fWriter.close();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
        
		Log.i(TAG, "over");
	}
	
	void stopGracefully(){
		shell.loop = false;
	}
}
