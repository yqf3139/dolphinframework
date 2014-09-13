package seu.lab.dolphinframework.fragment;

import java.util.List;

import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.dao.Rule;
import seu.lab.dolphin.server.DaoManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import seu.lab.dolphinframework.R;

public class TabExpansionDetailActivity extends Activity {
	
	static final String TAG = "TabExpansionDetailActivity";
	
	private TextView plug_name;
	private TextView plug_instruction;
	private ListView lv_plug_actions;
	private Button bt_new_action;
    private Switch plug_switcher;
    
	private PlugActionAdapter adapter = null;
	
	private Plugin plugin = null;
	
	private Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expansion_detail);

		plug_name = (TextView) findViewById(R.id.textView_expansion_detail_plug_name);
		plug_instruction = (TextView) findViewById(R.id.textView_expansion_detail_plug_instruction);
		lv_plug_actions = (ListView) findViewById(R.id.listView_plug_actions);
		bt_new_action=(Button) findViewById(R.id.button_expansion_detail_new_action);
		plug_switcher=(Switch) findViewById(R.id.switch_expansion_detail_plug_manager);
		
		Bundle data = getIntent().getExtras();
		plugin = TabExpansion.plugins.get(data.getInt("num"));
		
		plug_name.setText(plugin.getName());
		plug_instruction.setText(plugin.getDiscription());
		
		adapter = new PlugActionAdapter(TabExpansionDetailActivity.this);
		adapter.add(plugin);
		lv_plug_actions.setAdapter(adapter);
		
		//button:add a new action
		bt_new_action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			//	adapter.add("下滑", "滚动");//why cannot work?!!!!!!!

				//open the detail activity when click on the corresponding layout,deliver the plug name
				Intent i=new Intent(TabExpansionDetailActivity.this, test_activity.class);
				Bundle data=new Bundle();
				data.putString("txt", plugin.getName()+" new action");
				i.putExtras(data);
				TabExpansionDetailActivity.this.startActivity(i);
			}
		});
		
		plug_switcher.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                // TODO Auto-generated method stub  
                if (isChecked) {  
                	System.out.println(plugin.getName()+" switch on");
                } else {  
                	System.out.println(plugin.getName()+" switch off");
                }  
            }  
        });  
	}
	
	
	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		// TODO test
		new Thread(){
			public void run() {
				// TODO dirty
				Log.i(TAG, "updatePluginWithRuleChanged");

				DaoManager.getDaoManager(mContext).updatePluginWithRuleChanged(plugin);
			};
		}.start();
		super.onStop();
	}

}
