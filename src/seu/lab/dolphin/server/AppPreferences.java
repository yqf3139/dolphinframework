package seu.lab.dolphin.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
	
	SharedPreferences userPreferences = null;
	
	void init(Context context){
		userPreferences = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = userPreferences.edit();
		editor.putBoolean("init", true);
		editor.commit();
	}
	void clear(Context context){
		userPreferences = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = userPreferences.edit();
		editor.clear();
		editor.commit();
	}
}
