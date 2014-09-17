package seu.lab.dolphinframework.fragment;

import java.util.ArrayList;
import java.util.List;

import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.server.DaoManager;
import seu.lab.dolphin.server.FloatViewManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import seu.lab.dolphinframework.R;

class ViewHolder_expansion {
	public TextView plug_name;
	public TextView plug_functon;
	public Switch plug_switcher;
	public Button plug_add_one;
	public RelativeLayout plug_layout;
}

public class TabExpansionAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	public List<String> plugs_string = new ArrayList<String>();
	public List<String> plugs_discription = new ArrayList<String>();
	Context context;
	
	public TabExpansionAdapter(Context context) {
		super();
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return plugs_string.size()+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("Expansion Num", Integer.toString(position)+"/"+Integer.toString(getCount()));
		
		if( position == getCount()-1 ) {
			Log.i("add_one", "Enter! ");
			final ViewHolder_expansion holder;
			holder = new ViewHolder_expansion();
			convertView = mInflater.inflate(R.layout.expansion_list_cell, null);
			holder.plug_name = (TextView) convertView.findViewById(R.id.txt_expansion_list_cell_plug_name);
			holder.plug_name.setVisibility(View.GONE);
			holder.plug_functon = (TextView) convertView.findViewById(R.id.txt_expansion_list_cell_plug_function);
			holder.plug_functon.setVisibility(View.GONE);
			holder.plug_switcher = (Switch) convertView.findViewById(R.id.switch_expansion_list_cell);
			holder.plug_switcher.setVisibility(View.GONE);
			holder.plug_add_one = (Button) convertView.findViewById(R.id.txt_expansion_list_cell_add_one);
			holder.plug_add_one.setVisibility(View.VISIBLE);
			holder.plug_layout = (RelativeLayout) convertView.findViewById(R.id.layout_expansion_list_cell);
			
			holder.plug_add_one.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View layout = inflater.inflate(R.layout.dialog_add_new_plugin, (ViewGroup) ((Activity) context).findViewById(R.id.dialog_new_plugin));
					Builder builder = new AlertDialog.Builder(context);
					AlertDialog dialog = builder.create();
					builder.setTitle("请输入").setView(layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							holder.plug_name.setText(((TextView) layout.findViewById(R.id.dialog_new_plugin_name)).getText());
							holder.plug_name.setVisibility(View.VISIBLE);
							holder.plug_functon.setText(((TextView) layout.findViewById(R.id.dialog_new_plugin_description)).getText());
							holder.plug_functon.setVisibility(View.VISIBLE);
							holder.plug_switcher.setVisibility(View.VISIBLE);
							holder.plug_add_one.setVisibility(View.INVISIBLE);
							Plugin plugin = new Plugin(null, holder.plug_name.getText().toString(), -1, holder.plug_functon.getText().toString(), "", 0);
							TabExpansion.plugins.add(plugin);
							long plugin_id = DaoManager.getDaoManager(context).addPlugin(plugin);
							TabExpansionAdapter.this.add(plugin);
							TabExpansionAdapter.this.notifyDataSetChanged();
							Log.i("Changed Num", Integer.toString(TabExpansion.plugins.size()));
							FloatViewManager.getFlowViewManager(context).showFloatViewForActivity(FragmentMainActivity.class, (Activity) context, plugin_id);
							// 弹出浮动窗口，采集Activity
						}
					}).setNegativeButton("取消", null).show();
				}
			});

			return convertView;
		}
		
		ViewHolder_expansion holder = null;
		if (convertView == null || position==getCount()-2 ) {
			holder = new ViewHolder_expansion();
			convertView = mInflater.inflate(R.layout.expansion_list_cell, null);
			holder.plug_name = (TextView) convertView.findViewById(R.id.txt_expansion_list_cell_plug_name);
			holder.plug_functon = (TextView) convertView.findViewById(R.id.txt_expansion_list_cell_plug_function);
			holder.plug_switcher = (Switch) convertView.findViewById(R.id.switch_expansion_list_cell);
			holder.plug_layout = (RelativeLayout) convertView.findViewById(R.id.layout_expansion_list_cell);
			holder.plug_add_one = (Button) convertView.findViewById(R.id.txt_expansion_list_cell_add_one);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder_expansion) convertView.getTag();
		}
		
		final int pst = position;

		holder.plug_name.setText(plugs_string.get(pst));
		holder.plug_functon.setText(plugs_discription.get(pst));
		if(TabExpansion.plugins.get(position).getPlugin_type() == 0) {
			holder.plug_switcher.setChecked(true);
		} else {
			holder.plug_switcher.setChecked(false);
		}
		
		holder.plug_switcher.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1 == true) {
					TabExpansion.plugins.get(pst).setPlugin_type(0);
					DaoManager.getDaoManager(context).updatePlugin(TabExpansion.plugins.get(pst));
				} else {
					TabExpansion.plugins.get(pst).setPlugin_type(1);
					DaoManager.getDaoManager(context).updatePlugin(TabExpansion.plugins.get(pst));
				}
			}
			
		});
		
		holder.plug_layout.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				DaoManager.getDaoManager(context).deletePlugin(TabExpansion.plugins.get(pst));
				TabExpansion.plugins = DaoManager.getDaoManager(context).listAllPlugins();
				TabExpansionAdapter.this.add(TabExpansion.plugins);
				TabExpansionAdapter.this.notifyDataSetChanged();
				return true;
			}
			
		});
		
		if(TabExpansion.plugins.get(pst).getPlugin_type() != -1) {
			
			holder.plug_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//open the detail activity when click on the corresponding layout,deliver the plug name
					Intent i=new Intent(context, TabExpansionDetailActivity.class);
					Bundle data=new Bundle();
					data.putInt("num", pst);
					i.putExtras(data);
					context.startActivity(i);
				}
			});
			
			
		} else {
			holder.plug_switcher.setClickable(false);
		}

		

		
		return convertView;
	}

	public void add(List<Plugin> plugins) {
		this.plugs_string = new ArrayList<String>();
		this.plugs_discription = new ArrayList<String>();
		for(Plugin i : plugins) {
			this.plugs_string.add(i.getName());
			this.plugs_discription.add(i.getDiscription());
		}
		
	}
	
	public void add(Plugin plugin) {
		this.plugs_string.add(plugin.getName());
		this.plugs_discription.add(plugin.getDiscription());
	}

}
