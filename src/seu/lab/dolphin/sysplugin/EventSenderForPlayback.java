package seu.lab.dolphin.sysplugin;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.util.Log;

public class EventSenderForPlayback extends EventSender{
	
	public String command = "sendevent /dev/input/event"+EventSettings.EVENT_ID;
	
	static String[] testData = {
		"0003 0057 00000000",
		"0003 0053 00000259",
		"0003 0054 00000369",
		"0003 0058 00000069",
		"0003 0050 00000004",
		"0000 0002 00000000",
		"0000 0000 00000000",
		"0000 0002 00000000",
		"0000 0000 00000000",
	};

	private List<String> commands = null;
	
	public EventSenderForPlayback(){
		commands = null;
	}
	
	public EventSenderForPlayback(List<String> commands){
		this.commands = commands;
	}
	
	@Override
	List<String> getCommandList() {
		if(commands != null){
			return commands;
		}
		Log.i("getCommandList", "dolphincall /storage/sdcard0/project_dolphin/last_events /dev/input/event"+EventSettings.EVENT_ID);

		List<String> list = new ArrayList<String>();
		Log.i(TAG, "dolphincall /storage/sdcard0/project_dolphin/last_events /dev/input/event"+EventSettings.EVENT_ID);

		list.add("dolphincall /storage/sdcard0/project_dolphin/last_events /dev/input/event"+EventSettings.EVENT_ID);
		return list;
	}

	@Override
	String getStart() {
		// TODO Auto-generated method stub
		return ShellUtils.COMMAND_DOLPHIN;
	}
}
