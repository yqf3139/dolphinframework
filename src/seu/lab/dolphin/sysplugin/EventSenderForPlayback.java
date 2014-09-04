package seu.lab.dolphin.sysplugin;

import java.util.ArrayList;
import java.util.List;

import seu.lab.dolphin.server.DolphinServerVariables;
import seu.lab.dolphin.utility.ShellUtils;

import android.R.integer;
import android.util.Log;

public class EventSenderForPlayback extends EventSender{
		
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
	private String script = "";
	
	public EventSenderForPlayback(String script){
		commands = null;
		this.script = script;
	}
	
	public EventSenderForPlayback(List<String> commands){
		this.commands = commands;
	}
	
	@Override
	List<String> getCommandList() {
		if(commands != null){
			return commands;
		}
		List<String> list = new ArrayList<String>();

		list.add("dolphincall "+DolphinServerVariables.DOLPHIN_HOME+"/scripts/"+script+" /dev/input/event"+EventSettings.EVENT_ID);
		return list;
	}

	@Override
	String getStart() {
		// TODO Auto-generated method stub
		return ShellUtils.COMMAND_DOLPHIN;
	}
}
