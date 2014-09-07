package seu.lab.dolphin.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
	
	private static SharedPreferences userPreferences = null;
	
	public static boolean isInitialized(Context context){
		userPreferences = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
		return userPreferences.getBoolean("init", false);
	}
	
	public static void init(Context context){
		if(userPreferences == null)
			userPreferences = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = userPreferences.edit();
		editor.putBoolean("init", true);
		editor.commit();
	}
	
	public static SharedPreferences getPreferences(){
		return userPreferences;
	}
	
	public static void clear(Context context){
		if(userPreferences == null)
			userPreferences = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = userPreferences.edit();
		editor.clear();
		editor.commit();
	}
}
