package seu.lab.dolphinframework;
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
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		
		toggleButton = (ToggleButton) findViewById(R.id.toggle_service);
		toggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(toggleButton.isChecked()){
					startService(new Intent(DolphinServerVariables.REMOTE_SERVICE_NAME));
					bindService(new Intent(DolphinServerVariables.REMOTE_SERVICE_NAME), mConn, Context.BIND_AUTO_CREATE);
				}else {
			        unbindService(mConn);
			        stopService(new Intent(DolphinServerVariables.REMOTE_SERVICE_NAME));
			        mService = null;
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
	            	mService.getForeground();
	            }
			}
		});
		
		Button keycodeButton = (Button) findViewById(R.id.keycode_btn);
		Button swipeButton = (Button) findViewById(R.id.swipe_btn);
		Button playbackButton = (Button) findViewById(R.id.playback_btn);
		Button recordButton = (Button) findViewById(R.id.record_btn);
		Button installButton = (Button) findViewById(R.id.install_btn);
		Button settingButton = (Button) findViewById(R.id.screen_set_btn);
		Button createButton = (Button) findViewById(R.id.create_db_btn);


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
				new EventSenderForPlayback().start();
			}
		});
		recordButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new EventRecordWatcher(new EventRecorder(5)).start();
				//new EventRecorder(5).start();
			}
		});
		installButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new Installer().installAll(mContext);
			}
		});
		settingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EventSettings settings = new EventSettings();
				ScreenSetter setter = settings.new ScreenSetter(); 
				setter.start();
			}
		});
		createButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DaoManager.createDB(mContext);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
        Log.d(TAG, "onDestroy");  
        super.onDestroy();
	}
}
