package seu.lab.dolphin.sysplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import seu.lab.dolphin.utility.ShellUtils;

import android.R.integer;

public class EventSenderForSwipe extends EventSender{

	static final String command = "input swipe ";
	
	int[] coordinates;
	static final int[][] defaultSwipe = {
		{100,800,100,0},
		{100,0,100,800},
		{400,400,100,400},
		{100,400,400,400}
	};
	
	public static enum SwipeChoice{
		to_up,to_down,to_left,to_right
	}
	
	public EventSenderForSwipe(SwipeChoice choice){
		this.coordinates = defaultSwipe[choice.ordinal()];
	}
	public EventSenderForSwipe(int[] coordinates){
		this.coordinates = coordinates;
	}
	
	@Override
	List<String> getCommandList() {
		List<String> list = new ArrayList<String>();
		list.add(command+coordinates[0]+" "+coordinates[1]
				+" "+coordinates[2]+" "+coordinates[3]);
		return list;
	}
	@Override
	String getStart() {
		// TODO Auto-generated method stub
		return ShellUtils.COMMAND_DOLPHIN;
	}
	
}
