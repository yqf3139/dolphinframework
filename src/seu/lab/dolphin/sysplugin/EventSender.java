package seu.lab.dolphin.sysplugin;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import seu.lab.dolphin.server.DolphinServerVariables;
import seu.lab.dolphin.utility.ShellUtils;
import seu.lab.dolphin.utility.ShellUtils.CommandResult;

public abstract class EventSender extends Thread {

	static String TAG = "EventSender";
	static ShellUtils shell = new ShellUtils();

	@Override
	public void run() {
		Log.i(TAG, "start");

		List<String> cList = getCommandList();
		CommandResult result = shell.execCommand(cList, getStart());
		Log.i(TAG, cList.get(0));
		
		for (int i = 0; i < result.successMsg.size(); i++) {
			Log.i(TAG, "successMsg:" + result.successMsg.get(i));
		}
		for (int i = 0; i < result.errorMsg.size(); i++) {
			Log.i(TAG, "errorMsg:" + result.errorMsg.get(i));
		}
		Log.i(TAG, "result: " + result.result);
		Log.i(TAG, "over");
	}

	abstract List<String> getCommandList();

	abstract String getStart();
}
