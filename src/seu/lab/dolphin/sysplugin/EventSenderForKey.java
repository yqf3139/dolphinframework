package seu.lab.dolphin.sysplugin;

import java.util.ArrayList;
import java.util.List;

import seu.lab.dolphin.utility.ShellUtils;

public class EventSenderForKey extends EventSender{
	
	public static int KEYCODE_SOFT_RIGHT 	= 2;		
	public static int KEYCODE_HOME 			= 3;
	public static int KEYCODE_BACK 			= 4;
	public static int KEYCODE_CALL 			= 5;
	public static int KEYCODE_ENDCALL 		= 6;	
	public static int KEYCODE_STAR 			= 17;
	public static int KEYCODE_POUND 		= 18;	
	public static int KEYCODE_DPAD_UP 		= 19;	
	public static int KEYCODE_DPAD_DOWN 	= 20;		
	public static int KEYCODE_DPAD_LEFT 	= 21;		
	public static int KEYCODE_DPAD_RIGHT 	= 22;		
	public static int KEYCODE_DPAD_CENTER 	= 23;		
	public static int KEYCODE_VOLUME_UP 	= 24;		
	public static int KEYCODE_VOLUME_DOWN 	= 25;		
	public static int KEYCODE_POWER 		= 26;	
	public static int KEYCODE_CAMERA 		= 27;	
	public static int KEYCODE_CLEAR 		= 28;
	public static int KEYCODE_ENTER 		= 66;	
	public static int KEYCODE_MENU 			= 82;
	public static int KEYCODE_NOTIFICATION 	= 83;		
	public static int KEYCODE_SEARCH 		= 84;	
	
	static final String command = "input keyevent ";
	int keycode;
	
	public EventSenderForKey(int keycode){
		this.keycode = keycode;
	}

	@Override
	List<String> getCommandList() {
		List<String> list = new ArrayList<String>();
		list.add(command+keycode);
		//list.add("cat /storage/sdcard0/event_home  > /dev/input/event1");
		return list;
	}

	@Override
	String getStart() {
		// TODO Auto-generated method stub
		return ShellUtils.COMMAND_DOLPHIN;
	}
}
