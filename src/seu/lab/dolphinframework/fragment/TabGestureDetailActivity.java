package seu.lab.dolphinframework.fragment;

import seu.lab.dolphin.dao.Gesture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	private Gesture gesture = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesture_detail);

		gesture_name = (TextView) findViewById(R.id.textView_gesture_name);
		gesture_instruction = (TextView) findViewById(R.id.textView_gesture_instruction);
		gesture_accuracy = (TextView) findViewById(R.id.textView_gesture_detail_accuracy);
		layout_basic_inf=(RelativeLayout) findViewById(R.id.layout_gesture_detail_basic_inf);
		layout_samples=(RelativeLayout) findViewById(R.id.layout_gesture_detail_samples);
		layout_new_sample=(RelativeLayout) findViewById(R.id.layout_gesture_detail_new_sample);
		
		Bundle data = getIntent().getExtras();
		gesture = TabGesture.gestures.get(data.getInt("num"));
		
		gesture_name.setText(gesture.getName());
		gesture_instruction.setText(gesture.getDiscription());
		
		gesture_accuracy.setText("  "+99+"%  ");
		
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
				Intent i=new Intent(TabGestureDetailActivity.this, test_activity.class);
				Bundle data=new Bundle();
				data.putString("txt", gesture.getName()+" new sample");
				i.putExtras(data);
				TabGestureDetailActivity.this.startActivity(i);
			}
		});
	}
}
