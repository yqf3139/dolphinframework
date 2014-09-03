package seu.lab.dolphin.server;

import seu.lab.dolphin.client.ContinuousGestureEvent;
import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.Dolphin.DataTypeMask;
import seu.lab.dolphin.client.Dolphin.GestureMask;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.IDataReceiver;
import seu.lab.dolphin.client.IDolphinStateCallback;
import seu.lab.dolphin.client.IGestureListener;
import seu.lab.dolphin.client.RealTimeData;
import seu.lab.dolphin.sysplugin.EventRecordWatcher;
import seu.lab.dolphin.sysplugin.EventRecorder;
import seu.lab.dolphin.sysplugin.EventSenderForPlayback;
import seu.lab.dolphinframework.MainActivity;
import seu.lab.dolphinframework.R;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RemoteService extends Service {

	static final String TAG = "RemoteService";

	static final int ONGOING_NOTIFICATION = 1120;
	Context mContext = this;

	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	WindowManager mWindowManager;
	Button mFloatRecordButton;
	Button mFloatPlaybackButton;
	ImageView mFloatbarImage;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String Text = bundle.getString("text");
			mFloatRecordButton.setText(Text);
		}
	};

	Dolphin dolphin = null;
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
		public void onGesture(GestureEvent event) {
			// TODO Auto-generated method stub
			if (event.isConclusion) {
				Log.e(TAG, event.toString());
			}
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
			dolphin.start();
		}
	};

	private ActivityManager activityManager;
	private TaskShower taskShower;

	public String hello(String name) {
		return TAG + ": hello " + name;
	}

	private void createFloatView() {
		wmParams = new WindowManager.LayoutParams();
		mWindowManager = (WindowManager) getApplication().getSystemService(
				getApplication().WINDOW_SERVICE);
		wmParams.type = LayoutParams.TYPE_PHONE;

		wmParams.format = PixelFormat.RGBA_8888;

		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;

		wmParams.gravity = Gravity.LEFT | Gravity.TOP;

		wmParams.x = 0;
		wmParams.y = 0;

		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout,null);
		mWindowManager.addView(mFloatLayout, wmParams);
		mFloatbarImage = (ImageView) mFloatLayout.findViewById(R.id.float_bar);
		mFloatRecordButton = (Button) mFloatLayout
				.findViewById(R.id.float_record_button);
		mFloatPlaybackButton = (Button) mFloatLayout
				.findViewById(R.id.float_playback_button);
		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		mFloatLayout.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY;
			int paramX, paramY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.i(TAG, "ACTION_DOWN");
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = wmParams.x;
					paramY = wmParams.y;
					break;
				case MotionEvent.ACTION_MOVE:
					Log.i(TAG, "ACTION_MOVE");
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					wmParams.x = paramX + dx;
					wmParams.y = paramY + dy;
					mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					break;
				}
				return false;
			}
		});
		mFloatRecordButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mFloatRecordButton.getText().equals("record")) {
					new EventRecordWatcher(new EventRecorder(5)).start();
					new Thread() {
						public void run() {
							int num = 5;
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("text", Integer.toString(num));
							msg.setData(bundle);
							mHandler.sendMessage(msg);
							while (num > 0) {
								try {
									sleep(1000);
									msg = new Message();
									bundle = new Bundle();
									bundle.putString("text",
											Integer.toString(--num));
									msg.setData(bundle);
									mHandler.sendMessage(msg);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							try {
								sleep(1000);
								msg = new Message();
								bundle = new Bundle();
								bundle.putString("text", "confirm");
								msg.setData(bundle);
								mHandler.sendMessage(msg);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}.start();
				} else if (mFloatRecordButton.getText().equals("confirm")) {
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("text", "record");
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				}
			}
		});
		mFloatPlaybackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new EventSenderForPlayback().start();
			}
		});

	}

	public void getForeground() {
		ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
		Log.e("Foreground", "pkg:" + cn.getPackageName());
		Log.e("Foreground", "cls:" + cn.getClassName());
		final String clsString = cn.getClassName();
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), clsString,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	class TaskShower extends Thread {
		public boolean running = true;

		@Override
		public void run() {
			DolphinBroadcaster broadcaster = new DolphinBroadcaster();
			while (running) {
				try {
					sleep(5 * 1000);
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

		Notification notification = new Notification(R.drawable.dolphin_server,
				"ticket", System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification
				.setLatestEventInfo(this, "title", "message", pendingIntent);
		startForeground(ONGOING_NOTIFICATION, notification);

		createFloatView();

		activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
//		if (taskShower == null) {
//			taskShower = new TaskShower();
//			taskShower.start();
//		} else if (!taskShower.isAlive()) {
//			taskShower = new TaskShower();
//			taskShower.start();
//		}
		
		dolphin.resume();
		
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
		dolphin = Dolphin.getInstance(
				(AudioManager) getSystemService(Context.AUDIO_SERVICE), 
				stateCallback,
				null, 
				gestureListener);
		dolphin.switchToEarphoneSpeaker();
		dolphin.prepare();
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
		if (mFloatLayout != null) {
			mWindowManager.removeView(mFloatLayout);
		}
		
		dolphin.stop();
		
		super.onDestroy();
	}

	public class RemoteBinder extends Binder {
		public RemoteService getRemoteService() {
			return RemoteService.this;
		}
	}
}
