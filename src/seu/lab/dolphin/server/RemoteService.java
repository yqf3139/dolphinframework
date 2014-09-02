package seu.lab.dolphin.server;

import java.util.List;

import seu.lab.dolphin.client.ContinuousGestureEvent;
import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.IDataReceiver;
import seu.lab.dolphin.client.IDolphinStateCallback;
import seu.lab.dolphin.client.IGestureListener;
import seu.lab.dolphin.client.RealTimeData;
import seu.lab.dolphin.client.Dolphin.DataTypeMask;
import seu.lab.dolphin.client.Dolphin.GestureMask;
import seu.lab.dolphinframework.MainActivity;
import seu.lab.dolphinframework.R;
import android.R.bool;
import android.R.integer;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class RemoteService extends Service{

	static final String TAG = "RemoteService";
	public static final String REMOTE_SERVICE_NAME = "seu.lab.dolphin.server.REMOTE";
	static final int ONGOING_NOTIFICATION = 1120;
	Dolphin dolphin = null;
	Context mContext = this;

	IDataReceiver dataReceiver = new IDataReceiver() {
		
		@Override
		public void onData(RealTimeData arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public DataTypeMask getDataTypeMask() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	IGestureListener gestureListener = new IGestureListener() {
		
		@Override
		public void onGesture(GestureEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onContinuousGestureUpdate(ContinuousGestureEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onContinuousGestureStart(ContinuousGestureEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onContinuousGestureEnd() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public GestureMask gestureMask() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	IDolphinStateCallback stateCallback = new IDolphinStateCallback() {
		
		@Override
		public void onNoisy() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onCoreReady() {
			// TODO Auto-generated method stub
			
		}
	};
	private ActivityManager activityManager;
	private TaskShower taskShower;
	
	public String hello(String name){
		return TAG + ": hello " + name;
	}
	
	public void getForeground(){
        ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;  
        Log.e("Foreground", "pkg:"+cn.getPackageName());  
        Log.e("Foreground", "cls:"+cn.getClassName());
        final String clsString = cn.getClassName();
        Handler handler = new Handler(Looper.getMainLooper());  
        handler.post(new Runnable(){
            public void run(){  
                Toast.makeText(getApplicationContext(), 
                		clsString,
                		Toast.LENGTH_SHORT).show();  
            }  
        });
	}
	
	class TaskShower extends Thread{
		public boolean running = true;
		@Override
		public void run() {
			DolphinBroadcaster broadcaster = new DolphinBroadcaster();
			while(running){
				try {
					sleep(5*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getForeground();
				broadcaster.sendBroadcast(mContext);
			}
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");
		
		Notification notification = new Notification(R.drawable.dolphin_server, "ticket",System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(this, "title","message", pendingIntent);
		startForeground(ONGOING_NOTIFICATION, notification);
		
		activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		if(taskShower == null){
			taskShower = new TaskShower();
			taskShower.start();
		}else if(!taskShower.isAlive()){
			taskShower = new TaskShower();
			taskShower.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.e(TAG, "onBind");
		return new RemoteBinder();
	}
	
	@Override
	public void onRebind(Intent intent) {
		Log.e(TAG, "onRebind");
		super.onRebind(intent);
	}
	
	@Override
	public void onCreate() {
		Log.e(TAG, "onCreate");
		super.onCreate();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.e(TAG, "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy");
		taskShower.running = false;
		stopForeground(true);
		super.onDestroy();
	}
	
	public class RemoteBinder extends Binder{
		public RemoteService getRemoteService() {
			return RemoteService.this;
		}
	}
}
