package seu.lab.dolphin.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DolphinBroadcastReceiver extends BroadcastReceiver {

	static final String TAG = "DolphinBroadcastReceiver";
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		Log.i(TAG, "onReceive start");
		ctx.startService(new Intent(DolphinServerVariables.REMOTE_SERVICE_NAME));
		Log.i(TAG, "onReceive end");
	}

}
