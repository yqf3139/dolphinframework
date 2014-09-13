package seu.lab.dolphinframework.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import seu.lab.dolphinframework.R;

public class test_activity extends Activity{
	private TextView tvout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		
		tvout=(TextView) findViewById(R.id.tvout);
//		tvout.setText(getIntent().getStringExtra("txt"));
		Bundle data=getIntent().getExtras();
		String txt=data.getString("txt");
		tvout.setText(txt);
		
	}
}
