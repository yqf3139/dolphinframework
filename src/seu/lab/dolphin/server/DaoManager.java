package seu.lab.dolphin.server;

import java.util.List;

import android.content.Context;
import android.util.Log;
import seu.lab.dolphin.dao.DaoMaster;
import seu.lab.dolphin.dao.DaoMaster.OpenHelper;
import seu.lab.dolphin.dao.DaoSession;
import seu.lab.dolphin.dao.DolphinContext;
import seu.lab.dolphin.dao.DolphinContextDao;
import seu.lab.dolphin.dao.GestureDao;
import seu.lab.dolphin.dao.KeyEvent;
import seu.lab.dolphin.dao.KeyEventDao;
import seu.lab.dolphin.dao.ModelDao;
import seu.lab.dolphin.dao.PlaybackEventDao;
import seu.lab.dolphin.dao.PluginDao;
import seu.lab.dolphin.dao.RawGestureDataDao;
import seu.lab.dolphin.dao.RuleDao;
import seu.lab.dolphin.dao.SwipeEventDao;
import seu.lab.dolphin.dao.TrainingDatasetDao;
import seu.lab.dolphin.dao.TrainingRelationDao;

public class DaoManager {
	
	public static final String TAG = "DaoService";
	
	private static OpenHelper helper;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	
	public static void createDB(Context context) {
		daoMaster = getDaoMaster(context);
		daoMaster.createAllTables(helper.getWritableDatabase(), true);
		
		insertDefaultData(context);
	}

	public static void insertDefaultData(Context context) {
		daoSession = getDaoSession(context);
		DolphinContextDao dolphinContextDao  = daoSession.getDolphinContextDao();
		GestureDao gestureDao = daoSession.getGestureDao();
		KeyEventDao keyEventDao = daoSession.getKeyEventDao();
		ModelDao modelDao = daoSession.getModelDao();
		PlaybackEventDao playbackEventDao = daoSession.getPlaybackEventDao();
		PluginDao pluginDao = daoSession.getPluginDao();
		RawGestureDataDao rawGestureDataDao = daoSession.getRawGestureDataDao();
		RuleDao ruleDao = daoSession.getRuleDao();
		SwipeEventDao swipeEventDao = daoSession.getSwipeEventDao();
		TrainingDatasetDao trainingDatasetDao = daoSession.getTrainingDatasetDao();
		TrainingRelationDao trainingRelationDao = daoSession.getTrainingRelationDao();
		
		KeyEvent[] keyEvents = new KeyEvent[]{
				new KeyEvent(null, name, keycode),
		};
		/*
		 * Dolphin Context
		 */
		
		
//		dolphinContextDao.insert(new DolphinContext(null, "com.example.app1", "still"));
//		dolphinContextDao.insert(new DolphinContext(null, "com.example.app2", "moving"));
		
//		Log.e(TAG, ""+dolphinContextDao.count());
//		List<DolphinContext> dolphinContext = dolphinContextDao.loadAll();
//		for (int i = 0; i < dolphinContext.size(); i++) {
//			DolphinContext tmp = dolphinContext.get(i);
//			Log.e(TAG, tmp.getId()+":"+tmp.getActivity_name());
//		}
	}
	
	public static void dropDB(Context context) {
		if(daoMaster == null)
			daoMaster = getDaoMaster(context);
		daoMaster.dropAllTables(helper.getWritableDatabase(), true);
	}
	
	public static DaoMaster getDaoMaster(Context context){
	    if (daoMaster == null){
	        helper = new DaoMaster.DevOpenHelper(context, DolphinServerVariables.DATABASE_NAME, null);
	        daoMaster = new DaoMaster(helper.getWritableDatabase());
	    }
	    return daoMaster;
	}
	public static DaoSession getDaoSession(Context context){
	    if (daoSession == null){
	        if (daoMaster == null){
	            daoMaster = getDaoMaster(context);
	        }
	        daoSession = daoMaster.newSession();
	    }
	    return daoSession;
	}
}
