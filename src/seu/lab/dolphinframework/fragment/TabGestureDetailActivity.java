package seu.lab.dolphinframework.fragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import seu.lab.dolphin.client.ContinuousGestureEvent;
import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.DolphinException;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.IGestureListener;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.RawGestureData;
import seu.lab.dolphin.server.DaoManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import seu.lab.dolphinframework.R;

public class TabGestureDetailActivity extends Activity {
	
	private TextView gesture_name;
	private TextView gesture_instruction;
	private TextView gesture_accuracy;
	private RelativeLayout layout_basic_inf;
	private RelativeLayout layout_samples;
	private RelativeLayout layout_new_sample;
	Paint paint;

	private Gesture gesture = null;
	Context mContext = this;
	private LinearLayout graph_linear_layout;

	public TabGestureDetailActivity(){
		paint = new Paint();
		paint.setColor(Color.CYAN);
		paint.setStrokeWidth(5f);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle data = getIntent().getExtras();
		gesture = TabGesture.gestures.get(data.getInt("num"));
		
		if(gesture.getType() == 0){
			setContentView(R.layout.gesture_detail_simple);

			gesture_name = (TextView) findViewById(R.id.textView_gesture_name);
			gesture_instruction = (TextView) findViewById(R.id.textView_gesture_instruction);
			gesture_name.setText(gesture.getName());
			gesture_instruction.setText(gesture.getDiscription());
			
			return;
			
		}
		setContentView(R.layout.gesture_detail);

		gesture_name = (TextView) findViewById(R.id.textView_gesture_name);
		gesture_instruction = (TextView) findViewById(R.id.textView_gesture_instruction);
		gesture_accuracy = (TextView) findViewById(R.id.textView_gesture_detail_accuracy);
		layout_basic_inf=(RelativeLayout) findViewById(R.id.layout_gesture_detail_basic_inf);
		layout_samples=(RelativeLayout) findViewById(R.id.layout_gesture_detail_samples);
		layout_new_sample=(RelativeLayout) findViewById(R.id.layout_gesture_detail_new_sample);
		graph_linear_layout = (LinearLayout) findViewById(R.id.graph_linear_layout);
		
		gesture_name.setText(gesture.getName());
		gesture_instruction.setText(gesture.getDiscription());
		gesture_accuracy.setText(Long.toString(DaoManager.getDaoManager(mContext).countGestureRawData(gesture)));
		
		drawGraphs();
		
		layout_basic_inf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(TabGestureDetailActivity.this, test_activity.class);
				Bundle data=new Bundle();
				data.putString("txt", gesture.getName()+" set basic information");
				i.putExtras(data);
				TabGestureDetailActivity.this.startActivity(i);
			}
		});
		
		layout_samples.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(TabGestureDetailActivity.this, test_activity.class);
				Bundle data=new Bundle();
				data.putString("txt", gesture.getName()+" show all samples");
				i.putExtras(data);
				TabGestureDetailActivity.this.startActivity(i);
			}
		});
		
		layout_new_sample.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setMessage("点击确认按钮3秒钟后开始录制");
				builder.setTitle("录制新样本");
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						// 检测手势识别是否开启
						if(FragmentMainActivity.mService.getDolphinState() != Dolphin.States.WORKING.ordinal()) {
							Log.i("Add One", "No Server...");
							Toast.makeText(mContext, "超声波监听服务未打开", Toast.LENGTH_SHORT);
						} else {
							// 弹出菊花对话框
							final ProgressDialog pdialog = new ProgressDialog(mContext); 
							//设置进度条风格，风格为圆形，旋转的 
							pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
							//设置ProgressDialog 标题 
							pdialog.setTitle("进度对话框"); 
							//设置ProgressDialog 提示信息 
							pdialog.setMessage("处理中"); 
							//设置ProgressDialog 的进度条是否不明确 
							pdialog.setIndeterminate(false); 
							//设置ProgressDialog 是否可以按退回按键取消 
							pdialog.setCancelable(false); 
							//显示 
							pdialog.show(); 
							// 借用手势监听器
						    //创建Handler对象  
						    final Handler handler = new Handler();  
						    //新建一个线程对象  
						    Runnable updateThread = new Runnable(){  
						        //将要执行的操作写在线程对象的run方法当中  
						        public void run(){  
						            int num = 3;
						            while(num >= 0) {
						            	try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
						            	num--;
						            	pdialog.setMessage("请准备:"+num);
						            }
						            pdialog.setMessage("请做手势");
						            try {
										FragmentMainActivity.mService.borrowGestureListener(new IGestureListener() {
											@Override
											public void onGesture(GestureEvent arg0) {
												if(!arg0.isConclusion || arg0.rich_feature_data==null) {
													return;
												}
												// 接收到事件
												pdialog.dismiss();
												// 添加新样本 
												DaoManager.getDaoManager(mContext).addRawGestureData(gesture, arg0);
												
												try {
													FragmentMainActivity.mService.returnGestureListener();
												} catch (DolphinException e) {
													e.printStackTrace();
												} catch (JSONException e) {
													e.printStackTrace();
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
												JSONObject config = new JSONObject();
												JSONObject masks = new JSONObject();
												
												for (int j = 0; j < GestureEvent.gesture.length; j++) {
													try {
														masks.put(""+j,true);
													} catch (JSONException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}
												try {
													config.put("masks", masks);
												} catch (JSONException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												return config;
											}
										});
									} catch (DolphinException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
						        }  
						    };
						    handler.post(updateThread);
						}
						dialog.dismiss();
					}
					
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
	protected void onStop() {
		super.onStop();
//		new Thread(){
//			@Override
//			public void run(){
//				DaoManager.getDaoManager(mContext).refreshGesture(gesture);
//			}
//		}.start();
	}
	
	void drawGraphs(){
		List<RawGestureData> datas = gesture.getRaw_gesuture_data();
        
		DisplayMetrics dm = getResources().getDisplayMetrics();
		float w_screen = dm.widthPixels;
        
        float step = w_screen/30;

        int color_range = Color.RED - Color.CYAN;
        
		for (int i = 0; i < 10; i++) {			
			ImageView img = new ImageView(mContext);
			
			Bitmap bitmap = Bitmap.createBitmap((int) w_screen, 300, Bitmap.Config.ARGB_8888);
			
			double[] data = DaoManager.toDoubleArray(datas.get(datas.size()-i-1).getData());

			Canvas canvas = new Canvas(bitmap);
			
			canvas.drawColor(Color.WHITE);
			
			double max = data[0];
			for (int j = 0; j < data.length; j++) {
				if(max < data[j])max = data[j];
			}

			Log.i("data", data.length+"");
			for (int j = 0; j < 30; j++) {
				for (int j2 = 0; j2 < 60; j2++) {
					double d = data[j*60+j2] > 0.001 ? data[j*60+j2]/max : 0;
					paint.setColor((int) (d*color_range + Color.CYAN));
					canvas.drawLine(j*step, (59-j2)*5, (j+1)*step, (59-j2)*5, paint);
				}
			}
			canvas.save();
			
			img.setImageBitmap(bitmap);
			
			graph_linear_layout.addView(img);

		}
	}
}
