package seu.lab.dolphinframework.fragment;

import java.util.ArrayList;
import java.util.List;
import seu.lab.dolphin.dao.Plugin;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import seu.lab.dolphinframework.R;

class ViewHolder_expansion {
	public TextView plug_name;
	public TextView plug_functon;
	public RelativeLayout plug_layout;
	public Switch plug_switcher;
}

public class TabExpansionAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	public List<String> plugs_string = new ArrayList<String>();
	public List<String> plugs_discription = new ArrayList<String>();
	Context context;
	
	public TabExpansionAdapter(Context context) {
		super();
		this.context=context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void add(String name){
		this.plugs_string.add(name);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return plugs_string.size();
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

		ViewHolder_expansion holder = null;
		if (convertView == null) {
			holder = new ViewHolder_expansion();
			convertView = mInflater.inflate(R.layout.expansion_list_cell, null);
			holder.plug_name = (TextView) convertView
					.findViewById(R.id.txt_expansion_list_cell_plug_name);
			holder.plug_functon = (TextView) convertView
					.findViewById(R.id.txt_expansion_list_cell_plug_function);
			holder.plug_layout = (RelativeLayout) convertView
					.findViewById(R.id.layout_expansion_list_cell);
			holder.plug_switcher = (Switch) convertView
					.findViewById(R.id.switch_expansion_list_cell);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder_expansion) convertView.getTag();
		}

		final int pst = position;
		//flexible expansion_detail page according to the click position of expansion_plug_listview
		
		holder.plug_name.setText(plugs_string.get(pst));
		holder.plug_functon.setText(plugs_discription.get(pst));//static
		holder.plug_layout.setOnClickListener(new OnClickListener() {
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
		
		holder.plug_switcher.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                // TODO Auto-generated method stub  
                if (isChecked) {  
                	System.out.println(plugs_string.get(pst)+" switch on");
                } else {  
                	System.out.println(plugs_string.get(pst)+" switch off");
                }  
            }  
        }); 
		
		return convertView;
	}

	public void add(List<Plugin> plugins) {
		
		for(Plugin i : plugins) {
			this.plugs_string.add(i.getName());
			this.plugs_discription.add(i.getDiscription());
		}
		
	}

}
