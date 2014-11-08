package seu.lab.dolphinframework.main;

import java.io.File;

import seu.lab.dolphin.server.DaoManager;
import seu.lab.dolphin.server.DolphinServerVariables;
import seu.lab.dolphin.server.FloatViewManager;
import seu.lab.dolphin.server.UserPreferences;
import seu.lab.dolphin.sysplugin.Installer;
import seu.lab.dolphinframework.R;
import seu.lab.dolphinframework.fragment.FragmentMainActivity;
import seu.lab.dolphinframework.fragment.test_activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class GuideActivity extends Activity {
	static final String TAG = "GuideActivity";

	static Context mContext;

	private RelativeLayout layout_guide_record_unlock;
	private RelativeLayout layout_default_settings;
	private RelativeLayout layout_about_dolphin;
	private TextView text_guide_light;
	private Switch switch_guide_light;
	private Switch switch_guide_auto_start;
	private Switch switch_guide_enable_unlock;
	private Switch switch_guide_motion_mask;
	private Switch switch_guide_auto_sleep;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);

		mContext = this;

		layout_guide_record_unlock = (RelativeLayout) findViewById(R.id.layout_guide_record_unlock);
		layout_default_settings = (RelativeLayout) findViewById(R.id.layout_guide_default_settings);
		layout_about_dolphin = (RelativeLayout) findViewById(R.id.layout_guide_about_dolphin);

		text_guide_light = (TextView) findViewById(R.id.text_guide_light);

		switch_guide_light = (Switch) findViewById(R.id.switch_guide_light);
		switch_guide_auto_start = (Switch) findViewById(R.id.switch_guide_auto_start);
		switch_guide_enable_unlock = (Switch) findViewById(R.id.switch_guide_enable_unlock);
		switch_guide_motion_mask = (Switch) findViewById(R.id.switch_guide_motion_mask);
		switch_guide_auto_sleep = (Switch) findViewById(R.id.switch_guide_auto_sleep);

		layout_guide_record_unlock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// open guide page
				Intent i = new Intent(GuideActivity.this, test_activity.class);
				Bundle data = new Bundle();
				data.putString("txt", " layout_acuity");
				i.putExtras(data);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);

				FloatViewManager manager = FloatViewManager
						.getFlowViewManager(mContext);
				manager.showFloatViewForRecord(GuideActivity.class,
						GuideActivity.this, "unlock_script");
			}
		});
		layout_default_settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// open guide page
				Toast.makeText(mContext, "Dolphin 后台正在重新配置", Toast.LENGTH_SHORT)
						.show();
				UserPreferences.clear(mContext);
				UserPreferences.refresh(mContext);
				setSwitches();
				new Thread() {
					public void run() {
						DaoManager manager = DaoManager.getDaoManager(mContext);
						manager.dropDB();
						manager.createDB();
						if(manager.canUpdatePlugins())
							manager.updateAllPlugins();
						Handler handler = new Handler(Looper.getMainLooper());
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(GuideActivity.mContext,
										"Dolphin 配置完成", Toast.LENGTH_SHORT)
										.show();
							}
						});
					}
				}.start();
			}
		});
		layout_about_dolphin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// open guide page
				Intent i = new Intent(GuideActivity.this, InfoActivity.class);
				startActivity(i);
			}
		});

		text_guide_light.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final EditText editText = new EditText(mContext);
				new AlertDialog.Builder(mContext).setTitle("请输入服务器IP")
						.setIcon(android.R.drawable.ic_menu_edit)
						.setView(editText)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								UserPreferences.set("light_server", editText.getText().toString());
								UserPreferences.refresh(mContext);
								Log.i(TAG, "light_server update to "+editText.getText().toString());
							}
						})
						.setNegativeButton("取消", null)
						.show();
			}
		});

		switch_guide_light
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					UserPreferences.set("light", true);
					UserPreferences.refresh(mContext);
					Log.i(TAG, "switch_guide_light switch "+UserPreferences.needLight);
				} else {
					UserPreferences.set("light", false);
					UserPreferences.refresh(mContext);
					Log.i(TAG, "switch_guide_light switch "+UserPreferences.needLight);
				}
			}
		});

		switch_guide_auto_start
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							UserPreferences.set("auto_start", true);
							UserPreferences.refresh(mContext);
							Log.i(TAG, "switch_guide_auto_start switch on");
						} else {
							UserPreferences.set("auto_start", false);
							UserPreferences.refresh(mContext);
							Log.i(TAG, "switch_guide_auto_start switch off");
						}
					}
				});

		switch_guide_enable_unlock
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							UserPreferences.set("enable_unlock", true);
							UserPreferences.refresh(mContext);
							Log.i(TAG, "switch_guide_enable_unlock switch on");

						} else {
							UserPreferences.set("enable_unlock", false);
							UserPreferences.refresh(mContext);
							Log.i(TAG, "switch_guide_enable_unlock switch off");
						}
					}
				});

		switch_guide_motion_mask
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							UserPreferences.set("motion_mask", true);
							UserPreferences.refresh(mContext);
							Log.i(TAG, "switch_guide_motion_mask switch on");
						} else {
							UserPreferences.set("motion_mask", false);
							UserPreferences.refresh(mContext);
							Log.i(TAG, "switch_guide_motion_mask switch off");
						}
					}
				});

		switch_guide_auto_sleep
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							UserPreferences.set("auto_sleep", true);
							UserPreferences.refresh(mContext);
							Log.i(TAG, "switch_guide_sleep switch on");
						} else {
							UserPreferences.set("auto_sleep", false);
							UserPreferences.refresh(mContext);
							Log.i(TAG, "switch_guide_sleep switch off");
						}
					}
				});
	}

	void setSwitches() {
		UserPreferences.refresh(this);

		switch_guide_auto_start.setChecked(UserPreferences.needAutoStart);
		File file = new File(DolphinServerVariables.DOLPHIN_HOME
				+ "scripts/unlock_script");
		if (file.exists()) {
			switch_guide_enable_unlock.setEnabled(true);
			switch_guide_enable_unlock
					.setChecked(UserPreferences.needEnableUnlock);
		} else {
			switch_guide_enable_unlock.setEnabled(false);
			switch_guide_enable_unlock.setChecked(false);
		}
		switch_guide_motion_mask.setChecked(UserPreferences.needMotionMask);
		switch_guide_auto_sleep.setChecked(UserPreferences.needAutoSleep);
		switch_guide_light.setChecked(UserPreferences.needLight);
	}

	@Override
	protected void onResume() {

		setSwitches();

		super.onResume();
	}

}
