package seu.lab.dolphin.sysplugin;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import seu.lab.dolphin.sysplugin.ShellUtils.CommandResult;

public abstract class EventSender extends Thread{
	
	static String TAG = "EventSender";
	static ShellUtils shell = new ShellUtils();
	
	@Override
	public void run() {
		Log.i(TAG, "start");
		
		CommandResult result = shell.execCommand(getCommandList(), getStart());
		for (int i = 0; i < result.successMsg.size(); i++) {
			Log.i(TAG, "successMsg:"+result.successMsg.get(i));
		}
		Log.i(TAG, "over");
	}
	
	abstract List<String> getCommandList();
	abstract String getStart();
}
