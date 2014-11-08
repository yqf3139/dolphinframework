package seu.lab.dolphin.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserPreferences {
	
	static SharedPreferences userPreferences = null;
	
	public static void init(Context context){
		if(userPreferences == null)
			userPreferences = context.getSharedPreferences("user", Activity.MODE_PRIVATE);
//		SharedPreferences.Editor editor = userPreferences.edit();
//		editor.putBoolean("init	", true);
//		editor.commit();
	}
	public static void clear(Context context){
		if(userPreferences == null)
			userPreferences = context.getSharedPreferences("user", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = userPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
	public static boolean needEnableUnlock = true;
	public static boolean needMotionMask = false;
	public static boolean needAutoStart = false;
	public static boolean needAutoSleep = false;
	public static boolean needLight = false;
	public static String lightServer = "";

	
	public static void refresh(Context context){
		if(userPreferences == null)
			userPreferences = context.getSharedPreferences("user", Activity.MODE_PRIVATE);
		needEnableUnlock = userPreferences.getBoolean("enable_unlock", true);
		needMotionMask = userPreferences.getBoolean("motion_mask", false);
		needAutoStart = userPreferences.getBoolean("auto_start", false);
		needAutoSleep = userPreferences.getBoolean("auto_sleep", false);
		needLight = userPreferences.getBoolean("light", false);
		lightServer = userPreferences.getString("light_server", "");

	}
	
	public static void set(String key, boolean value) {
		Editor editor = userPreferences.edit();
		editor.putBoolean(key, value);
    	editor.commit();
	}
	
	public static void set(String key, String value) {
		Editor editor = userPreferences.edit();
		editor.putString(key, value);
    	editor.commit();
	}
	
}
