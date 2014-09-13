package seu.lab.dolphin.server;

import android.R.raw;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class MotionSensor {
	private static final String TAG = "MotionSensor";
	
	private SensorManager mSensorManager = null;

	private Sensor mSensorLinearAcceleration = null;
	private SensorEventListener mListener = new LinearAccelerationListener();
	float[] accelerometerValues = new float[3];
	private Context mContext;
	private MotionSensorCallback callback;
	
	MotionSensor(Context context, MotionSensorCallback callback){
		this.callback = callback;
		mContext = context;
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mSensorLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	}
	
	void start(){
		if(mSensorLinearAcceleration == null){
			Log.e(TAG, "mSensorLinearAcceleration = null, nothing to do");
			return;
		}
		Log.i(TAG, "start");
		mSensorManager.registerListener(mListener, mSensorLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	void stop(){
		Log.i(TAG, "stop");
		mSensorManager.unregisterListener(mListener);

	}
	class LinearAccelerationListener implements SensorEventListener {
		
		boolean lastMotion = false;
		boolean inMotion = false;
		
		double sum = 0;
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			lastMotion = inMotion;
			
			sum = event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2];
			sum = Math.sqrt(sum);
						
			if(sum > 0.1){
				inMotion = true;
			}else {
				inMotion = false;
			}
			
			if(lastMotion != inMotion)
				callback.onMotionStateChanged(inMotion);
		}
		
		public float round(float a, int n) {
			double i = Math.pow(10, n);
			return (float)((Math.round(a*i))/i);
		}

	}
}

abstract class MotionSensorCallback {
	public abstract void onMotionStateChanged(boolean isInMotion);
}

