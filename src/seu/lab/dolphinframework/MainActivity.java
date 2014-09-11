package seu.lab.dolphinframework;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.DolphinException;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.IDataReceiver;
import seu.lab.dolphin.client.RealTimeData;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.GestureDao;
import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.learn.DolphinTrainner;
import seu.lab.dolphin.server.AppPreferences;
import seu.lab.dolphin.server.DaoManager;
import seu.lab.dolphin.server.DolphinServerVariables;
import seu.lab.dolphin.server.RemoteService;
import seu.lab.dolphin.server.RemoteService.RemoteBinder;
import seu.lab.dolphin.sysplugin.EventRecordWatcher;
import seu.lab.dolphin.sysplugin.EventRecorder;
import seu.lab.dolphin.sysplugin.EventSenderForKey;
import seu.lab.dolphin.sysplugin.EventSenderForPlayback;
import seu.lab.dolphin.sysplugin.EventSenderForSwipe;
import seu.lab.dolphin.sysplugin.EventSettings;
import seu.lab.dolphin.sysplugin.Installer;
import seu.lab.dolphin.sysplugin.EventSettings.ScreenSetter;
import seu.lab.dolphinframework.R;
import android.R.integer;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	static final String TAG = "DFMainActivity";
	ToggleButton toggle_service;
	public static Context mContext;
	private RemoteService mService = null;
	
	private SurfaceView sfv;

	Drawer drawer;
	
	public MainActivity() {

	}
	
	IDataReceiver receiver = new IDataReceiver() {
		
		@Override
		public void onData(RealTimeData arg0) {
			drawer.simpleDraw(arg0.radius, arg0.feature_info, arg0.normal_info);
		}
		
		@Override
		public JSONObject getDataTypeMask() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	class Drawer{
		int w_screen;
        int h_screen;
        int density;
    	private Paint mainPaint;
    	private Paint extraPaint;

    	
    	Drawer(){
    		DisplayMetrics dm = getResources().getDisplayMetrics();
    		w_screen = dm.widthPixels;
            h_screen = dm.heightPixels;
            density = dm.densityDpi;
    		mainPaint = new Paint();
    		mainPaint.setColor(Color.CYAN);
    		mainPaint.setStrokeWidth(1);
    		mainPaint.setAntiAlias(true);
    		
    		extraPaint = new Paint();
    		extraPaint.setColor(Color.BLUE);
    		extraPaint.setStrokeWidth(20);
    		extraPaint.setAntiAlias(true);
    	}
        
    	private void simpleDraw(double radius, double[] feature, double[] info ) {
    		Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0, 0, w_screen, sfv.getHeight()));
    		if(canvas == null)return;
    		canvas.drawColor(Color.WHITE);
    		//canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.single_dolphin), 100, 100, mainPaint);
    		mainPaint.setAlpha((int) (100*(radius + 1)/4)+50);
    		canvas.drawCircle(w_screen/2, sfv.getHeight()/2, (float) (((radius + 1) * w_screen/6) + w_screen/8), mainPaint);
    		
    		int middle = 100;
    		if (null != feature) {
    			for (int i = 0; i < feature.length; i++) {
    				canvas.drawLine(20 * i + 50, middle, 20 * i + 50, middle
    						- (float) feature[i] * 1000, extraPaint);
    			}
    		}
    		middle = sfv.getHeight();
    		if (null != info) {
    			for (int i = 0; i < info.length; i++) {
    				canvas.drawLine(10 * i - 800, middle, 10 * i - 800, middle
    						- (float) info[i] * 600, extraPaint);
    			}
    		}
    		canvas.save();
    		sfv.getHolder().unlockCanvasAndPost(canvas);
    	}
    	
	}
	
	private ServiceConnection mConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.d(TAG, "Service Disconnected.");
						
            Toast.makeText(mContext, TAG + " Service Disconnected.", Toast.LENGTH_SHORT).show();
            mService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			mService = ((RemoteBinder)binder).getRemoteService();
			Log.d(TAG, " Service Connected.");

			if(mService == null){
				
			}else if(mService.getDolphinState() == Dolphin.States.WORKING.ordinal()){
				toggle_service.setChecked(true);
			}else {
				toggle_service.setChecked(false);
			}
			
            Toast.makeText(mContext, mService.hello("yqf"), Toast.LENGTH_SHORT).show();  
			
		}
	};
	private ToggleButton toogle_sfv;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		drawer = new Drawer();
		if(!AppPreferences.isInitialized(mContext)){
			Log.i(TAG, "first run: initing Plugin & DB");
			new Thread(){
				@Override
				public void run() {	
					EventSettings settings = new EventSettings();
					ScreenSetter setter = settings.new ScreenSetter(mContext); 
					setter.start();
					Installer.installAll(mContext);
					DaoManager daoManager = DaoManager.getDaoManager(mContext);
					daoManager.createDB();
					AppPreferences.init(mContext);

					List<Plugin> plugins = daoManager.listAllPlugins();
					for (int i = 0; i < plugins.size(); i++) {
						daoManager.updatePluginWithRuleChanged(plugins.get(i));
					}
				}
			}.start();
		}else {
			// TODO restore prefs
			EventSettings.EVENT_ID = AppPreferences.getPreferences().getInt("event_id", 1);
			
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01);

		toggle_service = (ToggleButton) findViewById(R.id.toggle_service);
		toggle_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(toggle_service.isChecked()){
					Log.e(TAG, "starting recognize");
					mService.startRecognition();
				}else {
					Log.e(TAG, "stop recognize");
					mService.stopRecognition();
				}
			}
		});
		
		toogle_sfv = (ToggleButton) findViewById(R.id.toggle_sfv);
		toogle_sfv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(toogle_sfv.isChecked()){
					try {
						mService.borrowDataReceiver(receiver);
					} catch (DolphinException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					try {
						mService.returnDataReceiver();
					} catch (DolphinException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		Button test = (Button) findViewById(R.id.test_btn);
		test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
	            if(mService == null)
	            	Toast.makeText(mContext, "null", Toast.LENGTH_SHORT).show(); 
	            else{
	            	Toast.makeText(mContext, mService.hello("yqf"), Toast.LENGTH_SHORT).show(); 
	            	mService.getForegroundActivityName();
	            }
			}
		});
		
//		Button keycodeButton = (Button) findViewById(R.id.keycode_btn);
//		Button swipeButton = (Button) findViewById(R.id.swipe_btn);
//		Button playbackButton = (Button) findViewById(R.id.playback_btn);
//		Button recordButton = (Button) findViewById(R.id.record_btn);
//		Button installButton = (Button) findViewById(R.id.install_btn);
//		Button settingButton = (Button) findViewById(R.id.screen_set_btn);
//		Button createButton = (Button) findViewById(R.id.create_db_btn);
		Button trainButton = (Button) findViewById(R.id.train);


//		keycodeButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				new EventSenderForKey(EventSenderForKey.KEYCODE_HOME).start();
//			}
//		});
//		swipeButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				new EventSenderForSwipe(EventSenderForSwipe.SwipeChoice.to_down).start();
//			}
//		});
//		playbackButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				new EventSenderForPlayback("last_events").start();
//			}
//		});
//		recordButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				new EventRecordWatcher(new EventRecorder(5)).start();
//			}
//		});
		trainButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new Thread(){
					@Override
					public void run() {
						GestureDao dao = DaoManager.getDaoManager(mContext).getDaoSession().getGestureDao();

						DaoManager.getDaoManager(mContext).getSingleModel_id(
								DolphinServerVariables.MODEL_PREFIX[3],
								new Gesture[]{
								dao.load((long) GestureEvent.Gestures.CROSSOVER_CLOCKWISE.ordinal()),
								dao.load((long) GestureEvent.Gestures.CROSSOVER_ANTICLOCK.ordinal()),
						});
						
//						DolphinTrainner trainner = new DolphinTrainner();
//						try {
//							JSONArray output;
//							output = DolphinTrainner.createModel("nf_default.dolphin", new Gesture[]{
//								dao.load((long) GestureEvent.Gestures.PUSH_PULL.ordinal()),
//								dao.load((long) GestureEvent.Gestures.SWIPE_LEFT_L.ordinal()),
//								dao.load((long) GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal()),
//								dao.load((long) GestureEvent.Gestures.SWIPE_LEFT_P.ordinal()),
//								dao.load((long) GestureEvent.Gestures.SWIPE_RIGHT_P.ordinal()),
//							});
//							System.err.println(output.toString());
//							output = DolphinTrainner.createModel("fn_default.dolphin", new Gesture[]{
//									dao.load((long) GestureEvent.Gestures.PULL_PUSH.ordinal()),
//									dao.load((long) GestureEvent.Gestures.SWING_LEFT_L.ordinal()),
//									dao.load((long) GestureEvent.Gestures.SWING_RIGHT_L.ordinal()),
//									dao.load((long) GestureEvent.Gestures.SWING_LEFT_P.ordinal()),
//									dao.load((long) GestureEvent.Gestures.SWING_RIGHT_P.ordinal()),
//								});
//							System.err.println(output.toString());
//							output = DolphinTrainner.createModel("nfnf_default.dolphin", new Gesture[]{
//									dao.load((long) GestureEvent.Gestures.PUSH_PULL_PUSH_PULL.ordinal()),
//									dao.load((long) GestureEvent.Gestures.SWIPE_BACK_LEFT_L.ordinal()),
//									dao.load((long) GestureEvent.Gestures.SWIPE_BACK_RIGHT_L.ordinal()),
//									dao.load((long) GestureEvent.Gestures.SWIPE_BACK_LEFT_P.ordinal()),
//									dao.load((long) GestureEvent.Gestures.SWIPE_BACK_RIGHT_P.ordinal()),
//								});
//							System.err.println(output.toString());
//							output = DolphinTrainner.createModel("cr_default.dolphin", new Gesture[]{
//									dao.load((long) GestureEvent.Gestures.CROSSOVER_CLOCKWISE.ordinal()),
//									dao.load((long) GestureEvent.Gestures.CROSSOVER_ANTICLOCK.ordinal()),
//								});
//							System.err.println(output.toString());
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
					}
				}.start();
				
			}
		});
//		installButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				new Installer().installAll(mContext);
//			}
//		});
//		settingButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				EventSettings settings = new EventSettings();
//				ScreenSetter setter = settings.new ScreenSetter(mContext); 
//				setter.start();
//			}
//		});
//		createButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				DaoManager.createDB(mContext);
//			}
//		});
		
		startService(new Intent(DolphinServerVariables.REMOTE_SERVICE_NAME));
		bindService(new Intent(DolphinServerVariables.REMOTE_SERVICE_NAME), mConn, Context.BIND_AUTO_CREATE);

	}
	
	@Override
	protected void onResume() {
		if(mService == null){
			
		}else if(mService.getDolphinState() == Dolphin.States.WORKING.ordinal()){
			toggle_service.setChecked(true);
		}else {
			toggle_service.setChecked(false);
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if(toogle_sfv.isChecked()){
			try {
				mService.returnDataReceiver();
			} catch (DolphinException e) {
				Log.e(TAG, e.toString());
			}
			toogle_sfv.setChecked(false);
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
        Log.d(TAG, "onDestroy");  
        super.onDestroy();
	}
}
