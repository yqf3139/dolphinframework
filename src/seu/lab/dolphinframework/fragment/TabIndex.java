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
		
		View newsLayout = inflater.inflate(R.layout.index, container, false);
		drawer = new Drawer();
		sfv = (SurfaceView) newsLayout.findViewById(R.id.SurfaceView01);
		hold = sfv.getHolder();
		hold.addCallback(this);
		
		try {
			FragmentMainActivity.mService.borrowDataReceiver(receiver);
			Log.i(TAG,"borrowed DataReceiver");
		} catch (DolphinException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newsLayout;
	}
	
	@Override
	public void onPause() {
		Log.i(TAG,"onPause");
		try {
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
		int w_screen;
        int h_screen;
        int density;
    	private Paint mainPaint;
    	private Paint extraPaint;
    	
    	Drawer(){
    		DisplayMetrics dm = getResources().getDisplayMetrics();
    		w_screen = dm.widthPixels;
            h_screen = dm.heightPixels;
            density = dm.densityDpi;
    		mainPaint = new Paint();
    		mainPaint.setColor(Color.CYAN);
    		mainPaint.setStrokeWidth(1);
    		mainPaint.setAntiAlias(true);
    		mainPaint.setTextSize(24f);
    		
    		extraPaint = new Paint();
    		extraPaint.setColor(Color.BLUE);
    		extraPaint.setStrokeWidth(20);
    		extraPaint.setAntiAlias(true);
    	}
        
    	private void simpleDraw(double radius, double[] feature, double[] info ) {
    		Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0, 0, w_screen, sfv.getHeight()));
    		if(canvas == null)return;
    		canvas.drawColor(Color.WHITE);
    		//canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.single_dolphin), 100, 100, mainPaint);
    		mainPaint.setAlpha((int) (100*(radius + 1)/4)+50);
    		canvas.drawCircle(w_screen/2, sfv.getHeight()/2, (float) (((radius + 1) * w_screen/6) + w_screen/8), mainPaint);
    		
    		int middle = 100;
    		if (null != feature) {
    			for (int i = 0; i < feature.length; i++) {
    				canvas.drawLine(20 * i + 50, middle, 20 * i + 50, middle
    						- (float) feature[i] * 1000, extraPaint);
    			}
    		}
    		middle = sfv.getHeight();
    		if (null != info) {
    			for (int i = 0; i < info.length; i++) {
    				canvas.drawLine(10 * i - 800, middle, 10 * i - 800, middle
    						- (float) info[i] * 600, extraPaint);
    			}
    		}
    		canvas.save();
    		sfv.getHolder().unlockCanvasAndPost(canvas);
    	}
    	
    	private void drawGreetings(){
    		Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0, 0, w_screen, sfv.getHeight()));
    		canvas.drawColor(Color.WHITE);
    		
    		String greetings = "Dolphin is standing by";
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

