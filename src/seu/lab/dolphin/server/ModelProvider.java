package seu.lab.dolphin.server;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Log.i(TAG, "fetch models Uri");
		Log.i(TAG, "input uri: "+uri.toString());
		Log.i(TAG, "input values: "+values.toString());
		
		Uri ret = Uri.parse("dolphin://+nf_default.dolphin+fn_default.dolphin+nfnf_default.dolphin+cr_default.dolphin");
		return ret;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		Log.e(TAG, "cannot update");
		return 0;
	}

}
