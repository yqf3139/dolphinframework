package seu.lab.dolphin.server;

import seu.lab.dolphin.sysplugin.EventRecordWatcher;
import seu.lab.dolphin.sysplugin.EventRecorder;
import seu.lab.dolphin.sysplugin.EventSenderForPlayback;
import seu.lab.dolphinframework.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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
	ImageView mFloatbarImage;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String Text = bundle.getString("text");
			mFloatRecordButton.setText(Text);
			
			if(Text.equals("record") && backClass != null && backActivity != null){
				Intent intent = new Intent(mContext, backClass);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				hideFloatView();
				backActivity.finish();

				Toast.makeText(mContext, "录制成功", Toast.LENGTH_SHORT).show();
				mContext.startActivity(intent);
			}
		}
	};

	private Class<?> backClass = null;

	private Activity backActivity;

	private String scriptName;
	
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

				if (mFloatRecordButton.getText().equals("record")) {
					new EventRecordWatcher(new EventRecorder(5,scriptName)).start();
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
				new EventSenderForPlayback(scriptName).start();
			}
		});

	}

	public void showFloatView(Class<?> back, Activity backActivity, String name) {
		backClass = back;
		scriptName = name;
		this.backActivity = backActivity;
		if(mFloatLayout == null)createFloatView();
		if(!mFloatLayout.isShown())
			mWindowManager.addView(mFloatLayout, wmParams);
	}
	
	public void hideFloatView() {
		if(mFloatLayout == null)createFloatView();
		if(mFloatLayout.isShown())
			mWindowManager.removeView(mFloatLayout);
	}
	
}
