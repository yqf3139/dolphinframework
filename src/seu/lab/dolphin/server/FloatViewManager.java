package seu.lab.dolphin.server;

import seu.lab.dolphin.dao.DolphinContext;
import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.sysplugin.EventRecordWatcher;
import seu.lab.dolphin.sysplugin.EventRecorder;
import seu.lab.dolphin.sysplugin.EventSenderForPlayback;
import seu.lab.dolphinframework.R;
import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FloatViewManager {
	private static final String TAG = "FlowViewManager";
	
	private static FloatViewManager manager = null;
	
	Context mContext;
	
	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	WindowManager mWindowManager;
	Button mFloatRecordButton;
	Button mFloatPlaybackButton;
	Button mFloatPickButton;
	Button mFloatDropButton;

	ImageView mFloatbarImage;

	Handler mHandler = new Handler();

	private Class<?> backClass = null;

	private Activity backActivity;

	private String scriptName;

	private long pluginId;
	
	private int type = 0;
	
	private int state = -1;
	
	private FloatViewManager(Context context){
		this.mContext = context;
	}
	
	public static FloatViewManager getFlowViewManager(Context context) {
		if(manager == null)manager = new FloatViewManager(context);
		return manager;
	}
	
	public void createFloatView() {
		Log.i(TAG, "Float View created");
		
		if(wmParams != null)return;
		
		wmParams = new WindowManager.LayoutParams();
		mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = wmParams.y = 0;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout,null);
		mFloatbarImage = (ImageView) mFloatLayout.findViewById(R.id.float_bar);
		mFloatRecordButton = (Button) mFloatLayout.findViewById(R.id.float_record_button);
		mFloatPlaybackButton = (Button) mFloatLayout.findViewById(R.id.float_playback_button);
		mFloatPickButton = (Button) mFloatLayout.findViewById(R.id.float_pick_button);
		mFloatDropButton = (Button) mFloatLayout.findViewById(R.id.float_drop_button);
		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		
		mFloatLayout.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY;
			int paramX, paramY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = wmParams.x;
					paramY = wmParams.y;
					break;
				case MotionEvent.ACTION_MOVE:
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

				switch (state) {
				case -1: // not start
					new EventRecordWatcher(new EventRecorder(5,scriptName)).start();
					state = 5;
					new Thread() {
						public void run() {
							int num = 5;
							mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									mFloatRecordButton.setText(Integer.toString(5));
									mFloatRecordButton.setBackgroundColor(Color.BLACK);
								}
							});
							while (num > 0) {
								try {
									sleep(1000);
									final int a = --num;
									mHandler.post(new Runnable() {
										
										@Override
										public void run() {
											mFloatRecordButton.setText(Integer.toString(a));
										}
									});

								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							try {
								sleep(1000);
								state = -1;// tobe confirmed
								mHandler.post(new Runnable() {
									
									@Override
									public void run() {
										mFloatRecordButton.setText("");
										mFloatRecordButton.setBackgroundResource(android.R.drawable.ic_menu_add);
										mFloatPickButton.setVisibility(View.VISIBLE);
										mFloatPlaybackButton.setVisibility(View.VISIBLE);
									}
								});
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}.start();
					break;

				default:
					break;
				}
			}
		});
		
		mFloatPlaybackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new EventSenderForPlayback(scriptName).start();
			}
		});

		mFloatPickButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(type == 1){
					jumpBack("录制成功");
					return;
				}
				ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
				ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
				final String name = cn.getClassName();
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						DolphinContext context = new DolphinContext(null, name, "still", 0l, 0l);
						long id = DaoManager.getDaoManager(mContext).addDolphinContext(context, pluginId);
						if(id == 0){
							jumpBack("重复的插件环境");
						}else {
							jumpBack("插件环境选择成功");
						}
					}
				});
			}
		});
		
		mFloatDropButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clean();
				jumpBack("放弃选取");
			}
		});
		
	}

	public void showFloatViewForRecord(Class<?> back, Activity backActivity, String name) {
		type = 1;
		backClass = back;
		scriptName = name;
		this.backActivity = backActivity;
		if(mFloatLayout == null)createFloatView();
		if(!mFloatLayout.isShown()){
			mFloatRecordButton.setVisibility(View.VISIBLE);
			mFloatPlaybackButton.setVisibility(View.GONE);
			mFloatPickButton.setVisibility(View.GONE);

			mWindowManager.addView(mFloatLayout, wmParams);
		}
	}
	
	public void showFloatViewForActivity(Class<?> back, Activity backActivity, long plugin_id) {
		type = 2;
		backClass = back;
		pluginId = plugin_id;
		this.backActivity = backActivity;
		if(mFloatLayout == null)createFloatView();
		if(!mFloatLayout.isShown()){
			mFloatRecordButton.setVisibility(View.GONE);
			mFloatPlaybackButton.setVisibility(View.GONE);
			mFloatPickButton.setVisibility(View.VISIBLE);
			
			mWindowManager.addView(mFloatLayout, wmParams);
		}
	}
	
	public void hideFloatView() {
		if(mFloatLayout == null)createFloatView();
		if(mFloatLayout.isShown())
			mWindowManager.removeView(mFloatLayout);
	}

	void clean(){
		switch (type) {
		case 1:
			
			break;
		case 2:
			Plugin plugin = DaoManager.getDaoManager(mContext).getDaoSession().getPluginDao().load(pluginId);
			if(plugin != null)
				DaoManager.getDaoManager(mContext).deletePlugin(plugin);
			break;
		default:
			break;
		}
	}
	
	void jumpBack(String toastMsg){
		Intent intent = new Intent(mContext, backClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		hideFloatView();
		backActivity.finish();

		Toast.makeText(mContext, toastMsg, Toast.LENGTH_SHORT).show();
		mContext.startActivity(intent);
	}
}
