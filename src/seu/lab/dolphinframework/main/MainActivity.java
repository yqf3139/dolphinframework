package seu.lab.dolphinframework.main;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.DolphinException;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.IDataReceiver;
import seu.lab.dolphin.client.RealTimeData;
import seu.lab.dolphin.dao.DaoSession;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.GestureDao;
import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.learn.DolphinTrainner;
import seu.lab.dolphin.server.AppPreferences;
import seu.lab.dolphin.server.DaoManager;
import seu.lab.dolphin.server.DolphinServerVariables;
import seu.lab.dolphin.server.RemoteService;
import seu.lab.dolphin.server.RemoteService.RemoteBinder;
import seu.lab.dolphin.server.UserPreferences;
import seu.lab.dolphin.sysplugin.EventSettings;
import seu.lab.dolphin.sysplugin.Installer;
import seu.lab.dolphin.sysplugin.EventSettings.ScreenSetter;
import seu.lab.dolphinframework.R;
import seu.lab.dolphinframework.fragment.FragmentMainActivity;
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
import android.text.TextUtils.TruncateAt;
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
	public static Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		Button test = (Button) findViewById(R.id.test_btn);
//		test.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				//startActivity(new Intent(getApplicationContext(), GraphActivity.class));
//				DaoManager manager = DaoManager.getDaoManager(mContext);
//				DaoSession session = manager.getDaoSession();
//				Plugin plugin = session.getPluginDao().load(1l);
////				List<Pla>manager.listPlaybackEventsRecord(plugin);
//			}
//		});
//
//		Button start = (Button) findViewById(R.id.start_btn);
//		start.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				//startActivity(new Intent(getApplicationContext(), GraphActivity.class));
//				Intent i=new Intent(mContext,FreshmanActivity.class);
//				startActivity(i);
//				finish();
//			}
//		});
//		
//		Button trainButton = (Button) findViewById(R.id.train);
//
//		trainButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				new Thread(){
//					@Override
//					public void run() {
//						
//						GestureDao dao = DaoManager.getDaoManager(mContext).getDaoSession().getGestureDao();
//						
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
//					}
//				}.start();
//			}
//		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!AppPreferences.isInitialized(mContext)){
			Log.i(TAG, "first run: initing Plugin & DB");
			Intent i=new Intent(mContext,FreshmanActivity.class);
			startActivity(i);
			finish();
		}else {
			// TODO restore prefs
			AppPreferences.resume(mContext);
			UserPreferences.refresh(mContext);
			Intent i=new Intent(mContext,FragmentMainActivity.class);
			startActivity(i);
			finish();
		}
	}
}
