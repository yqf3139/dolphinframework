
package seu.lab.dolphinframework.main;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import seu.lab.dolphin.client.ContinuousGestureEvent;
import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.DolphinException;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.IDolphinStateCallback;
import seu.lab.dolphin.client.IGestureListener;
import seu.lab.dolphin.server.DaoManager;
import seu.lab.dolphin.server.IMotionSensorCallback;
import seu.lab.dolphin.server.MotionSensor;
import seu.lab.dolphin.server.UserPreferences;
import seu.lab.dolphin.sysplugin.EventSettings;
import seu.lab.dolphin.sysplugin.Installer;
import seu.lab.dolphin.sysplugin.EventSettings.ScreenSetter;
import seu.lab.dolphinframework.R;
import seu.lab.dolphinframework.fragment.FragmentMainActivity;


public class FreshmanActivity extends Activity {

	static final String TAG = "FreshmanActivity";
	
	boolean canTouch = false;
	boolean inMotion = false;
	boolean coreReady = false;

    // 显示导航页面的viewpager
    private ViewPager guideViewPager;

    // 页面适配器
    private ViewPagerAdapter guideViewAdapter;

    // 页面图片列表
    private ArrayList<View> mViews;

    // 图片资源，这里我们放入了5张图片，因为第6张图片，我们已经在freshman_content_view.xml中加载好了
    private final int images[] = {
            R.drawable.freshman_page1_welcome, R.drawable.freshman_page2_close, R.drawable.freshman_page3_away, R.drawable.freshman_page4_clap, R.drawable.freshman_page5_horizon
    };

    // 底部导航的小点
    private ImageView[] guideDots;

    // 记录当前选中的图片
    private int currentIndex;

    private Button startBtn;
    private Button skipBtn;
    private TextView completeTextView;
    private ProgressBar completeProcessBar;

    public Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		Toast.makeText(mContext, "install complete", Toast.LENGTH_SHORT).show();
			completeProcessBar.setVisibility(View.GONE);
			completeTextView.setText("Dolphin初始化完成");
			startBtn.setVisibility(View.VISIBLE);
    	}
    };
    
    Context mContext = this;
    
    Dolphin dolphin;

    IDolphinStateCallback callback = new IDolphinStateCallback(){

		@Override
		public void onCoreReady() {
			try {
				dolphin.start();
			} catch (DolphinException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			canTouch = true;
			new Handler(mContext.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					selectPage(1);
				}
			});
		}

		@Override
		public void onNoisy() {
			// TODO Auto-generated method stub
			
		}
    	
    };
    
    IGestureListener listener = new IGestureListener() {
		
		@Override
		public void onGesture(GestureEvent event) {
			if(event.isConclusion){
				if(currentIndex < 5){
					new Handler(mContext.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							selectPage(currentIndex+1);
						}
					});
				}
			}
		}
		
		@Override
		public void onContinuousGestureUpdate(ContinuousGestureEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onContinuousGestureStart(ContinuousGestureEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onContinuousGestureEnd() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public JSONObject getGestureConfig() {
			
			JSONObject config = new JSONObject();
			JSONObject masks = new JSONObject();
			
			try {
				masks.put(""+GestureEvent.Gestures.PUSH.ordinal(),true);
				masks.put(""+GestureEvent.Gestures.PULL.ordinal(),true);
				masks.put(""+GestureEvent.Gestures.SWIPE_LEFT_L.ordinal(),true);
				masks.put(""+GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal(),true);
				masks.put(""+GestureEvent.Gestures.PUSH_PULL.ordinal(),true);
				config.put("masks", masks);
			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			}
			
			return config;
		}
	};
    
	void selectPage(int idx){
		guideViewPager.setCurrentItem(idx, true);
        try {
			setCurrentDot(idx);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      //last page processbar
        if (idx == mViews.size()-1) {
        	skipBtn.setVisibility(View.GONE);
        	
		}
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freshman_view_main);

        initView();

        initDot();
        
        // 添加页面更换监听事件，来更新导航小点的状态。
        guideViewPager.setOnPageChangeListener(new OnPageChangeListener() {
        	
            @Override
            public void onPageSelected(int arg0) {
                try {
					setCurrentDot(arg0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              //last page processbar
                if (arg0==mViews.size()-1) {
                	skipBtn.setVisibility(View.GONE);
                	
        		}
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        // 开始按钮的点击事件监听
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 我们随便跳转一个页面
                Intent intent = new Intent(FreshmanActivity.this, FragmentMainActivity.class);
                startActivity(intent);
                FreshmanActivity.this.finish();
            }
        });
        skipBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreshmanActivity.this, FragmentMainActivity.class);
                startActivity(intent);
                FreshmanActivity.this.finish();
            }
        });
        
        new InstallThread().start();
                
        try {
			dolphin = Dolphin.getInstance(
					(AudioManager) getSystemService(Context.AUDIO_SERVICE), 
					getContentResolver(), 
					callback, null, listener);
		} catch (DolphinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // 初始化页面
    private void initView() {
        guideViewPager = (ViewPager)findViewById(R.id.freshman_view_pager);
        mViews = new ArrayList<View>();

        for (int i = 0; i < images.length; i++) {
            // 新建一个ImageView容器来放置我们的图片。
            ImageView iv = new ImageView(FreshmanActivity.this);
            iv.setBackgroundResource(images[i]);

            // 将容器添加到图片列表中
            mViews.add(iv);
        }

        // 上面添加了5张图片了，还有一张放在freshman_content_view.xml中，我们把这个页面也添加进来。
        View view = LayoutInflater.from(FreshmanActivity.this).inflate(R.layout.freshman_finish_view,
                null);
        mViews.add(view);

        // 现在为我们的开始按钮找到对应的控件
        startBtn = (Button) view.findViewById(R.id.freshman_start_btn);
        skipBtn=(Button) findViewById(R.id.freshman_button_skip);
        completeTextView=(TextView) view.findViewById(R.id.freshman_textView_complete);
        completeProcessBar=(ProgressBar) view.findViewById(R.id.freshman_progressBar);
        // 现在用到我们的页面适配器了
        guideViewAdapter = new ViewPagerAdapter(mViews);

        guideViewPager.setAdapter(guideViewAdapter);
    }

    // 初始化导航小点
    private void initDot() {
        // 找到放置小点的布局
        LinearLayout layout = (LinearLayout)findViewById(R.id.freshman_layout_dots);

        // 初始化小点数组
        guideDots = new ImageView[mViews.size()];

        // 循环取得小点图片，让每个小点都处于正常状态
        for (int i = 0; i < mViews.size(); i++) {
            guideDots[i] = (ImageView)layout.getChildAt(i);
            guideDots[i].setSelected(false);
        }

        // 初始化第一个小点为选中状态
        currentIndex = 0;
        guideDots[currentIndex].setSelected(true);
    }

    // 页面更换时，更新小点状态
    private void setCurrentDot(int position) throws InterruptedException {
        if (position < 0 || position > mViews.size() - 1 || currentIndex == position) {
            return;
        }

        guideDots[position].setSelected(true);
        guideDots[currentIndex].setSelected(false);

        currentIndex = position;
    }

    @Override
    protected void onStart() {
    	try {
			dolphin.prepare();
		} catch (DolphinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	super.onStart();
    }
    
    @Override
    protected void onStop() {
    	try {
			dolphin.stop();
		} catch (DolphinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	super.onStop();
    }
    
//    @Override
//    public boolean onTouchEvent(MotionEvent arg0) {
//    	Log.e("onTouchEvent", ""+canTouch);
//      if(canTouch)
//    	  return super.onTouchEvent(arg0);
//      else return false;
//    }
    
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//    	Log.e("dispatchTouchEvent", ""+canTouch);
//
//    	if(canTouch)
//    		return super.dispatchGenericMotionEvent(ev);
//    	else return false;
//    }
    
    class InstallThread extends Thread{
		@Override
		public void run() {	
			EventSettings settings = new EventSettings();
			ScreenSetter setter = settings.new ScreenSetter(mContext); 
			setter.start();
			Installer.installAll(mContext);
			DaoManager daoManager = DaoManager.getDaoManager(mContext);
			daoManager.createDB();
			daoManager.updateAllPlugins();
			//AppPreferences.init(mContext);
			UserPreferences.init(mContext);
			
			handler.obtainMessage().sendToTarget();
		}
    }
    
    
}
