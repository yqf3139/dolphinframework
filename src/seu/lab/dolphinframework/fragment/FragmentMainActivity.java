package seu.lab.dolphinframework.fragment;

import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.server.RemoteService;
import seu.lab.dolphinframework.R;
import seu.lab.dolphinframework.main.GuideActivity;
import seu.lab.dolphinframework.main.MainActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentMainActivity extends Activity implements OnClickListener {
	
	public static RemoteService mService = null;
	
	private TabIndex tab_index;
	private TabGesture tab_gesture;
	private TabExpansion tab_expansion;

	private LinearLayout tab_btn_index;
	private LinearLayout tab_btn_gesture;
	private LinearLayout tab_btn_expansion;
	private Switch switch_dolphin;
	private ImageButton bt_settings;

	private FragmentManager fragmentManager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		this.mService = MainActivity.mService;
		initViews();
		fragmentManager = getFragmentManager();
		setTabSelection(0);
		
		switch_dolphin = (Switch) findViewById(R.id.switch_dolphin_bar);
		bt_settings = (ImageButton) findViewById(R.id.btn_dolphin_bar_setting);
		
		switch_dolphin.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                // TODO Auto-generated method stub  
                if (isChecked) {
                	mService.startRecognition();
//                	mService..showFloatView();
                	Toast.makeText(MainActivity.mContext, "Service is On", Toast.LENGTH_SHORT).show();
                } else {
                	mService.stopRecognition();
//                	mService.hideFloatView();
                	Toast.makeText(MainActivity.mContext, "Service is Off", Toast.LENGTH_SHORT).show();
                }  
            }  
        });  
		
		bt_settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//open guide page
				Intent i=new Intent(FragmentMainActivity.this, GuideActivity.class);
				Bundle data=new Bundle();
				data.putString("txt"," nothing");
				i.putExtras(data);
				FragmentMainActivity.this.startActivity(i);
			}
		});
	}

	

	private void initViews() {

		tab_btn_index = (LinearLayout) findViewById(R.id.lyt_fragment_tab_index);
		tab_btn_gesture = (LinearLayout) findViewById(R.id.lyt_fragment_tab_gesture);
		tab_btn_expansion = (LinearLayout) findViewById(R.id.lyt_fragment_tab_expansion);

		tab_btn_index.setOnClickListener(this);
		tab_btn_gesture.setOnClickListener(this);
		tab_btn_expansion.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lyt_fragment_tab_index:
			setTabSelection(0);
			break;
		case R.id.lyt_fragment_tab_gesture:
			setTabSelection(1);
			break;
		case R.id.lyt_fragment_tab_expansion:
			setTabSelection(2);
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	private void setTabSelection(int index) {
		int getcolor_pressed =getResources().getColor(R.color.deep_sky_blue);
		resetBtn();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (index) {
		case 0:
			(tab_btn_index.findViewById(R.id.btn_index_pressed)).setBackgroundColor(getcolor_pressed);
			if (tab_index == null) {
				tab_index = new TabIndex();
				transaction.add(R.id.frm_fragment, tab_index);
			} else {
				transaction.show(tab_index);
			}
			
			break;
		case 1:
			(tab_btn_gesture.findViewById(R.id.btn_gesture_pressed)).setBackgroundColor(getcolor_pressed);
			if (tab_gesture == null) {
				tab_gesture = new TabGesture();
				transaction.add(R.id.frm_fragment, tab_gesture);
			} else {
				transaction.show(tab_gesture);
			}
			break;
		case 2:
			(tab_btn_expansion.findViewById(R.id.btn_expansion_pressed)).setBackgroundColor(getcolor_pressed);
			if (tab_expansion == null) {
				tab_expansion = new TabExpansion();
				transaction.add(R.id.frm_fragment, tab_expansion);
			} else {
				transaction.show(tab_expansion);
			}
			break;
		}
		transaction.commit();
	}

	private void resetBtn() {
		int getcolor_normal = Resources.getSystem().getColor(android.R.color.transparent);
		(tab_btn_index.findViewById(R.id.btn_index_pressed)).setBackgroundColor(getcolor_normal);
		(tab_btn_gesture.findViewById(R.id.btn_gesture_pressed)).setBackgroundColor(getcolor_normal);
		(tab_btn_expansion.findViewById(R.id.btn_expansion_pressed)).setBackgroundColor(getcolor_normal);

	}
	
	@SuppressLint("NewApi")
	private void hideFragments(FragmentTransaction transaction) {
		if (tab_index != null) {
			transaction.hide(tab_index);
		}
		if (tab_gesture != null) {
			transaction.hide(tab_gesture);
		}
		if (tab_expansion != null) {
			transaction.hide(tab_expansion);
		}
	}

	@Override
	protected void onResume() {
		if(mService == null){
			
		}else if(mService.getDolphinState() == Dolphin.States.WORKING.ordinal()){
			switch_dolphin.setChecked(true);
		}else {
			switch_dolphin.setChecked(false);
		}
		super.onResume();
	}
	
}
