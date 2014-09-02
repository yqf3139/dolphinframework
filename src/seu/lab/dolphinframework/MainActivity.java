package seu.lab.dolphinframework;
import seu.lab.dolphin.server.RemoteService;
import seu.lab.dolphin.server.RemoteService.RemoteBinder;
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
					startService(new Intent(RemoteService.REMOTE_SERVICE_NAME));
					bindService(new Intent(RemoteService.REMOTE_SERVICE_NAME), mConn, Context.BIND_AUTO_CREATE);
				}else {
			        unbindService(mConn);
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
	}
	
	@Override
	protected void onDestroy() {
        Log.d(TAG, "onDestroy");  
        super.onDestroy();
	}
}
