package seu.lab.dolphin.server;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DolphinBroadcaster {
	
	static final String TAG = "DolphinBroadcaster";
	static final String BROADCAST_CLIENT_NAME = "seu.lab.dolphin.server.BROADCAST_CLIENT";

	void sendBroadcast(Context ctx){
		Log.i(TAG,"send start");
		Intent intent = new Intent(BROADCAST_CLIENT_NAME);
		ctx.sendBroadcast(intent);//发送广播事件  
		Log.i(TAG,"send end");
	}
	
}
