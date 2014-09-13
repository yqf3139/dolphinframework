package seu.lab.dolphinframework.fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.KeyEvent;
import seu.lab.dolphin.dao.PlaybackEvent;
import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.dao.Rule;
import seu.lab.dolphin.dao.SwipeEvent;
import seu.lab.dolphin.server.DaoManager;
import seu.lab.dolphinframework.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

class ViewHolder_plug_action {
	public Button plug_action_gesture;
	public Button plug_action_function;
}

public class PlugActionAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater = null;
	Context context;
	
	private List<Rule> rules = null;
	private Plugin plugin;
	
	public PlugActionAdapter(Context context) {
		super();
		this.context=context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void add(Plugin plugin) {
		rules = plugin.getRules();
		this.plugin = plugin;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rules.size();
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
			
		ViewHolder_plug_action holder = null;
		if (convertView == null) {
			holder = new ViewHolder_plug_action();
			convertView = mInflater.inflate(R.layout.expansion_detail_action_list_cell, null);
			holder.plug_action_gesture = (Button) convertView.findViewById(R.id.button_plug_action_gesture);
			holder.plug_action_function = (Button) convertView.findViewById(R.id.button_plug_action_function);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder_plug_action) convertView.getTag();
		}

		final Rule rule = rules.get(position);
		
		holder.plug_action_gesture.setText(rule.getGesture().getName());
		switch(rules.get(position).getEvent_type()) {
		case 1: 
			holder.plug_action_function.setText(DaoManager.getDaoManager(context).getKeyEvent(rule.getEvent_id()).getName());
			break;
		case 2:
			holder.plug_action_function.setText(DaoManager.getDaoManager(context).getSwipeEvent(rule.getEvent_id()).getName());
			break;
		case 3:
			holder.plug_action_function.setText(DaoManager.getDaoManager(context).getPlaybackEvent(rule.getEvent_id()).getName());
			break;
		}
		
		final View cV = convertView;
		holder.plug_action_gesture.setOnClickListener(new OnClickListener() {
			
			private List<Gesture> gestures;
			private ArrayList<String> gestures_name;

			@Override
			public void onClick(View v) {
				gestures = DaoManager.getDaoManager(context).listGesturesAvailble(plugin);
				gestures_name = new ArrayList<String>();
				for(Gesture i : gestures) {
					gestures_name.add(i.getName());
				}
				Builder builder = new AlertDialog.Builder(context);
				final AlertDialog dialog = builder.create();
				builder.setTitle("请选择手势").setItems((String[]) gestures_name.toArray(new String[gestures_name.size()]), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if(!rule.getGesture().equals(gestures.get(arg1))) {
							Toast.makeText(context, "Changed", Toast.LENGTH_SHORT).show();
							((Button) cV.findViewById(R.id.button_plug_action_gesture)).setText(gestures_name.get(arg1));
							
							// TODO 修改手势-同步到数据库
							rule.setGesture(gestures.get(arg1));
							DaoManager.getDaoManager(context).updateRule(rule);
							
						}
						dialog.dismiss();
					}
				}).setNegativeButton("取消", null).show();
				
			}
		});
		
		holder.plug_action_function.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Builder builder = new AlertDialog.Builder(context);
				final AlertDialog dialog = builder.create();
				builder.setTitle("请选择事件").setItems(new String[] { "按键事件", "两点滑动事件", "录制事件" }, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						dialog.dismiss();
						Builder builder2 = new AlertDialog.Builder(context);
						AlertDialog dialog2 = builder2.create();
						builder2.setTitle("请选择");
						switch(arg1) {
						case 0:
							final List<KeyEvent> keyevents = DaoManager.getDaoManager(context).listAllKeyEvents();
							final List<String> keyevent_names = new ArrayList<String>();
							for (KeyEvent i : keyevents) {
								keyevent_names.add(i.getName());
							}
							builder2.setItems(((String[]) keyevent_names.toArray(new String[keyevent_names.size()])), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									Log.i("Click", DaoManager.getDaoManager(context).getSwipeEvent(rule.getEvent_id()).getName());
									Log.i("Click", keyevents.get(arg1).getName());
									if(!DaoManager.getDaoManager(context).getKeyEvent(rule.getEvent_id()).equals(keyevents.get(arg1))) {
										// Toast.makeText(context, "Changed", Toast.LENGTH_SHORT).show();
										Log.i("Click", "Changed");
										((Button) cV.findViewById(R.id.button_plug_action_function)).setText(keyevent_names.get(arg1));
										
										// TODO 修改按键-同步到数据库
										rule.setEvent_type(1);
										rule.setEvent_id(keyevents.get(arg1).getId());
										DaoManager.getDaoManager(context).updateRule(rule);
									}
								}
							}).setNegativeButton("取消", null).show();
							break;
						case 1:
							final List<SwipeEvent> swipeevents = DaoManager.getDaoManager(context).listAllSwipeEvents();
							final List<String> swipeevent_names = new ArrayList<String>();
							for (SwipeEvent i : swipeevents) {
								swipeevent_names.add(i.getName());
							}
							builder2.setItems(((String[]) swipeevent_names.toArray(new String[swipeevent_names.size()])), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									Log.i("Click", DaoManager.getDaoManager(context).getSwipeEvent(rule.getEvent_id()).getName());
									Log.i("Click", swipeevents.get(arg1).getName());
									if(!DaoManager.getDaoManager(context).getSwipeEvent(rule.getEvent_id()).equals(swipeevents.get(arg1))) {
										Log.i("Click", "Changed");
										((Button) cV.findViewById(R.id.button_plug_action_function)).setText(swipeevent_names.get(arg1));
										// TODO 修改滑动-同步到数据库
										rule.setEvent_type(2);
										rule.setEvent_id(swipeevents.get(arg1).getId());
										DaoManager.getDaoManager(context).updateRule(rule);
									}
								}
							}).setNegativeButton("取消", null).show();
							break;
						case 2:
							final List<PlaybackEvent> pbevents = DaoManager.getDaoManager(context).listPlaybackEventsRecord(plugin);
							final List<String> pbevent_names = new ArrayList<String>();
							for (PlaybackEvent i : pbevents) {
								pbevent_names.add(i.getName());
							}
							builder2.setItems(((String[]) pbevent_names.toArray(new String[pbevent_names.size()])), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									Log.i("Click", DaoManager.getDaoManager(context).getSwipeEvent(rule.getEvent_id()).getName());
									Log.i("Click", pbevents.get(arg1).getName());
									if(!DaoManager.getDaoManager(context).getPlaybackEvent(rule.getEvent_id()).equals(pbevents.get(arg1))) {
										Log.i("Click", "Changed");
										((Button) cV.findViewById(R.id.button_plug_action_function)).setText(pbevent_names.get(arg1));
										// TODO 修改录制-同步到数据库
										rule.setEvent_type(3);
										rule.setEvent_id(pbevents.get(arg1).getId());
										DaoManager.getDaoManager(context).updateRule(rule);
									}
								}
							}).setNegativeButton("取消", null).show();
							break;
						}
						
					}
				}).setNegativeButton("取消", null).show();
			}
		});
		return convertView;
	}
}

class RuleToChange {
	Long rule_id;
	Long gesture_id;
	Integer event_type;
	Long Event_id;
}
