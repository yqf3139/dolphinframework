package seu.lab.dolphinframework.fragment;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import seu.lab.dolphin.client.DolphinException;
import seu.lab.dolphin.client.IDataReceiver;
import seu.lab.dolphin.client.RealTimeData;
import seu.lab.dolphinframework.R;
import seu.lab.dolphinframework.main.MainActivity;

public class TabIndex extends Fragment implements SurfaceHolder.Callback{
	
	private static String TAG = "TabIndex";
	
	private SurfaceView sfv;
	private SurfaceHolder hold;

	Drawer drawer;
	
	IDataReceiver receiver = new IDataReceiver() {
		
		@Override
		public void onData(RealTimeData data) {
			drawer.simpleDraw(data.radius, data.feature_info, data.normal_info);
		}
		
		@Override
		public JSONObject getDataTypeMask() {
			// TODO Auto-generated method stub
			return null;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.i(TAG,"onCreateView");

		View newsLayout = inflater.inflate(R.layout.index, container, false);
		drawer = new Drawer();
		sfv = (SurfaceView) newsLayout.findViewById(R.id.SurfaceView01);
		hold = sfv.getHolder();
		hold.addCallback(this);
		
		new Thread(){
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(FragmentMainActivity.mService != null){
					try {
						FragmentMainActivity.mService.borrowDataReceiver(receiver);
						Log.i(TAG,"borrowed DataReceiver");

					} catch (DolphinException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		return newsLayout;
	}
	
	@Override
	public void onResume() {
		Log.i(TAG,"onResume");
		
		if(!isHidden()){
			if(FragmentMainActivity.mService != null){
				try {
					FragmentMainActivity.mService.borrowDataReceiver(receiver);
					Log.i(TAG,"borrowed DataReceiver");

				} catch (DolphinException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		super.onResume();
	}
	
	@Override
	public void onPause() {
		Log.i(TAG,"onPause");
		try {
			if(FragmentMainActivity.mService != null)
				FragmentMainActivity.mService.returnDataReceiver();
			Log.i(TAG,"returned DataReceiver");
			drawer.drawGreetings();

		} catch (DolphinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onPause();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		Log.i(TAG, "onHiddenChanged to "+hidden);
				
		if(hidden){
			try {
				FragmentMainActivity.mService.returnDataReceiver();
				Log.i(TAG,"returned DataReceiver");
				drawer.drawGreetings();

			} catch (DolphinException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				FragmentMainActivity.mService.borrowDataReceiver(receiver);
				Log.i(TAG,"borrowed DataReceiver");
			} catch (DolphinException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		super.onHiddenChanged(hidden);
	}
		
	class Drawer{
		float w_screen;
		float h_screen;
        
        float w_feature;		
        float w_info;
        
        int density;
    	private Paint mainPaint;
    	private Paint featurePaint;
		private Paint infoPaint;
    	
    	Drawer(){
    		DisplayMetrics dm = getResources().getDisplayMetrics();
    		w_screen = dm.widthPixels;
            h_screen = dm.heightPixels;
            density = dm.densityDpi;
    		mainPaint = new Paint();
    		mainPaint.setColor(Color.CYAN);
    		mainPaint.setStrokeWidth(1);
    		mainPaint.setAntiAlias(true);
    		
    		w_feature = w_screen/20;
    		w_info = w_screen/100;
    		
    		featurePaint = new Paint();
    		featurePaint.setColor(Color.CYAN);
    		featurePaint.setStrokeWidth(w_feature);
    		featurePaint.setAntiAlias(true);
    		
    		infoPaint = new Paint();
    		infoPaint.setColor(Color.BLUE);
    		infoPaint.setStrokeWidth(w_info);
    		infoPaint.setAntiAlias(true);
    	}
        
    	private void simpleDraw(double radius, double[] feature, double[] info ) {
    		Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0, 0, (int) w_screen, sfv.getHeight()));
    		if(canvas == null)return;
    		canvas.drawColor(Color.WHITE);
    		//canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.single_dolphin), 100, 100, mainPaint);
    		mainPaint.setAlpha((int) (200*(radius + 1)/4)+50);
    		featurePaint.setAlpha((int) (200*(radius + 1)/2)+50);
    		infoPaint.setAlpha((int) (200*(radius + 1)/2)+50);
    		canvas.drawCircle(w_screen/2, sfv.getHeight()/2, (float) (((radius + 1) * w_screen/6) + w_screen/8), mainPaint);
    		
    		int middle = 0;
    		if (null != feature) {
    			for (int i = 0; i < feature.length; i++) {
    				canvas.drawLine(w_feature * i, middle, w_feature * i, middle
    						- (float) feature[i] * 3000 * density/240, featurePaint);
    			}
    		}
    		middle = sfv.getHeight();
    		if (null != info) {
    			for (int i = 50; i < info.length-50; i++) {
    				canvas.drawLine(w_info * (i-50), middle, w_info * (i-50), middle
    						- (float)info[i] * 1500 * density /240, infoPaint);
    			}
    		}
    		canvas.save();
    		sfv.getHolder().unlockCanvasAndPost(canvas);
    	}
    	
    	private void drawGreetings(){
    		Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0, 0, (int) w_screen, sfv.getHeight()));
    		if(canvas == null)return;
    		canvas.drawColor(Color.WHITE);
    		mainPaint.setTextSize((float)w_screen/20);
    		mainPaint.setAlpha(255);

    		String greetings = "Dolphin 准备就绪";
    		canvas.drawText(greetings, 0, greetings.length(), w_screen/4, sfv.getHeight()/4, mainPaint);
    		
    		canvas.save();
    		sfv.getHolder().unlockCanvasAndPost(canvas);
    	}
    	
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawer.drawGreetings();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}

