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
    private Switch plug_switcher;
    
	private PlugActionAdapter adapter = null;
	
	private Plugin plugin = null;
	
	private Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(TAG, "onCreate");
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expansion_detail);

		plug_name = (TextView) findViewById(R.id.textView_expansion_detail_plug_name);
		plug_instruction = (TextView) findViewById(R.id.textView_expansion_detail_plug_instruction);
		lv_plug_actions = (ListView) findViewById(R.id.listView_plug_actions);
		plug_switcher=(Switch) findViewById(R.id.switch_expansion_detail_plug_manager);
		
		Bundle data = getIntent().getExtras();
//		TabExpansion.plugins = DaoManager.getDaoManager(mContext).listAllPlugins();

		plugin = TabExpansion.plugins.get(data.getInt("num"));
		plugin.refresh();
		plug_name.setText(plugin.getName());
		plug_instruction.setText(plugin.getDiscription());
		
		adapter = new PlugActionAdapter(TabExpansionDetailActivity.this);
		adapter.add(plugin);
		lv_plug_actions.setAdapter(adapter);
		
		
		plug_switcher.setChecked(plugin.getPlugin_type() == 0);
		plug_switcher.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                // TODO Auto-generated method stub  
                if (isChecked) {  
                	plugin.setPlugin_type(0);
                	plugin.update();
                	System.out.println(plugin.getName()+" switch on");
                } else {  
                	plugin.setPlugin_type(1);
                	plugin.update();
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
