package seu.lab.dolphinframework.main;

import java.io.IOException;

import seu.lab.dolphin.server.DaoManager;
import seu.lab.dolphinframework.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class InfoActivity extends Activity {

	Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		new Thread(){
			public void run() {
				DaoManager manager = DaoManager.getDaoManager(getApplicationContext());
				try {
					manager.dumpGestures();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}.start();

	}
	
}
