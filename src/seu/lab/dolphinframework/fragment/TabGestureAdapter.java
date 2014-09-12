package seu.lab.dolphinframework.fragment;

import java.util.ArrayList;
import java.util.List;

import seu.lab.dolphin.dao.Gesture;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import seu.lab.dolphinframework.R;

class ViewHolder_gesture {
	public TextView gesture_name;
	public LinearLayout gesture_layout;
}

public class TabGestureAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	public List<String> gestures_string = new ArrayList<String>();
	Context context;
	
	public TabGestureAdapter(Context context) {
		super();
		this.context=context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void add(String name){
		this.gestures_string.add(name);
	}

	public void add(List<Gesture> gestures) {
		for(Gesture i : gestures) {
			this.gestures_string.add(i.getName());
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gestures_string.size();
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
	public View getView(int position, View convertView, ViewGroup parent){

		ViewHolder_gesture holder = null;
		if (convertView == null) {
			holder = new ViewHolder_gesture();
			convertView = mInflater.inflate(R.layout.gesture_list_cell, null);
			holder.gesture_name = (TextView) convertView
					.findViewById(R.id.txt_gesture_list_cell_name);
			holder.gesture_layout = (LinearLayout) convertView
					.findViewById(R.id.layout_gesture_list_cell);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder_gesture) convertView.getTag();
		}

		final int pst = position;
		holder.gesture_name.setText(gestures_string.get(pst));
		holder.gesture_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("Click on the layout on gesture list cell------" + gestures_string.get(pst));
				Intent i = new Intent(context, TabGestureDetailActivity.class);
				Bundle data = new Bundle();
				data.putInt("num", pst);
				i.putExtras(data);
				context.startActivity(i);
			}
		});
		
		return convertView;
	}

}
