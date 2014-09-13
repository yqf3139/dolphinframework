package seu.lab.dolphin.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
	
	static SharedPreferences userPreferences = null;
	
	public static void init(Context context){
		if(userPreferences == null)
			userPreferences = context.getSharedPreferences("user", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = userPreferences.edit();
		editor.putBoolean("init	", true);
		editor.commit();
	}
	public static void clear(Context context){
		if(userPreferences == null)
			userPreferences = context.getSharedPreferences("user", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = userPreferences.edit();
		editor.clear();
		editor.commit();
	}
	public static boolean needUnlockScreen = true;
	
	public static void resume(Context context){
		if(userPreferences == null)
			userPreferences = context.getSharedPreferences("user", Activity.MODE_PRIVATE);
		needUnlockScreen = userPreferences.getBoolean("unlock_screen", true);
	}
	
}
