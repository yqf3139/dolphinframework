package seu.lab.dolphinframework.main;

import seu.lab.dolphinframework.R;
import seu.lab.dolphinframework.fragment.FragmentMainActivity;
import seu.lab.dolphinframework.fragment.test_activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class GuideActivity extends Activity {
	private RelativeLayout layout_acuity;
	private RelativeLayout layout_default_settings;
	private RelativeLayout layout_about_dolphin;
	private RelativeLayout layout_update;
	private Switch switch_auto_start;
	private Switch switch_app_switch;
	private Switch switch_screen_capture;
	private Switch switch_preference_learning;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		
		layout_acuity=(RelativeLayout) findViewById(R.id.layout_guide_acuity);
		layout_default_settings=(RelativeLayout) findViewById(R.id.layout_guide_default_settings);
		layout_about_dolphin=(RelativeLayout) findViewById(R.id.layout_guide_about_dolphin);
		layout_update=(RelativeLayout) findViewById(R.id.layout_guide_update);
		
		switch_auto_start=(Switch) findViewById(R.id.switch_guide_auto_start);
		switch_app_switch=(Switch) findViewById(R.id.switch_guide_app_switch);
		switch_screen_capture=(Switch) findViewById(R.id.switch_guide_screen_capture);
		switch_preference_learning=(Switch) findViewById(R.id.switch_guide_preference_learning);
		
		layout_acuity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//open guide page
				Intent i=new Intent(GuideActivity.this, test_activity.class);
				Bundle data=new Bundle();
				data.putString("txt"," layout_acuity");
				i.putExtras(data);
				GuideActivity.this.startActivity(i);
			}
		});
		layout_default_settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//open guide page
				Intent i=new Intent(GuideActivity.this, test_activity.class);
				Bundle data=new Bundle();
				data.putString("txt"," layout_default_settings");
				i.putExtras(data);
				GuideActivity.this.startActivity(i);
			}
		});
		layout_about_dolphin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//open guide page
				Intent i=new Intent(GuideActivity.this, test_activity.class);
				Bundle data=new Bundle();
				data.putString("txt"," layout_about_dolphin");
				i.putExtras(data);
				GuideActivity.this.startActivity(i);
			}
		});
		layout_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//open guide page
				Intent i=new Intent(GuideActivity.this, test_activity.class);
				Bundle data=new Bundle();
				data.putString("txt"," layout_update");
				i.putExtras(data);
				GuideActivity.this.startActivity(i);
			}
		});
		
		switch_auto_start.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                // TODO Auto-generated method stub  
                if (isChecked) {  
                	System.out.println("switch_auto_start switch on");
                } else {  
                	System.out.println("switch_auto_start switch off");
                }  
            }  
        }); 
		switch_app_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                // TODO Auto-generated method stub  
                if (isChecked) {  
                	System.out.println("switch_app_switch switch on");
                } else {  
                	System.out.println("switch_app_switch switch off");
                }  
            }  
        }); 
		switch_screen_capture.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                // TODO Auto-generated method stub  
                if (isChecked) {  
                	System.out.println("switch_screen_capture switch on");
                } else {  
                	System.out.println("switch_screen_capture switch off");
                }  
            }  
        }); 
		switch_preference_learning.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                // TODO Auto-generated method stub  
                if (isChecked) {  
                	System.out.println("switch_preference_learning switch on");
                } else {  
                	System.out.println("switch_preference_learning switch off");
                }  
            }  
        }); 
	}
	
}
