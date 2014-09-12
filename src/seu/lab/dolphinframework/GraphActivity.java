package seu.lab.dolphinframework;

import java.util.List;

import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.RawGestureData;
import seu.lab.dolphin.server.DaoManager;
import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GraphActivity extends Activity {

	public static final String TAG = "GraphActivity";
	
	Context mContext = this;
	
	Paint paint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.graph_linear_layout);

		paint = new Paint();
		paint.setColor(Color.CYAN);
		paint.setStrokeWidth(4f);
		
		DaoManager manager = DaoManager.getDaoManager(mContext);
		Gesture gesture = manager.getDaoSession().getGestureDao().load((long) GestureEvent.Gestures.PUSH_PULL.ordinal());
		List<RawGestureData> datas = gesture.getRaw_gesuture_data();
        
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels/2;
        
        int step = w_screen/30;

        Log.i(TAG, Color.CYAN+" "+Color.RED);
        int color_range = Color.RED - Color.CYAN;
        
		for (int i = 0; i < 10; i++) {			
			ImageView img = new ImageView(mContext);
			
			Bitmap bitmap = Bitmap.createBitmap(w_screen, 240, Bitmap.Config.ARGB_8888);
			
			double[] data = DaoManager.toDoubleArray(datas.get(datas.size()-i-1).getData());

			Canvas canvas = new Canvas(bitmap);
			
			canvas.drawColor(Color.WHITE);
			
			double max = data[0];
			for (int j = 0; j < data.length; j++) {
				if(max < data[j])max = data[j];
			}
	        Log.i(TAG, "max: "+max);

			for (int j = 0; j < 30; j++) {
				for (int j2 = 0; j2 < 60; j2++) {
					double d = data[j*60+j2] > 0.001 ? data[j*60+j2]/max : 0;
					paint.setColor((int) (d*color_range + Color.CYAN));
					canvas.drawLine(j*step, (59-j2)*4, (j+1)*step, (59-j2)*4, paint);
				}
			}
			canvas.save();
			
			img.setImageBitmap(bitmap);
			
			layout.addView(img);
			layout.addView(new Button(mContext));

		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
	}

}
