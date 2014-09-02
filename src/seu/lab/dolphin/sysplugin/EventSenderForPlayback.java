package seu.lab.dolphin.sysplugin;

import java.util.ArrayList;
import java.util.List;

public class EventSenderForPlayback extends EventSender{

	static String command = "sendevent /dev/input/event1 ";
	
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
	
	public EventSenderForPlayback(List<String> commands){
		this.commands = commands;
	}
	
	@Override
	List<String> getCommandList() {
		List<String> list = new ArrayList<String>();
//		for (int i = 0; i < testData.length; i++) {
//			list.add(command+testData[i]);
//		}
//		for (int i = 0; i < commands.size(); i++) {
//			list.add(command+commands.get(i));
//		}
		list.add("/data/local/tmp/dolphincall /storage/sdcard0/project_dolphin/last_events /dev/input/event1");
		return list;
	}

	@Override
	String getStart() {
		// TODO Auto-generated method stub
		return ShellUtils.COMMAND_DOLPHIN;
	}
}
