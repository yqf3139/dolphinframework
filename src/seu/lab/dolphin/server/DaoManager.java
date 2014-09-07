package seu.lab.dolphin.server;

import java.util.List;

import android.content.Context;
import android.util.Log;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.dao.DaoMaster;
import seu.lab.dolphin.dao.DaoMaster.OpenHelper;
import seu.lab.dolphin.dao.DaoSession;
import seu.lab.dolphin.dao.DolphinContext;
import seu.lab.dolphin.dao.DolphinContextDao;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.GestureDao;
import seu.lab.dolphin.dao.KeyEvent;
import seu.lab.dolphin.dao.KeyEventDao;
import seu.lab.dolphin.dao.Model;
import seu.lab.dolphin.dao.ModelConfig;
import seu.lab.dolphin.dao.ModelConfigDao;
import seu.lab.dolphin.dao.ModelDao;
import seu.lab.dolphin.dao.PlaybackEvent;
import seu.lab.dolphin.dao.PlaybackEventDao;
import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.dao.PluginDao;
import seu.lab.dolphin.dao.RawGestureDataDao;
import seu.lab.dolphin.dao.Rule;
import seu.lab.dolphin.dao.RuleDao;
import seu.lab.dolphin.dao.SwipeEvent;
import seu.lab.dolphin.dao.SwipeEventDao;
import seu.lab.dolphin.dao.TrainingDataset;
import seu.lab.dolphin.dao.TrainingDatasetDao;
import seu.lab.dolphin.dao.TrainingRelationDao;
import seu.lab.dolphin.sysplugin.EventSenderForKey;

public class DaoManager {
	
	public static final String TAG = "DaoService";
	
	private static OpenHelper helper;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	
	public static void createDB(Context context) {
		Log.i(TAG, "createDB");
		
		daoMaster = getDaoMaster(context);
		daoMaster.dropAllTables(helper.getWritableDatabase(), true);
		daoMaster.createAllTables(helper.getWritableDatabase(), true);
		
		insertDefaultData(context);
	}

	private static void insertDefaultData(Context context) {
		Log.i(TAG, "insertDefaultData");

		daoSession = getDaoSession(context);
		DolphinContextDao dolphinContextDao  = daoSession.getDolphinContextDao();
		GestureDao gestureDao = daoSession.getGestureDao();
		KeyEventDao keyEventDao = daoSession.getKeyEventDao();
		ModelDao modelDao = daoSession.getModelDao();
		ModelConfigDao modelConfigDao = daoSession.getModelConfigDao();
		PlaybackEventDao playbackEventDao = daoSession.getPlaybackEventDao();
		PluginDao pluginDao = daoSession.getPluginDao();
		RawGestureDataDao rawGestureDataDao = daoSession.getRawGestureDataDao();
		RuleDao ruleDao = daoSession.getRuleDao();
		SwipeEventDao swipeEventDao = daoSession.getSwipeEventDao();
		TrainingDatasetDao trainingDatasetDao = daoSession.getTrainingDatasetDao();
		TrainingRelationDao trainingRelationDao = daoSession.getTrainingRelationDao();
		
		KeyEvent[] keyEvents = new KeyEvent[]{
				new KeyEvent(null, "back", EventSenderForKey.KEYCODE_BACK),
				new KeyEvent(null, "call", EventSenderForKey.KEYCODE_CALL),
				new KeyEvent(null, "camera", EventSenderForKey.KEYCODE_CAMERA),
				new KeyEvent(null, "clear", EventSenderForKey.KEYCODE_CLEAR),
				new KeyEvent(null, "dpad center", EventSenderForKey.KEYCODE_DPAD_CENTER),
				new KeyEvent(null, "dpad down", EventSenderForKey.KEYCODE_DPAD_DOWN),
				new KeyEvent(null, "dpad left", EventSenderForKey.KEYCODE_DPAD_LEFT),
				new KeyEvent(null, "dpad right", EventSenderForKey.KEYCODE_DPAD_RIGHT),
				new KeyEvent(null, "dpad up", EventSenderForKey.KEYCODE_DPAD_UP),
				new KeyEvent(null, "end call", EventSenderForKey.KEYCODE_ENDCALL),
				new KeyEvent(null, "enter", EventSenderForKey.KEYCODE_ENTER),
				new KeyEvent(null, "home", EventSenderForKey.KEYCODE_HOME),
				new KeyEvent(null, "menu", EventSenderForKey.KEYCODE_MENU),
				new KeyEvent(null, "notification", EventSenderForKey.KEYCODE_NOTIFICATION),
				new KeyEvent(null, "pound", EventSenderForKey.KEYCODE_POUND),
				new KeyEvent(null, "power", EventSenderForKey.KEYCODE_POWER),
				new KeyEvent(null, "search", EventSenderForKey.KEYCODE_SEARCH),
				new KeyEvent(null, "soft right", EventSenderForKey.KEYCODE_SOFT_RIGHT),
				new KeyEvent(null, "star", EventSenderForKey.KEYCODE_STAR),
				new KeyEvent(null, "volume down", EventSenderForKey.KEYCODE_VOLUME_DOWN),
				new KeyEvent(null, "volume up", EventSenderForKey.KEYCODE_VOLUME_UP)
		};

		SwipeEvent[] swipeEvents = new SwipeEvent[]{
				new SwipeEvent(null, "default swipe up", 100, 800, 100, 0),
				new SwipeEvent(null, "default swipe down", 100, 0, 100, 800),
				new SwipeEvent(null, "default swipe left", 400, 400, 100, 400),
				new SwipeEvent(null, "default swipe right", 100, 400, 400, 400),
		};
		
		PlaybackEvent playbackEvent = new PlaybackEvent(null, "last record script", "last_events");
		
		keyEventDao.insertInTx(keyEvents);
		swipeEventDao.insertInTx(swipeEvents);
		playbackEventDao.insert(playbackEvent);
		
		TrainingDataset defaultTrainingDatasetForNF = new TrainingDataset(null, "default training set for near far", "", 1l);
		long defaultTrainingDatasetForNF_ID = trainingDatasetDao.insert(defaultTrainingDatasetForNF);
		Model defaultModelForNF = new Model(null, "nf_default.dolphin", "default model for near far", defaultTrainingDatasetForNF_ID);
		long defaultModelForNF_ID = modelDao.insert(defaultModelForNF);
		defaultTrainingDatasetForNF.setModel_id(defaultModelForNF_ID);
		
		TrainingDataset defaultTrainingDatasetForFN = new TrainingDataset(null, "default training set for far near", "", 1l);
		long defaultTrainingDatasetForFN_ID = trainingDatasetDao.insert(defaultTrainingDatasetForFN);
		Model defaultModelForFN = new Model(null, "fn_default.dolphin", "default model for far near", defaultTrainingDatasetForFN_ID);
		long defaultModelForFN_ID = modelDao.insert(defaultModelForFN);
		defaultTrainingDatasetForFN.setModel_id(defaultModelForFN_ID);

		TrainingDataset defaultTrainingDatasetForFNFN = new TrainingDataset(null, "default training set for far near", "", 1l);
		long defaultTrainingDatasetForFNFN_ID = trainingDatasetDao.insert(defaultTrainingDatasetForFNFN);
		Model defaultModelForFNFN = new Model(null, "fnfn_default.dolphin", "default model for far near", defaultTrainingDatasetForFNFN_ID);
		long defaultModelForFNFN_ID = modelDao.insert(defaultModelForFNFN);
		defaultTrainingDatasetForFNFN.setModel_id(defaultModelForFNFN_ID);

		TrainingDataset defaultTrainingDatasetForCR = new TrainingDataset(null, "default training set for far near", "", 1l);
		long defaultTrainingDatasetForCR_ID = trainingDatasetDao.insert(defaultTrainingDatasetForCR);
		Model defaultModelForCR = new Model(null, "cr_default.dolphin", "default model for far near", defaultTrainingDatasetForCR_ID);
		long defaultModelForCR_ID = modelDao.insert(defaultModelForCR);
		defaultTrainingDatasetForCR.setModel_id(defaultModelForCR_ID);
		
		Gesture[] gestures = new Gesture[GestureEvent.gesture.length-1];
		for (int i =1; i < gestures.length; i++) {
			gestureDao.insert(new Gesture(null, 0, GestureEvent.gesture[i], 0, "null"));
		}
		
		ModelConfig modelConfig = new ModelConfig(null, "null", "models+=1+2+3+4");
		long defaultModelConfig_ID = modelConfigDao.insert(modelConfig);
		
		Plugin defaultPlugin = new Plugin(null, "default plugin", 1, "", "");
		long defaultPlugin_ID = pluginDao.insert(defaultPlugin);
		
		Plugin duokanPlugin = new Plugin(null, "duokan plugin", 1, "", "");
		long duokanPlugin_ID = pluginDao.insert(duokanPlugin);
		
		Rule[] defaultRules = new Rule[]{
				// default rules
				new Rule(null, "default to left l", "", true, 2, 3l, defaultPlugin_ID, (long)GestureEvent.Gestures.SWIPE_LEFT_L.ordinal()),
				new Rule(null, "default to left p", "", true, 2, 3l, defaultPlugin_ID, (long)GestureEvent.Gestures.SWIPE_LEFT_P.ordinal()),
				new Rule(null, "default to right l", "", true, 2, 4l, defaultPlugin_ID, (long)GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal()),
				new Rule(null, "default to rgiht p", "", true, 2, 4l, defaultPlugin_ID, (long)GestureEvent.Gestures.SWIPE_RIGHT_P.ordinal()),
				new Rule(null, "default to home", "", true, 1, 12l, defaultPlugin_ID, (long)GestureEvent.Gestures.PULL.ordinal()),
				new Rule(null, "default to last record", "", true, 3, 1l, defaultPlugin_ID, (long)GestureEvent.Gestures.PUSH_PULL_PUSH.ordinal()),
				
				// duokan rules
				new Rule(null, "duokan to left", "", true, 2, 3l, duokanPlugin_ID, (long)GestureEvent.Gestures.PULL_PUSH_PULL.ordinal()),
				new Rule(null, "duokan to right", "", true, 2, 4l, duokanPlugin_ID, (long)GestureEvent.Gestures.PUSH_PULL_PUSH.ordinal()),

		};
	
		ruleDao.insertInTx(defaultRules);
		
		DolphinContext[] dolphinContexts = new DolphinContext[]{
				new DolphinContext(null, "*", "still", defaultModelConfig_ID, defaultPlugin_ID),
				new DolphinContext(null, "com.duokan.reader.DkMainActivity", "still", defaultModelConfig_ID, duokanPlugin_ID),

		};
		dolphinContextDao.insertInTx(dolphinContexts);

	}
	
	public static void dropDB(Context context) {
		Log.i(TAG, "dropDB");

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
