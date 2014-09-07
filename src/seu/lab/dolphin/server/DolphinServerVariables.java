package seu.lab.dolphin.server;

import android.net.Uri;

public class DolphinServerVariables {
	public static final String DOLPHIN_HOME = "/storage/sdcard0/dolphin_home/";
	public static final String REMOTE_SERVICE_NAME = "seu.lab.dolphin.server.REMOTE";
	public static final String DATABASE_NAME = "dolphin_db";
	public static final String BROADCAST_CLIENT_NAME = "seu.lab.dolphin.server.BROADCAST_CLIENT";
	public static final String MODEL_PROVIDE_NAME = "seu.lab.dolphin.server.MODEL_PROVIDER";
	public static final int DOLPHIN_CONTEXT_FRESH_INTERVAL = 2;
	public static final Uri defaultModelUri = Uri.parse("dolphinmodels://+nf_default.dolphin+fn_default.dolphin+nfnf_default.dolphin+cr_default.dolphin");

}
