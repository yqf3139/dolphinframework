package seu.lab.dolphin.server;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import seu.lab.dolphin.client.ContinuousGestureEvent;
import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.IDataReceiver;
import seu.lab.dolphin.client.IDolphinStateCallback;
import seu.lab.dolphin.client.IGestureListener;
import seu.lab.dolphin.client.RealTimeData;
import seu.lab.dolphin.core.LinearTest;
import seu.lab.dolphin.dao.DaoSession;
import seu.lab.dolphin.dao.DolphinContext;
import seu.lab.dolphin.dao.DolphinContextDao;
import seu.lab.dolphin.dao.DolphinContextDao.Properties;
import seu.lab.dolphin.dao.KeyEvent;
import seu.lab.dolphin.dao.KeyEventDao;
import seu.lab.dolphin.dao.Model;
import seu.lab.dolphin.dao.ModelConfig;
import seu.lab.dolphin.dao.ModelConfigDao;
import seu.lab.dolphin.dao.ModelDao;
import seu.lab.dolphin.dao.PlaybackEvent;
import seu.lab.dolphin.dao.PlaybackEventDao;
import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.dao.PluginDao;
import seu.lab.dolphin.dao.Rule;
import seu.lab.dolphin.dao.RuleDao;
import seu.lab.dolphin.dao.SwipeEvent;
import seu.lab.dolphin.dao.SwipeEventDao;
import seu.lab.dolphin.sysplugin.EventRecordWatcher;
import seu.lab.dolphin.sysplugin.EventRecorder;
import seu.lab.dolphin.sysplugin.EventSenderForKey;
import seu.lab.dolphin.sysplugin.EventSenderForPlayback;
import seu.lab.dolphin.sysplugin.EventSenderForSwipe;
import seu.lab.dolphinframework.MainActivity;
import seu.lab.dolphinframework.R;
import android.R.integer;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
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
	
	static final long DEFAULT_ID = 1l;
	
	Context mContext = this;

	DaoSession daoSession = null;
	ModelConfigDao modelConfigDao = null;
	PluginDao pluginDao = null;
	DolphinContextDao dolphinContextDao = null;
	ModelDao modelDao = null;
	KeyEventDao keyEventDao = null;
	SwipeEventDao swipeEventDao = null;
	PlaybackEventDao playbackEventDao = null;
	
	ModelConfig currentModelConfig = null;
	Plugin currentPlugin = null;
	DolphinContext currentDolphinContext = null;
	
	ModelConfig defaultModelConfig = null;
	Plugin defaultPlugin = null;
	DolphinContext defaultDolphinContext = null;
	
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
		public Bundle getDataTypeMask() {
			// TODO Auto-generated method stub
			return null;
		}

	};

	IGestureListener gestureListener = new IGestureListener() {

		@Override
		public void onGesture(GestureEvent event) {
			
			// TODO broadcaster
			// broadcaster.sendBroadcast(mContext);

			if (event.isConclusion) {
				Log.e(TAG, event.toString());
				
				// find rule by gesture id & plugin id
				List<Rule> rules = currentPlugin.getRules();
				Rule ruleToApply = null;
				for (Rule rule : rules) {
					if(rule.getApplied() && rule.getGesture_id() == event.type){
						ruleToApply = rule;
						break;
					}
				}
				applyRule(ruleToApply);
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
		public ContentValues gestureMask() {
			
			// TODO claim the gesture you need to be true
			
			return new ContentValues();
		}
	};

	IDolphinStateCallback stateCallback = new IDolphinStateCallback() {

		@Override
		public void onNoisy() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCoreReady() {
			dolphin.start();
		}
	};

	private ActivityManager activityManager;

	private QueryBuilder<DolphinContext> dolphinContextBuilder;
	private QueryBuilder<Model> modelBuilder;

	private Query<DolphinContext> findDolphinContextByActivityNameQuery;

	private DolphinBroadcaster broadcaster;

	private DolphinContextRefresher refresher;


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
				new EventSenderForPlayback("last_events").start();
			}
		});

	}

	public String getForegroundActivityName() {
		ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
		return cn.getClassName();
	}

	
	private void applyContext(DolphinContext dolphinContext) {
		if(dolphinContext == null){
			dolphinContext = defaultDolphinContext;
		}
		if(dolphinContext.getId() == currentDolphinContext.getId())
			return;
		
		ModelConfig modelConfig = dolphinContext.getModelConfig();
		Plugin plugin = dolphinContext.getPlugin();
		
		applyModels(modelConfig);
		applyPlugin(plugin);
	}
	
	private void applyModels(ModelConfig modelConfig) {
		if(modelConfig == null){
			modelConfig = defaultModelConfig;
		}
		
		if(modelConfig.getId() != currentModelConfig.getId()){
			String[] modelpaths = getModelPaths(modelConfig);
			
			try {
				LinearTest.setModel(modelpaths);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
				Log.e(TAG, "model err");
			}
		}
	}
	
	private void applyPlugin(Plugin plugin) {
		if(plugin == null){
			plugin = defaultPlugin;
		}
		if(plugin.getId() != currentPlugin.getId()){
			currentPlugin = plugin;
		}
	}
	
	private void applyRule(Rule rule) {
		switch (rule.getEvent_type()) {
		case 1:
			KeyEvent keyEvent = keyEventDao.load(rule.getEvent_id());
			if(keyEvent != null){
				new EventSenderForKey(keyEvent.getKeycode()).start();
			}
			break;
		case 2:
			SwipeEvent swipeEvent = swipeEventDao.load(rule.getEvent_id());
			if(swipeEvent != null){
				int[] coordinates = new int[]{
					swipeEvent.getX1(),
					swipeEvent.getY1(),
					swipeEvent.getX2(),
					swipeEvent.getY2()
				};
				new EventSenderForSwipe(coordinates).start();
			}
			break;
		case 3:
			PlaybackEvent playbackEvent = playbackEventDao.load(rule.getEvent_id());
			if(playbackEvent != null){
				new EventSenderForPlayback(playbackEvent.getScript_name()).start();
			}
			break;
		default:
			break;
		}
	}
	
	static private String[] getModelPaths(ModelConfig modelConfig){
		
		String[] modelpaths = modelConfig.getModel_ids().split(Pattern.quote("+"));
		
		if(modelpaths.length != 5)
			modelpaths = LinearTest.defaultModels;
		
		return modelpaths;
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
		activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		refresher = new DolphinContextRefresher();
		
		daoSession = DaoManager.getDaoSession(mContext);
		modelConfigDao = daoSession.getModelConfigDao();
		pluginDao = daoSession.getPluginDao();
		dolphinContextDao = daoSession.getDolphinContextDao();
		modelDao = daoSession.getModelDao();
		keyEventDao = daoSession.getKeyEventDao();
		swipeEventDao = daoSession.getSwipeEventDao();
		playbackEventDao = daoSession.getPlaybackEventDao();
		
		dolphin = Dolphin.getInstance(
				(AudioManager) getSystemService(Context.AUDIO_SERVICE), 
				getContentResolver(),
				stateCallback,
				null, 
				gestureListener);
		dolphin.switchToEarphoneSpeaker();
		dolphin.prepare();
		super.onCreate();
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            Log.e(TAG, "onStartCommand");
            
            currentDolphinContext = defaultDolphinContext = dolphinContextDao.load(DEFAULT_ID);
            currentModelConfig = defaultModelConfig = modelConfigDao.load(DEFAULT_ID);
            currentPlugin = defaultPlugin = pluginDao.load(DEFAULT_ID);

            dolphin.resume();

            Notification notification = new Notification(R.drawable.dolphin_server,
            		"ticket", System.currentTimeMillis());
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            notification.setLatestEventInfo(this, "title","message", pendingIntent);
            
            startForeground(ONGOING_NOTIFICATION, notification);
            
            createFloatView();

            refresher.start();
            
			return super.onStartCommand(intent, flags, startId);
    }

	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.e(TAG, "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy");
		
		refresher.stopGracefully();
		dolphin.stop();

		stopForeground(true);

		if (mFloatLayout != null) {
			mWindowManager.removeView(mFloatLayout);
		}
		
		super.onDestroy();
	}

	public class RemoteBinder extends Binder {
		public RemoteService getRemoteService() {
			return RemoteService.this;
		}
	}
	
	
	class DolphinContextRefresher extends Thread{
		static final String TAG = "DolphinContextRefresher";
		
		private boolean isRunning = true;
		
		public void stopGracefully(){
			Log.i(TAG,"stopGracefully");
			isRunning = false;
		}
		
		@Override
		public void run() {
			Log.i(TAG,"run");
			
			String activityName = null;
			while (isRunning) {
				try {
					sleep(DolphinServerVariables.DOLPHIN_CONTEXT_FRESH_INTERVAL*1000);
				} catch (InterruptedException e) {
					Log.e(TAG, e.toString());
				}
				
				activityName = getForegroundActivityName();
				if(activityName.equals(currentDolphinContext.getActivity_name()))
					continue;
				
				findDolphinContextByActivityNameQuery.setParameter(1, activityName);
				List<DolphinContext> res = findDolphinContextByActivityNameQuery.list();
				if(res.size() == 1){
					currentDolphinContext = res.get(0);
					applyContext(currentDolphinContext);
				}else {
					applyContext(null);
				}
			}
			Log.i(TAG,"end");
		}
	}

}
