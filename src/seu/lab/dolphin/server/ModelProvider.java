package seu.lab.dolphin.server;

import seu.lab.dolphin.client.GestureEvent;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class ModelProvider extends ContentProvider{

	static final String TAG = "ModelProvider";

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		Log.e(TAG, "cannot delete");
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		Log.e(TAG, "cannot getType");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.i(TAG, "fetch models Uri");
		Log.i(TAG, "input uri: "+uri.toString());
		Log.i(TAG, "input values: "+values.toString());
		
		// TODO Auto-generate models
		// find models by masks
		
		//if found, stringfy and send back
		
		//if not, train new one or return default

		return Uri.parse(DolphinServerVariables.DEFAULT_MODEL_CONFIG.toString());
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		Log.e(TAG, "cannot query");
		return null;
	}
	
	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		Log.e(TAG, "cannot update");
		return 0;
	}

}
