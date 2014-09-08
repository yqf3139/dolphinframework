package seu.lab.dolphinframework;
import seu.lab.dolphin.client.IDataReceiver;
import seu.lab.dolphin.client.RealTimeData;
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
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	static final String TAG = "DFMainActivity";
	ToggleButton toggleButton;
	public static Context mContext;
	private RemoteService mService = null;

	
	private Paint tPaint;
	private SurfaceView sfv;

	public MainActivity() {
		tPaint = new Paint();
		tPaint.setColor(Color.MAGENTA);
		tPaint.setStrokeWidth(1);
		tPaint.setAntiAlias(true);
	}
	
	IDataReceiver receiver = new IDataReceiver() {
		
		@Override
		public void onData(RealTimeData arg0) {
			SimpleDrawCircle(arg0.radius);
		}
		
		@Override
		public Bundle getDataTypeMask() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	private void SimpleDrawCircle(double radius) {
		Canvas canvas = sfv.getHolder().lockCanvas(
				new Rect(0, 0, 1024, sfv.getHeight()));
		if (canvas == null)
			return;
		canvas.drawColor(Color.WHITE);
		canvas.drawCircle(250, 100, (float) (((radius + 1) * 80) + 10), tPaint);
		canvas.save();
		sfv.getHolder().unlockCanvasAndPost(canvas);
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

            Toast.makeText(mContext, mService.hello("yqf"), Toast.LENGTH_SHORT).show();  
			
		}
	};
	private ToggleButton toogle_sfv;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
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

				}
			}.start();
		}else {
			// TODO restore prefs
			EventSettings.EVENT_ID = AppPreferences.getPreferences().getInt("event_id", 1);
			
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01);

		toggleButton = (ToggleButton) findViewById(R.id.toggle_service);
		toggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(toggleButton.isChecked()){
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
					mService.borrowDataReceiver(receiver);
				}else {
					mService.returnDataReceiver();
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
		
		Button keycodeButton = (Button) findViewById(R.id.keycode_btn);
		Button swipeButton = (Button) findViewById(R.id.swipe_btn);
		Button playbackButton = (Button) findViewById(R.id.playback_btn);
		Button recordButton = (Button) findViewById(R.id.record_btn);
//		Button installButton = (Button) findViewById(R.id.install_btn);
//		Button settingButton = (Button) findViewById(R.id.screen_set_btn);
//		Button createButton = (Button) findViewById(R.id.create_db_btn);


		keycodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new EventSenderForKey(EventSenderForKey.KEYCODE_HOME).start();
			}
		});
		swipeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new EventSenderForSwipe(EventSenderForSwipe.SwipeChoice.to_down).start();
			}
		});
		playbackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new EventSenderForPlayback("last_events").start();
			}
		});
		recordButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new EventRecordWatcher(new EventRecorder(5)).start();
				//new EventRecorder(5).start();
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
	protected void onDestroy() {
        Log.d(TAG, "onDestroy");  
        super.onDestroy();
	}
}
