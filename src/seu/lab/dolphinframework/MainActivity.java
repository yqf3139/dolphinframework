package seu.lab.dolphinframework;

import seu.lab.dolphin.server.IRemoteService;
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
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	static final String TAG = "DFMainActivity";
	ToggleButton toggleButton;
	private IRemoteService mService = null;
	Context mContext = this;
	
	private ServiceConnection mConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.d(TAG, TAG + " Service Disconnected.");
            Toast.makeText(mContext, TAG + " Service Disconnected.", Toast.LENGTH_SHORT).show();

		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			mService = IRemoteService.Stub.asInterface(service);
			Log.d(TAG, TAG + " Service Connected.");
            try {  
                Toast.makeText(mContext, mService.hello("yqf"), Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
			
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		toggleButton = (ToggleButton) findViewById(R.id.toggle_service);
		toggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(toggleButton.isChecked()){
					startService(new Intent("seu.lab.dolphin.server.RemoteService"));
					bindService(new Intent("seu.lab.dolphin.server.RemoteService"), mConn, Context.BIND_AUTO_CREATE);
				}else {
			        unbindService(mConn);
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
        Log.d(TAG, TAG + " onDestroy().");  
        super.onDestroy();
	}
}
