package seu.lab.dolphin.server;

import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import seu.lab.dolphin.client.ContinuousGestureEvent;
import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.DolphinCoreVariables;
import seu.lab.dolphin.client.DolphinException;
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
import seu.lab.dolphinframework.R;
import seu.lab.dolphinframework.fragment.FragmentMainActivity;
import seu.lab.dolphinframework.main.MainActivity;
import android.R.integer;
import android.R.string;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.MaskFilter;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
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
import android.widget.RemoteViews;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RemoteService extends Service {

	static final String TAG = "RemoteService";

	static final int ONGOING_NOTIFICATION = 1120;
	
	static final long DEFAULT_ID = 1l;
	
	Context mContext = this;

	private boolean screenlocked = false;
	private boolean screenOn = true;
	private boolean inMotion = false;


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
	
	long lastEventTime = 0l;

	FloatViewManager floatViewManager = null; 
	
	MotionSensor motionSensor = null;
	IMotionSensorCallback motionSensorCallback = new IMotionSensorCallback() {
		
		@Override
		public void onMotionStateChanged(boolean isInMotion) {
			inMotion = isInMotion;
			Log.i(TAG, "isInMotion: "+isInMotion);
		}
	};
	
	Dolphin dolphin = null;
	IDataReceiver dataReceiver = new IDataReceiver() {

		@Override
		public void onData(RealTimeData arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public JSONObject getDataTypeMask() {
			// TODO Auto-generated method stub
			return null;
		}

	};

	IGestureListener gestureListener = new IGestureListener() {
		
		@Override
		public void onGesture(GestureEvent event) {
			
			// TODO broadcaster
			// broadcaster.sendBroadcast(mContext);

			if(inMotion){
				Log.i(TAG, "inMotion mask enable, GestureEvent dropped");
				return;
			}
			
			if(!event.isConclusion){
				return;
			}
			Log.e(TAG, event.toString());
			
			final String rs = event.result;
			new Handler(getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(mContext, rs, Toast.LENGTH_SHORT).show();
				}
			});
			
			lastEventTime = System.currentTimeMillis();
			
			
			screenlocked = isScreenLocked();
			screenOn = isScreenOn();
			
			// screen off and locked, light it
			if(screenlocked && !screenOn){
				long duration = event.duration;
				if(duration > 1000 && event.type == GestureEvent.Gestures.PUSH.ordinal()){
					new EventSenderForKey(EventSenderForKey.KEYCODE_POWER).start();
					Log.e(TAG, "slow light");
					return;
				}
				if(event.isFast){
					new EventSenderForKey(EventSenderForKey.KEYCODE_POWER).start();
					Log.e(TAG, "fast light");
					
					new Thread(){
						public void run() {
							try {
								sleep(1500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							new EventSenderForPlayback("unlock_script").start();
							Log.e(TAG, "fast unlock");
						}
					}.start();

					return;
				}
			}
			
			// screen on and locked, unlock it
			if(screenlocked && screenOn){
				long duration = event.duration;
				if(event.isFast){
					new EventSenderForPlayback("unlock_script").start();
					Log.e(TAG, "unlock");

				}
				return;
			}
			
			if(currentPlugin.getPlugin_type() != 0){
				Log.i(TAG, "currentPlugin is disabled, drop event");
				return;
			}
			
			// find rule by gesture id & plugin id
			List<Rule> rules = currentPlugin.getRules();
			Rule ruleToApply = null;
			for (Rule rule : rules) {
				if(rule.getApplied() && rule.getGesture_id() == event.type){
					ruleToApply = rule;
					break;
				}
			}
			if(ruleToApply != null){
				Log.i(TAG, "apply rule: "+ruleToApply.getName());
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
		public JSONObject getGestureConfig() {
			// claim the gesture you need to be true
			
			JSONObject config = new JSONObject();
			JSONObject masks = new JSONObject();
			
			try {
				masks.put(""+GestureEvent.Gestures.SWIPE_LEFT_L.ordinal(),true);
				masks.put(""+GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal(),true);
				masks.put(""+GestureEvent.Gestures.SWIPE_LEFT_P.ordinal(),true);
				masks.put(""+GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal(),true);
				masks.put(""+GestureEvent.Gestures.PUSH_PULL_PUSH.ordinal(),true);
				masks.put(""+GestureEvent.Gestures.PULL.ordinal(),true);
				config.put("masks", masks);
			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			}
			
			return config;
		}
	};


	IDolphinStateCallback stateCallback = new IDolphinStateCallback() {


		@Override
		public void onNoisy() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCoreReady() {
			Log.e(TAG, "on core ready");
			inMotion = false;
    		refresher = new DolphinContextRefresher();
            refresher.start();
            if(UserPreferences.needMotionMask)
        		motionSensor.start();
			try {
				dolphin.start();
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	};

	private ActivityManager mActivityManager;

	private DolphinBroadcaster broadcaster;

	private DolphinContextRefresher refresher;

	private KeyguardManager mKeyguardManager;
	private PowerManager mPowerManager;


	public String hello(String name) {
		return TAG + ": hello " + name;
	}


	public String getForegroundActivityName() {
		ComponentName cn = mActivityManager.getRunningTasks(1).get(0).topActivity;
		return cn.getClassName();
	}
	
	private void applyContext(DolphinContext dolphinContext) {
		if(dolphinContext == null){
			Log.i(TAG,"dolphin context not matched, apply to default context");
			dolphinContext = defaultDolphinContext;
		}
		if(dolphinContext.getId() == currentDolphinContext.getId()){
			Log.i(TAG,"dolphin context not changed");
			return;
		}
		
		currentDolphinContext = dolphinContext;
		currentDolphinContext.refresh();
		
		Log.i(TAG,"applyContext to "+dolphinContext.getActivity_name());
		
		try {
			applyModelConfig(dolphinContext.getModelConfig());
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
		applyPlugin(dolphinContext.getPlugin());
	}
	
	private void applyModelConfig(ModelConfig modelConfig) throws JSONException {
		if(modelConfig == null){
			Log.i(TAG,"modelConfig not matched, apply to default modelConfig");
			modelConfig = defaultModelConfig;
		}
		
		if(modelConfig.getId() != currentModelConfig.getId()){
			applyModelConfigForce(modelConfig);
		}
	}
	
	private void applyPlugin(Plugin plugin) {
		if(plugin == null){
			Log.i(TAG,"plugin not matched, apply to default plugin");
			plugin = defaultPlugin;
		}
		if(plugin.getId() != currentPlugin.getId()){
			applyPluginForce(plugin);
		}
	}
	
	private void applyModelConfigForce(ModelConfig modelConfig) throws JSONException {
		Log.i(TAG,"applyModels to "+modelConfig.getModel_ids());

		currentModelConfig = modelConfig;

		JSONObject config = new JSONObject();
		JSONObject masks = new JSONObject(modelConfig.getMasks());
		JSONArray models = new JSONArray();
		JSONArray outputs = new JSONArray();
		
		JSONArray modelIDs = new JSONArray(modelConfig.getModel_ids());

		// get model paths by the splited ids
		boolean err = false;
		for (int i = 0; i < 4; i++) {
			if(modelIDs.getInt(i) == 0){
				models.put("");
				outputs.put(new JSONArray("[]"));
				continue;
			}
			Model model = modelDao.load((long)modelIDs.getInt(i));
			if(model == null){
				Log.e(TAG, "database err: model lost");
				err = true;
				break;
			}
			models.put(model.getModel_path());
			outputs.put(new JSONArray(model.getOutput()));
		}
		
		if(!err){
			config.put("models", models);
			config.put("masks", masks);
			config.put("outputs", outputs);
		}
		
		dolphin.setGestureConfig(null, config);
	}
	
	private void applyPluginForce(Plugin plugin) {
		currentPlugin = plugin;
		currentPlugin.refresh();
		Log.i(TAG,"applyPlugin to "+currentPlugin.getName());
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
	
	private String compressModelsToString(String[] modelpaths) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("models://");
		for (int i = 1; i < modelpaths.length; i++) {
			sBuilder.append('+');
			sBuilder.append(modelpaths[i]);
		}
		return sBuilder.toString();
	}
	
    public boolean isScreenLocked() {
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }
    
    public boolean isScreenOn() {
		return mPowerManager.isScreenOn();
	}

    public void startRecognition() {
    	Log.e(TAG, "starting dolphin");
		try {
			dolphin.prepare();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void stopRecognition() {
    	Log.e(TAG, "stoping dolphin");
    	motionSensor.stop();
    	
		if(refresher != null) {
			refresher.stopGracefully();
		}
		try {
			dolphin.stop();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
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
		mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        mKeyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        mPowerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        
		daoSession = DaoManager.getDaoManager(mContext).getDaoSession();
		modelConfigDao = daoSession.getModelConfigDao();
		pluginDao = daoSession.getPluginDao();
		dolphinContextDao = daoSession.getDolphinContextDao();
		modelDao = daoSession.getModelDao();
		keyEventDao = daoSession.getKeyEventDao();
		swipeEventDao = daoSession.getSwipeEventDao();
		playbackEventDao = daoSession.getPlaybackEventDao();
		
		showNotification();
        
        floatViewManager = FloatViewManager.getFlowViewManager(mContext);
        floatViewManager.createFloatView();
        
        motionSensor = new MotionSensor(mContext, motionSensorCallback);
        
		try {
			dolphin = Dolphin.getInstance(
					(AudioManager) getSystemService(Context.AUDIO_SERVICE), 
					getContentResolver(),
					stateCallback,
					null, 
					gestureListener);
		} catch (DolphinException e) {
			Log.e(TAG, e.toString());
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
		dolphin.switchToEarphoneSpeaker();
		
		super.onCreate();
	}
	
	void showNotification(){
        Notification notification = new Notification(R.drawable.ic_launcher,
        		"Dolphin 后台服务启动", System.currentTimeMillis());
        
        Intent notificationIntent = new Intent(mContext, seu.lab.dolphinframework.main.MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_dialog);
        
        contentView.setImageViewResource(R.id.noti_icon, android.R.drawable.ic_media_play);
        contentView.setImageViewResource(R.id.noti_close, android.R.drawable.ic_delete);
        contentView.setOnClickPendingIntent(R.id.noti_close, PendingIntent.getService(mContext, 1, new Intent(mContext, RemoteService.class).putExtra("close", true), 0));
        contentView.setOnClickPendingIntent(R.id.noti_main, PendingIntent.getActivity(mContext, 1, new Intent(mContext, MainActivity.class), 0));

        notification.contentView = contentView;
        
        startForeground(ONGOING_NOTIFICATION, notification);
	}
	
	public void borrowDataReceiver(IDataReceiver receiver) throws DolphinException {
		dolphin.setDataReceiver(receiver);

	}
	
	public void returnDataReceiver() throws DolphinException {
		dolphin.setDataReceiver(null);
	}
	
	public void borrowGestureListener(IGestureListener listener) throws DolphinException, JSONException {
		dolphin.setGestureListener(listener);
	}
	
	public void returnGestureListener() throws DolphinException, JSONException {
		dolphin.setGestureListener(gestureListener);
	}
	
	public int getDolphinState() {
		return dolphin.getCurrentState();
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if(intent == null)		
        	return super.onStartCommand(intent, flags, startId);

        Bundle bundle = intent.getExtras();
        if(bundle != null && bundle.containsKey("close")){
            Log.e(TAG, "close self");
            stopForeground(true);
        	stopSelf();
        }
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
		floatViewManager.hideFloatView();
		stopForeground(true);
		if(refresher != null) {
			refresher.stopGracefully();
		}
		stopRecognition();

		super.onDestroy();
		
		Log.e(TAG, "onDestroy end");
	}

	public class RemoteBinder extends Binder {
		public RemoteService getRemoteService() {
			return RemoteService.this;
		}
	}
	
	
	class DolphinContextRefresher extends Thread{
		static final String TAG = "DolphinContextRefresher";
		
		private boolean isRunning = true;
		private QueryBuilder<DolphinContext> dolphinContextBuilder;
		private Query<DolphinContext> findDolphinContextByActivityNameQuery;
		
		DolphinContextRefresher(){
			dolphinContextBuilder = dolphinContextDao.queryBuilder();
			findDolphinContextByActivityNameQuery = dolphinContextBuilder.where(Properties.Activity_name.eq("")).build();
	        
			if(currentDolphinContext == null){
				currentDolphinContext = defaultDolphinContext = dolphinContextDao.load(DEFAULT_ID);
		        currentModelConfig = defaultModelConfig = modelConfigDao.load(DEFAULT_ID);
		        currentPlugin = defaultPlugin = pluginDao.load(DEFAULT_ID);
			}
		}
		
		public void stopGracefully(){
			Log.i(TAG,"stopGracefully");
			isRunning = false;
		}
		
		@Override
		public void run() {
			Log.i(TAG,"run");
			
	        
	        try {
				applyModelConfigForce(currentDolphinContext.getModelConfig());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        applyPluginForce(currentDolphinContext.getPlugin());
			
			Query<DolphinContext> query = findDolphinContextByActivityNameQuery.forCurrentThread();
			String activityName = null;
			long eventDuration;
			lastEventTime = System.currentTimeMillis();
			while (isRunning) {
				try {
					sleep(DolphinServerVariables.DOLPHIN_CONTEXT_FRESH_INTERVAL*1000);
				} catch (InterruptedException e) {
					Log.e(TAG, e.toString());
				}
				
				eventDuration = System.currentTimeMillis() - lastEventTime;
				
				if(UserPreferences.needAutoSleep 
						&& eventDuration > DolphinServerVariables.DOLPHIN_SLEEP_TIME){
					stopRecognition();
					continue;
				}
				screenlocked = isScreenLocked();
				screenOn = isScreenOn();

				if(!UserPreferences.needEnableUnlock){
					screenlocked = false;
					screenOn = true;
				}
				
				if(screenlocked){
					Log.i(TAG, "screenlocked");
					applyContext(null);
					continue;
				}
				
				activityName = getForegroundActivityName();
				
				Log.i(TAG, "matching context for "+activityName);

				Log.i(TAG, currentDolphinContext.getModelConfig().getMasks());
				Log.i(TAG, currentDolphinContext.getModelConfig().getModel_ids());

				if(activityName.equals(currentDolphinContext.getActivity_name()))
					continue;
								
				query.setParameter(0, activityName);
				List<DolphinContext> res = query.list();
				if(res.size() == 1){
					applyContext(res.get(0));
				}else {
					applyContext(null);
				}
			}
			Log.i(TAG,"run end");
		}
	}

}
