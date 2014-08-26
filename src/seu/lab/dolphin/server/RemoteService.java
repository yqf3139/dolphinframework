package seu.lab.dolphin.server;

import seu.lab.dolphinframework.MainActivity;
import seu.lab.dolphinframework.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;

public class RemoteService extends Service {

	private static final String TAG = "RemoteService";
	private static final int NOTIFY_ID = 0;
	private IRemoteService.Stub mBinderStub = new IRemoteService.Stub() {
		
		@Override
		public String hello(String name) throws RemoteException {
			return "from server: hello "+name;
		}
	};
	private Context mContext = this;
	private Notification mNotification;

	private NotificationManager mNotificationManager;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinderStub;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, TAG + " onCreate()");
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		super.onCreate();
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		setUpNotification();
		Log.d(TAG, TAG + " onStart()");
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy() {
		mNotificationManager.cancel(NOTIFY_ID);
		Log.d(TAG, TAG + " onDestroy()");
		super.onDestroy();
	}
    private void setUpNotification() {  
        int icon = R.drawable.dolphin_server;
        CharSequence tickerText = "开始下载";  
        long when = System.currentTimeMillis();  
        mNotification = new Notification(icon, tickerText, when);  
        // 放置在"正在运行"栏目中  
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;  
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);  
        // 指定内容意图  
        mNotification.setLatestEventInfo(this, "dolphin", "dolphin", contentIntent);
        mNotificationManager.notify(NOTIFY_ID, mNotification);  
    }  

}
