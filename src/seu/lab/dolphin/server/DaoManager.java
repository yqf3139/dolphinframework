package seu.lab.dolphin.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.dao.query.CountQuery;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import seu.lab.dolphin.client.GestureEvent;
import seu.lab.dolphin.client.GestureData.RichFeatureData;
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
import seu.lab.dolphin.dao.RawGestureData;
import seu.lab.dolphin.dao.RawGestureDataDao;
import seu.lab.dolphin.dao.RawGestureDataDao.Properties;
import seu.lab.dolphin.dao.Rule;
import seu.lab.dolphin.dao.RuleDao;
import seu.lab.dolphin.dao.SwipeEvent;
import seu.lab.dolphin.dao.SwipeEventDao;
import seu.lab.dolphin.dao.TrainingDataset;
import seu.lab.dolphin.dao.TrainingDatasetDao;
import seu.lab.dolphin.dao.TrainingRelation;
import seu.lab.dolphin.dao.TrainingRelationDao;
import seu.lab.dolphin.learn.DolphinTrainner;
import seu.lab.dolphin.sysplugin.EventSenderForKey;

public class DaoManager implements IDaoManager{
	
	public static final String TAG = "DaoManager";
	private Context mContext;
	
	private static DaoManager daoManager = null;
	
	private OpenHelper helper;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	
	private static QueryBuilder<RawGestureData> rawDataQueryBuilder;
	private static CountQuery<RawGestureData> countQuery;

	private static final int[] featureToGestureID = {
		0,//dummy
		GestureEvent.Gestures.PUSH_PULL.ordinal(),
		GestureEvent.Gestures.SWIPE_LEFT_L.ordinal(),
		GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal(),
		GestureEvent.Gestures.SWIPE_LEFT_P.ordinal(),
		GestureEvent.Gestures.SWIPE_RIGHT_P.ordinal(),

		GestureEvent.Gestures.PULL_PUSH.ordinal(),
		GestureEvent.Gestures.SWING_LEFT_L.ordinal(),
		GestureEvent.Gestures.SWING_RIGHT_L.ordinal(),
		GestureEvent.Gestures.SWING_LEFT_P.ordinal(),
		GestureEvent.Gestures.SWING_RIGHT_P.ordinal(),

		GestureEvent.Gestures.PUSH_PULL_PUSH_PULL.ordinal(),
		GestureEvent.Gestures.SWIPE_BACK_LEFT_L.ordinal(),
		GestureEvent.Gestures.SWIPE_BACK_RIGHT_L.ordinal(),
		GestureEvent.Gestures.SWIPE_BACK_LEFT_P.ordinal(),
		GestureEvent.Gestures.SWIPE_BACK_RIGHT_P.ordinal(),

		GestureEvent.Gestures.CROSSOVER_CLOCKWISE.ordinal(),
		GestureEvent.Gestures.CROSSOVER_ANTICLOCK.ordinal(),

	};
	
	private DaoManager(Context context) {
		mContext = context;
		daoSession = getDaoSession();
		rawDataQueryBuilder = daoSession.getRawGestureDataDao().queryBuilder();
		countQuery = rawDataQueryBuilder.where(Properties.Gesture_id.eq(0)).buildCount();
	}
	
	public static DaoManager getDaoManager(Context context) {
		return daoManager == null ? daoManager = new DaoManager(context) : daoManager;
	}
	
	public void createDB() {
		Log.i(TAG, "createDB");
		
		daoMaster = getDaoMaster();
		DaoMaster.dropAllTables(helper.getWritableDatabase(), true);
		DaoMaster.createAllTables(helper.getWritableDatabase(), true);
		
		try {
			insertDefaultData();
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}

	private void insertDefaultData() throws JSONException {
		Log.i(TAG, "insertDefaultData");

		DolphinContextDao dolphinContextDao = daoSession.getDolphinContextDao();
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
		String outputForNF = DolphinServerVariables.DEFAULT_MODEL_CONFIG.getJSONArray("outputs").getJSONArray(0).toString();
		Model defaultModelForNF = new Model(null, 
				"nf_default.dolphin", 
				outputForNF,
				"default model for near far", 
				defaultTrainingDatasetForNF_ID);
		long defaultModelForNF_ID = modelDao.insert(defaultModelForNF);
		defaultTrainingDatasetForNF.setModel_id(defaultModelForNF_ID);
		defaultTrainingDatasetForNF.update();
		
		TrainingDataset defaultTrainingDatasetForFN = new TrainingDataset(null, "default training set for far near", "", 1l);
		long defaultTrainingDatasetForFN_ID = trainingDatasetDao.insert(defaultTrainingDatasetForFN);
		String outputForFN = DolphinServerVariables.DEFAULT_MODEL_CONFIG.getJSONArray("outputs").getJSONArray(1).toString();
		Model defaultModelForFN = new Model(
				null, 
				"fn_default.dolphin", 
				outputForFN,
				"default model for far near", 
				defaultTrainingDatasetForFN_ID);
		long defaultModelForFN_ID = modelDao.insert(defaultModelForFN);
		defaultTrainingDatasetForFN.setModel_id(defaultModelForFN_ID);
		defaultTrainingDatasetForFN.update();

		TrainingDataset defaultTrainingDatasetForNFNF = new TrainingDataset(null, "default training set for far near far near", "", 1l);
		long defaultTrainingDatasetForNFNF_ID = trainingDatasetDao.insert(defaultTrainingDatasetForNFNF);
		String outputForNFNF = DolphinServerVariables.DEFAULT_MODEL_CONFIG.getJSONArray("outputs").getJSONArray(2).toString();
		Model defaultModelForNFNF = new Model(
				null,
				"nfnf_default.dolphin",
				outputForNFNF,
				"default model for far near",
				defaultTrainingDatasetForNFNF_ID);
		long defaultModelForNFNF_ID = modelDao.insert(defaultModelForNFNF);
		defaultTrainingDatasetForNFNF.setModel_id(defaultModelForNFNF_ID);
		defaultTrainingDatasetForNFNF.update();

		TrainingDataset defaultTrainingDatasetForCR = new TrainingDataset(null, "default training set for far near", "", 1l);
		long defaultTrainingDatasetForCR_ID = trainingDatasetDao.insert(defaultTrainingDatasetForCR);
		String outputForCR = DolphinServerVariables.DEFAULT_MODEL_CONFIG.getJSONArray("outputs").getJSONArray(3).toString();
		Model defaultModelForCR = new Model(
				null, 
				"cr_default.dolphin",
				outputForCR,
				"default model for far near", 
				defaultTrainingDatasetForCR_ID);
		long defaultModelForCR_ID = modelDao.insert(defaultModelForCR);
		defaultTrainingDatasetForCR.setModel_id(defaultModelForCR_ID);
		defaultTrainingDatasetForCR.update();
		
		for (int i = 1; i < GestureEvent.gesture.length; i++) {
			gestureDao.insert(new Gesture(null, 0, GestureEvent.gesture[i], GestureEvent.learnable[i] ? 1 : 0, "null"));
		}
		
		String defaultMasks = DolphinServerVariables.DEFAULT_MODEL_CONFIG.getJSONObject("masks").toString();

		ModelConfig defaultModelConfig = new ModelConfig(null, defaultMasks, "[1,2,3,4]");
		long defaultModelConfig_ID = modelConfigDao.insert(defaultModelConfig);
		
		ModelConfig duokanModelConfig = new ModelConfig(null, "{}", "[]");
		long duokanModelConfig_ID = modelConfigDao.insert(duokanModelConfig);
		
		Plugin defaultPlugin = new Plugin(null, "default plugin", 1, "", "",1l);
		long defaultPlugin_ID = pluginDao.insert(defaultPlugin);
		
		Plugin duokanPlugin = new Plugin(null, "duokan plugin", 1, "", "",1l);
		long duokanPlugin_ID = pluginDao.insert(duokanPlugin);
		
		Rule[] defaultRules = new Rule[]{
				// default rules
				new Rule(null, "default to left l", "", true, 2, 3l, defaultPlugin_ID, (long)GestureEvent.Gestures.SWIPE_LEFT_L.ordinal()),
				new Rule(null, "default to left p", "", true, 2, 3l, defaultPlugin_ID, (long)GestureEvent.Gestures.SWIPE_LEFT_P.ordinal()),
				new Rule(null, "default to right l", "", true, 2, 4l, defaultPlugin_ID, (long)GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal()),
				new Rule(null, "default to rgiht p", "", true, 2, 4l, defaultPlugin_ID, (long)GestureEvent.Gestures.SWIPE_RIGHT_P.ordinal()),
				new Rule(null, "default to home", "", true, 1, 12l, defaultPlugin_ID, (long)GestureEvent.Gestures.PULL.ordinal()),
				new Rule(null, "default to home 2", "", true, 1, 12l, defaultPlugin_ID, (long)GestureEvent.Gestures.PUSH_PULL.ordinal()),
				new Rule(null, "default to last record", "", true, 3, 1l, defaultPlugin_ID, (long)GestureEvent.Gestures.PUSH_PULL_PUSH.ordinal()),
				
				// duokan rules
				new Rule(null, "duokan to left", "", true, 2, 3l, duokanPlugin_ID, (long)GestureEvent.Gestures.PULL_PUSH_PULL.ordinal()),
				new Rule(null, "duokan to right", "", true, 2, 4l, duokanPlugin_ID, (long)GestureEvent.Gestures.PUSH_PULL_PUSH.ordinal()),

		};
	
		ruleDao.insertInTx(defaultRules);
		
		DolphinContext[] dolphinContexts = new DolphinContext[]{
				new DolphinContext(null, "*", "still", defaultModelConfig_ID, defaultPlugin_ID),
				new DolphinContext(null, "com.duokan.reader.DkMainActivity", "still", duokanModelConfig_ID, duokanPlugin_ID),
		};
		dolphinContextDao.insertInTx(dolphinContexts);
		defaultPlugin.setDolphin_context_id(1l);
		defaultPlugin.update();
		duokanPlugin.setDolphin_context_id(2l);
		duokanPlugin.update();
		
		TrainingRelation[] trainingRelations = new TrainingRelation[]{
				// nf
				new TrainingRelation(null, GestureEvent.Gestures.PUSH_PULL.ordinal(), defaultModelForNF_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWIPE_LEFT_L.ordinal(), defaultModelForNF_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWIPE_RIGHT_L.ordinal(), defaultModelForNF_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWING_LEFT_P.ordinal(), defaultModelForNF_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWING_RIGHT_P.ordinal(), defaultModelForNF_ID),

				// fn
				new TrainingRelation(null, GestureEvent.Gestures.PULL_PUSH.ordinal(), defaultModelForFN_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWING_LEFT_L.ordinal(), defaultModelForFN_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWING_RIGHT_L.ordinal(), defaultModelForFN_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWING_LEFT_P.ordinal(), defaultModelForFN_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWING_RIGHT_P.ordinal(), defaultModelForFN_ID),

				// nfnf
				new TrainingRelation(null, GestureEvent.Gestures.PUSH_PULL_PUSH_PULL.ordinal(), defaultModelForNFNF_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWIPE_BACK_LEFT_L.ordinal(), defaultModelForNFNF_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWIPE_BACK_RIGHT_L.ordinal(), defaultModelForNFNF_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWIPE_BACK_LEFT_P.ordinal(), defaultModelForNFNF_ID),
				new TrainingRelation(null, GestureEvent.Gestures.SWIPE_BACK_RIGHT_P.ordinal(), defaultModelForNFNF_ID),

				// cr
				new TrainingRelation(null, GestureEvent.Gestures.CROSSOVER_ANTICLOCK.ordinal(), defaultModelForCR_ID),
				new TrainingRelation(null, GestureEvent.Gestures.CROSSOVER_CLOCKWISE.ordinal(), defaultModelForCR_ID),

		};
		trainingRelationDao.insertInTx(trainingRelations);
		
		try {
			readRawGesturesFromFile(mContext.getAssets().open("default.data"),rawGestureDataDao);
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		} catch (ClassNotFoundException e) {
			Log.e(TAG, e.toString());
		}
		Log.i(TAG, "insertDefaultData end");

	}
	
	private void readRawGesturesFromFile(InputStream inputStream, RawGestureDataDao rawGestureDataDao) throws StreamCorruptedException, IOException, ClassNotFoundException {
		Log.i(TAG, "readRawGesturesFromFile");
		
		ObjectInputStream ois = new ObjectInputStream(inputStream);
		List<RawGestureData> rawGestureDatas = new LinkedList<RawGestureData>();
		
		List<RichFeatureData> richFeatureDatas = (List<RichFeatureData>) ois.readObject();
		
		RichFeatureData data;

		for (int i = 0; i < richFeatureDatas.size(); i++) {
			data = richFeatureDatas.get(i);
			rawGestureDatas.add(new RawGestureData(null, toByteArray(data.data), featureToGestureID[data.feature_id]));
		}
		
		rawGestureDataDao.insertInTx(rawGestureDatas);
		
		ois.close();
		inputStream.close();
		
		Log.i(TAG, "readRawGesturesFromFile end");

	}

	public void dropDB() {
		Log.i(TAG, "dropDB");

		if(daoMaster == null)
			daoMaster = getDaoMaster();
		DaoMaster.dropAllTables(helper.getWritableDatabase(), true);
	}
	
	public DaoMaster getDaoMaster(){
	    if (daoMaster == null){
	        helper = new DaoMaster.DevOpenHelper(mContext, DolphinServerVariables.DATABASE_NAME, null);
	        daoMaster = new DaoMaster(helper.getWritableDatabase());
	    }
	    return daoMaster;
	}
	
	public DaoSession getDaoSession(){
	    if (daoSession == null){
	        if (daoMaster == null){
	            daoMaster = getDaoMaster();
	        }
	        daoSession = daoMaster.newSession();
	    }
	    return daoSession;
	}
	
	public static byte[] toByteArray(double[] doubleArray){
	    int times = Double.SIZE / Byte.SIZE;
	    byte[] bytes = new byte[doubleArray.length * times];
	    for(int i=0;i<doubleArray.length;i++){
	        ByteBuffer.wrap(bytes, i*times, times).putDouble(doubleArray[i]);
	    }
	    return bytes;
	}

	public static double[] toDoubleArray(byte[] byteArray){
	    int times = Double.SIZE / Byte.SIZE;
	    double[] doubles = new double[byteArray.length / times];
	    for(int i=0;i<doubles.length;i++){
	        doubles[i] = ByteBuffer.wrap(byteArray, i*times, times).getDouble();
	    }
	    return doubles;
	}

	public JSONArray getModel_idsFromMasks(JSONObject masks){
		
		boolean[] boolmasks = new boolean[GestureEvent.gesture.length];
		
		for (int i = 1; i < boolmasks.length; i++) {
			if(masks.has(""+i))
				boolmasks[i] = true;
		}
		
		return getModel_idsFromMasks(masks,boolmasks);
	}
	
	public JSONArray getModel_idsFromMasks(boolean[] boolmasks){
		JSONObject masks = new JSONObject();
		try {
			for (int i = 1; i < boolmasks.length; i++) {
				if(boolmasks[i])
						masks.put(""+i, true);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getModel_idsFromMasks(masks,boolmasks);
	}
	
	public JSONArray getModel_idsFromMasks(JSONObject masks, boolean[] boolmasks){
		// TODO
		// split the masks , find the four gesture group
		// for each group
		// if should learn , > 2
		// getSingleModelConfig
		// else put null
	
		// aggregate the ids into one array
		JSONArray modelIds = new JSONArray();
		
		for (int i = 0; i < 4; i++) {
			modelIds.put(getSingleModel_idWrapper(
					i, 
					DolphinServerVariables.GESTURE_GROUP[i][0], 
					DolphinServerVariables.GESTURE_GROUP[i][1],
					DolphinServerVariables.GESTURE_GROUP[i][2], 
					boolmasks));
		}

		return modelIds;
	}
		
	long getSingleModel_idWrapper(int idx, int main, int start, int over, boolean[] boolmasks){
		List<Long> gestureIds = new LinkedList<Long>();
		if(boolmasks[main])
			gestureIds.add((long) main);
		for (int i = start; i <= over; i++) {
			if(boolmasks[i])
				gestureIds.add((long) i);
		}
		if(gestureIds.size() < 2)return 0l;
		
		GestureDao dao = daoSession.getGestureDao();
		Gesture[] gestures = new Gesture[gestureIds.size()];
		for (int i = 0; i < gestures.length; i++) {
			gestures[i] = dao.load(gestureIds.get(i));
		}
		
		return getSingleModel_id(DolphinServerVariables.MODEL_PREFIX[idx], gestures);
		
	}
	
	public long getSingleModel_id(String prefix,Gesture[] gestures){
		
		// if the input gestures set -> model exists
		// sqlite> select s, group_concat(g) from (select traing_data_set_id as s, gesture_id as g from training_relation order by gesture_id) group by s;
		
		String ids = "";
		String modelpath = prefix;

		for (int i = 0; i < gestures.length-1; i++) {
			ids += gestures[i].getId()+",";
			modelpath += gestures[i].getId()+"_";
		}
		ids += gestures[gestures.length-1].getId();
		modelpath += gestures[gestures.length-1].getId()+".dolphin";

		Cursor cursor = daoSession.getTrainingRelationDao().getDatabase().rawQuery(
				"select s from (select traing_data_set_id as s, gesture_id as g from training_relation order by gesture_id) group by s having group_concat(g) = ?",
				new String[]{ids}
		);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			long trainset_id = cursor.getLong(0);
			// return the model id
			return daoSession.getTrainingDatasetDao().load(trainset_id).getModel_id();
		}

		// else generate models and outputs <- gestures
		JSONArray output = null;
		try {
			 output = DolphinTrainner.createModel(modelpath, gestures);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// save the new models file , training relations dao
		TrainingDataset dataset = new TrainingDataset(null, modelpath, "", 0l);
		long dataset_id = daoSession.getTrainingDatasetDao().insert(dataset);

		Model model = new Model(null, modelpath, output.toString(), modelpath, dataset_id);
		long model_id = daoSession.getModelDao().insert(model);
		dataset.setModel_id(model_id);
		dataset.update();
		
		TrainingRelation[] relations = new TrainingRelation[gestures.length];
		for (int i = 0; i < relations.length; i++) {
			relations[i] = new TrainingRelation(null, gestures[i].getId(), dataset_id);
		}
		daoSession.getTrainingRelationDao().insertInTx(relations);
		// return single model id
		return model_id;
	}
	
	public boolean updatePluginWithRuleChanged(Plugin plugin) {
		// get masks
		List<Rule> rules = plugin.getRules();
//		boolean[] masks = new boolean[GestureEvent.gesture.length];
//		for (int i = 0; i < rules.size(); i++) {
//			masks[(int) rules.get(i).getGesture_id()] = true;
//		}
		JSONObject masks = new JSONObject();
		
		try {
			for (int i = 0; i < rules.size(); i++) {
				masks.put(""+rules.get(i).getGesture_id(), true);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ModelConfig config = plugin.getDolphinContext().getModelConfig();
		JSONArray model_ids = getModel_idsFromMasks(masks);
		config.setMasks(masks.toString());
		config.setModel_ids(model_ids.toString());
		daoSession.getModelConfigDao().update(config);
		return true;
	}
	
	public void updateAllPlugins(){
		List<Plugin> plugins = listAllPlugins();
		for (int i = 0; i < plugins.size(); i++) {
			if(plugins.get(i).getDolphin_context_id() == 1l)
				continue;
			updatePluginWithRuleChanged(plugins.get(i));
		}
	}
		
	/*
	 * implements IDaoManager
	 */
	
	@Override
	public List<Gesture> listAllGestures() {
		return daoSession.getGestureDao().loadAll();
	}

	@Override
	public List<Plugin> listAllPlugins() {
		return daoSession.getPluginDao().loadAll();
	}

	@Override
	public List<Gesture> listGesturesAvailble(Plugin plugin) {
		// TODO test
		List<Gesture> allGestures = listAllGestures();
		List<Rule> rules = plugin.getRules();
		List<Gesture> unusedGestures = new LinkedList<Gesture>();
		boolean found;
		for (Gesture gesture : allGestures) {
			found = false;
			for (Rule rule : rules) {
				if(rule.getGesture_id() == gesture.getId()){
					found = true;
					break;
				}
			}
			if(!found)unusedGestures.add(gesture);
		}
		return unusedGestures;
	}

	@Override
	public List<KeyEvent> listAllKeyEvents() {
		return daoSession.getKeyEventDao().loadAll();
	}

	@Override
	public List<SwipeEvent> listAllSwipeEvents() {
		return daoSession.getSwipeEventDao().loadAll();
	}

	@Override
	public List<PlaybackEvent> listPlaybackEventsRecord(Plugin plugin) {
		return daoSession.getPlaybackEventDao().loadAll();
	}

	@Override
	public long addPlaybackEvent(PlaybackEvent playbackEvent) {
		return daoSession.getPlaybackEventDao().insert(playbackEvent);
	}

	@Override
	public long addRule(Rule rule) {
		long id = daoSession.getRuleDao().insert(rule);
		
		updatePluginWithRuleChanged(rule.getPlugin());
		
		return id;
	}

	@Override
	public boolean addDolphinContextAndPlugin(DolphinContext dolphinContext,
			Plugin plugin) {
		long plugin_id = daoSession.getPluginDao().insert(plugin);
		
		dolphinContext.setPlugin_id(plugin_id);
		dolphinContext.setModelConfig(new ModelConfig(null, "", ""));
		long dolphin_context_id = daoSession.getDolphinContextDao().insert(dolphinContext);
		plugin.setDolphin_context_id(dolphin_context_id);
		plugin.update();
		
		// TODO generate model and get id
		updatePluginWithRuleChanged(plugin);
		
		return true;
	}

	@Override
	public boolean deletePlaybackEvent(PlaybackEvent playbackEvent) {
		daoSession.getPlaybackEventDao().delete(playbackEvent);
		return true;
	}

	@Override
	public boolean deleteRule(Rule rule) {
		Plugin plugin = rule.getPlugin();
		rule.delete();
		updatePluginWithRuleChanged(plugin);
		// TODO rule changed, refresh model
		return false;
	}

	@Override
	public boolean deleteDolphinContextAndPlugin(Plugin plugin) {
		// TODO Auto-generated method stub
		// delete rules under a plugin
		PlaybackEventDao playbackEventDao = daoSession.getPlaybackEventDao();
		List<Rule> rules = plugin.getRules();
		for (Rule rule : rules) {
			if(rule.getEvent_type() == 3){
				// delete playback events under a rule
				PlaybackEvent playbackEvent = playbackEventDao.load(rule.getEvent_id());
				File script = new File(DolphinServerVariables.DOLPHIN_HOME+"scripts/"+playbackEvent.getScript_name());
				if(script.exists())script.delete();
				playbackEventDao.delete(playbackEvent);
			}
			// delete rules
			rule.delete();
		}
		// delete the dolphin context
		DolphinContext dolphinContext = plugin.getDolphinContext();
		ModelConfig modelConfig = dolphinContext.getModelConfig();
		
			// delete modelconfig
				// delete models & files
//		String[] modelString = modelConfig.getModel_ids().split(Pattern.quote("+"));
//		for (int i = 1; i < 5; i++) {
//			Model model = daoSession.getModelDao().load(Long.parseLong(modelString[i]));
//			// delete training set & training relations
//			TrainingDataset dataset = model.getTrainingDataset();
//			daoSession.getTrainingRelationDao().deleteInTx(dataset.getTraining_relation());
//			dataset.delete();
//			
//			File modelFile = new File(DolphinServerVariables.DOLPHIN_HOME+"models/"+model.getModel_path());
//			if(modelFile.exists())modelFile.delete();
//			model.delete();
//		}
		
		daoSession.getModelConfigDao().delete(modelConfig);
		dolphinContext.delete();
		
		// delete plugin
		plugin.delete();

		return false;
	}

	@Override
	public boolean updatePlugin(Plugin plugin) {
		plugin.update();
		return true;
	}

	@Override
	public boolean updateRule(Rule rule) {
		rule.update();
		return true;
	}

//	@Override
//	public boolean updateRuleWithGestureChanged(Rule rule) {
//		rule.update();
//		// TODO update models
//
//		// updatePluginWithRuleChanged(rule.getPlugin());
//		
//		return false;
//	}
	
	@Override
	public long addRawGestureData(RawGestureData rawGestureData) {
		return daoSession.getRawGestureDataDao().insert(rawGestureData);
	}

	@Override
	public long countGestureRawData(Gesture gesture) {
		CountQuery<RawGestureData> query = countQuery.forCurrentThread();
		query.setParameter(1, gesture.getId());
		return query.count();
	}
	
	
	@Override
	public void refreshGesture(Gesture gesture){
		// find all the models that uses this gesture, refresh these models
		List<TrainingRelation> relations = gesture.getTraining_relation();
		for (int i = 0; i < relations.size(); i++) {
			refreshModel(relations.get(i).getTrainingDataset().getModel());
		}
	}
	
	@Override
	public void refreshModel(Model model){
		TrainingDataset dataset = model.getTrainingDataset();
		List<TrainingRelation> relations = dataset.getTraining_relation();
		
		// find all the gestures in the model
		Gesture[] gestures = new Gesture[relations.size()];
		for (int i = 0; i < gestures.length; i++) {
			gestures[i] = relations.get(i).getGesture();
		}
		
		try {
			DolphinTrainner.createModel(model.getModel_path(), gestures);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public KeyEvent getKeyEvent(long id) {
		return daoSession.getKeyEventDao().load(id);
	}

	@Override
	public SwipeEvent getSwipeEvent(long id) {
		return daoSession.getSwipeEventDao().load(id);
	}

	@Override
	public PlaybackEvent getPlaybackEvent(long id) {
		return daoSession.getPlaybackEventDao().load(id);
	}

	@Override
	public boolean deleteRawGestureData(RawGestureData rawGestureData) {
		rawGestureData.delete();
		return true;
	}

}
