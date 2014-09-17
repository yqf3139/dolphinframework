package seu.lab.dolphinframework.fragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import seu.lab.dolphin.client.ContinuousGestureEvent;
import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.IGestureListener;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.RawGestureData;
import seu.lab.dolphin.server.DaoManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
		gesture_accuracy.setText("  "+99+"%  ");
		
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
				
				// 检测手势识别是否开启
				// FragmentMainActivity.mService.getDolphinState() == Dolphin.States.WORKING.ordinal()
				
				// 借用手势监听器
//				FragmentMainActivity.mService.borrowGestureListener(new IGestureListener() {
//					
//					@Override
//					public void onGesture(GestureEvent arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onContinuousGestureUpdate(ContinuousGestureEvent arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onContinuousGestureStart(ContinuousGestureEvent arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onContinuousGestureEnd() {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public JSONObject getGestureConfig() {
//						JSONObject config = new JSONObject();
//						JSONObject masks = new JSONObject();
//						
//						for (int j = 0; j < GestureEvent.gesture.length; j++) {
//							masks.put(""+j,true);
//						}
//						
//						return config;
//					}
//				});
				
				// 添加新样本 
				// DaoManager.getDaoManager(mContext).addRawGestureData(gesture, event);
				
				// 全部添加完毕后调用
//				DaoManager.getDaoManager(mContext).refreshGesture(gesture);
			}
		});
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
