package seu.lab.dolphin.server;

import seu.lab.dolphin.sysplugin.EventSettings;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
	
	private static SharedPreferences appPreferences = null;
	
	public static boolean isInitialized(Context context){
		appPreferences = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
		return appPreferences.getBoolean("init", false);
	}
	
	public static void init(Context context){
		if(appPreferences == null)
			appPreferences = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = appPreferences.edit();
		editor.putBoolean("init", true);
		editor.commit();
	}
	
	public static SharedPreferences getPreferences(Context context){
		if(appPreferences == null)
			appPreferences = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
		return appPreferences;
	}
	
	public static void clear(Context context){
		if(appPreferences == null)
			appPreferences = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = appPreferences.edit();
		editor.clear();
		editor.commit();
	}

	public static void resume(Context mContext) {
		if(appPreferences == null)
			appPreferences = mContext.getSharedPreferences("user", Activity.MODE_PRIVATE);
		EventSettings.EVENT_ID = appPreferences.getInt("event_id", 1);		
	}
}
