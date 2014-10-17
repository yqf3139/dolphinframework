
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
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import seu.lab.dolphin.client.ContinuousGestureEvent;
import seu.lab.dolphin.client.Dolphin;
import seu.lab.dolphin.client.DolphinException;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.IDolphinStateCallback;
import seu.lab.dolphin.client.IGestureListener;
import seu.lab.dolphin.server.AppPreferences;
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
            R.drawable.freshman_page1_welcome, R.drawable.freshman_page2_close, R.drawable.freshman_page3_away, R.drawable.freshman_page4_clap,
            R.drawable.freshman_page5_up_down_up, R.drawable.freshman_page6_freq, R.drawable.freshman_page7_horizon
    };

    // 底部导航的小点
    private ImageView[] guideDots;

    // 记录当前选中的图片
    private int currentIndex;

    private Button startBtn;
    private Button skipBtn;
    private TextView completeTextView;
    private TextView freqTextView;
    private ProgressBar completeProcessBar;

    public Handler handler = new Handler();
    
    DecimalFormat format = new DecimalFormat("0.00");
    
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
			handler.post(new Runnable() {
				@Override
				public void run() {
					selectPage(1);
				}
			});
		}

		@Override
		public void onNoisy() {

		}

		@Override
		public void onCoreFail() {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(mContext, "Dolphin 被硬件降噪干扰", Toast.LENGTH_SHORT).show();
					
				}
			});
		}

		@Override
		public void onNormal() {
			// TODO Auto-generated method stub
			
		}
    	
    };
    
    IGestureListener listener = new IGestureListener() {
		
    	int next = 0;
    	boolean[] two = new boolean[2];
    	String msg = null;

		@Override
		public void onGesture(GestureEvent event) {
			if(event.isConclusion){
				next = 0;
				msg = null;
				switch (currentIndex) {
				case 0:
					if(event.isFast)
						next = 1;
					break;
				case 1:
					if(event.type == GestureEvent.Gestures.PUSH.ordinal())
						next = 2;
					break;
				case 2:
					if(event.type == GestureEvent.Gestures.PULL.ordinal())
						next = 3;
					break;
				case 3:
					if(event.type == GestureEvent.Gestures.PUSH_PULL.ordinal()
							|| event.type == GestureEvent.Gestures.SWIPE_LEFT_P.ordinal()
							|| event.type == GestureEvent.Gestures.SWIPE_RIGHT_P.ordinal())
						next = 4;
					break;
				case 4:
					if(event.type == GestureEvent.Gestures.PUSH_PULL_PUSH.ordinal()){
						next = 5;
					}
					break;
				case 5:
					msg = "请连续挥动";
					break;
				case 6:
					if(event.type == GestureEvent.Gestures.SWIPE_LEFT_L.ordinal()){
						two[0] = true;
						if(!two[1])msg = "再试试右滑";
						if(two[0] && two[1])next = 7;
					}
					if(event.type == GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal()){
						two[1] = true;
						if(!two[0])msg = "再试试左滑";
						if(two[0] && two[1])next = 7;
					}
//					if(event.type == GestureEvent.Gestures.SWIPE_LEFT_L.ordinal()
//						||event.type == GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal()){
//						next = 7;
//					} 
					break;
				default:
					break;
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(next != 0) selectPage(next);
						if(msg != null)
							Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
		
		@Override
		public void onContinuousGestureUpdate(ContinuousGestureEvent arg0) {
			// TODO Auto-generated method stub
			final String freq = format.format(arg0.freq);
			if(currentIndex == 5)
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						freqTextView.setText("频率："+freq+" Hz");
					}
				});
		}
		
		@Override
		public void onContinuousGestureStart(ContinuousGestureEvent arg0) {
			// TODO Auto-generated method stub
			final String freq = format.format(arg0.freq);
			if(currentIndex == 5)
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						freqTextView.setVisibility(View.VISIBLE);
						freqTextView.setText("频率："+freq+" Hz");
					}
				});

		}
		
		@Override
		public void onContinuousGestureEnd() {
			// TODO Auto-generated method stub
			if(currentIndex == 5)
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						freqTextView.setVisibility(View.GONE);
						selectPage(6);
					}
				});
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
				masks.put(""+GestureEvent.Gestures.PUSH_PULL_PUSH.ordinal(),true);
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
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
            	try {
        			dolphin.stop();
        		} catch (DolphinException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	
            	startBtn.setClickable(false);
            	
            	Toast.makeText(mContext, "请在屏幕上随意滑动一会", Toast.LENGTH_SHORT).show();
            	
            	// 设置屏幕
            	new Thread(){
            		public void run() {
            			EventSettings settings = new EventSettings();
            			ScreenSetter setter = settings.new ScreenSetter(mContext); 
            			setter.set();
                    	
            			handler.post(new Runnable() {
							
							@Override
							public void run() {
		            			// 初始化偏好设置
		            			AppPreferences.init(mContext);
		            			UserPreferences.init(mContext);
		            			
		                        FreshmanActivity.this.finish();
		                        // 我们跳转一个页面
		                        Intent intent = new Intent(FreshmanActivity.this, FragmentMainActivity.class);
		                        startActivity(intent);
							}
						});

            		}
            	}.start();
            }
        });
        skipBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	selectPage(5);
            }
        });
        
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
        
        new InstallThread().start();

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
        freqTextView = (TextView) findViewById(R.id.freq);
        completeProcessBar=(ProgressBar) view.findViewById(R.id.freshman_progressBar);
        
        // 现在用到我们的页面适配器了
        guideViewAdapter = new ViewPagerAdapter(mViews);

        guideViewPager.setAdapter(guideViewAdapter);
        guideViewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return !canTouch;
			}
		});
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

    
    class InstallThread extends Thread{
    	
		boolean err = false;
		String errString = "";
    	
		@Override
		public void run() {
			
			try {
				Installer.installAll(mContext);
			} catch (IOException e1) {
				Log.e(TAG, "安装复制文件出现异常，程序即将退出");
				Log.e(TAG, e1.toString());
				err = true;
				errString = "安装复制文件出现异常，程序即将退出";
			} catch (DolphinException e) {
				Log.e(TAG, "超级权限未获得，程序即将退出");
				Log.e(TAG, e.toString());
				err = true;
				errString = "超级权限未获得，程序即将退出";
			}

			if(err){
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(mContext, errString, Toast.LENGTH_SHORT).show();
						new Thread(){
							public void run() {
								try {
									sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								FreshmanActivity.this.finish();
							}
						}.start();
					}
				});
				return;
			}
			
	    	try {
				dolphin.prepare();
			} catch (DolphinException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			DaoManager daoManager = DaoManager.getDaoManager(mContext);
			daoManager.createDB();
			daoManager.updateAllPlugins();
			
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					completeProcessBar.setVisibility(View.GONE);
					completeTextView.setText("Dolphin初始化完成");
					startBtn.setVisibility(View.VISIBLE);
				}
			});
		}
    }
    
    
}
