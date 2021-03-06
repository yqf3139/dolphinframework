package seu.lab.dolphin.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import seu.lab.dolphin.dao.DolphinContext;
import seu.lab.dolphin.dao.Model;
import seu.lab.dolphin.dao.ModelConfig;
import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.Rule;
import seu.lab.dolphin.dao.RawGestureData;
import seu.lab.dolphin.dao.TrainingDataset;
import seu.lab.dolphin.dao.TrainingRelation;
import seu.lab.dolphin.dao.KeyEvent;
import seu.lab.dolphin.dao.SwipeEvent;
import seu.lab.dolphin.dao.PlaybackEvent;

import seu.lab.dolphin.dao.DolphinContextDao;
import seu.lab.dolphin.dao.ModelDao;
import seu.lab.dolphin.dao.ModelConfigDao;
import seu.lab.dolphin.dao.PluginDao;
import seu.lab.dolphin.dao.GestureDao;
import seu.lab.dolphin.dao.RuleDao;
import seu.lab.dolphin.dao.RawGestureDataDao;
import seu.lab.dolphin.dao.TrainingDatasetDao;
import seu.lab.dolphin.dao.TrainingRelationDao;
import seu.lab.dolphin.dao.KeyEventDao;
import seu.lab.dolphin.dao.SwipeEventDao;
import seu.lab.dolphin.dao.PlaybackEventDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dolphinContextDaoConfig;
    private final DaoConfig modelDaoConfig;
    private final DaoConfig modelConfigDaoConfig;
    private final DaoConfig pluginDaoConfig;
    private final DaoConfig gestureDaoConfig;
    private final DaoConfig ruleDaoConfig;
    private final DaoConfig rawGestureDataDaoConfig;
    private final DaoConfig trainingDatasetDaoConfig;
    private final DaoConfig trainingRelationDaoConfig;
    private final DaoConfig keyEventDaoConfig;
    private final DaoConfig swipeEventDaoConfig;
    private final DaoConfig playbackEventDaoConfig;

    private final DolphinContextDao dolphinContextDao;
    private final ModelDao modelDao;
    private final ModelConfigDao modelConfigDao;
    private final PluginDao pluginDao;
    private final GestureDao gestureDao;
    private final RuleDao ruleDao;
    private final RawGestureDataDao rawGestureDataDao;
    private final TrainingDatasetDao trainingDatasetDao;
    private final TrainingRelationDao trainingRelationDao;
    private final KeyEventDao keyEventDao;
    private final SwipeEventDao swipeEventDao;
    private final PlaybackEventDao playbackEventDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        dolphinContextDaoConfig = daoConfigMap.get(DolphinContextDao.class).clone();
        dolphinContextDaoConfig.initIdentityScope(type);

        modelDaoConfig = daoConfigMap.get(ModelDao.class).clone();
        modelDaoConfig.initIdentityScope(type);

        modelConfigDaoConfig = daoConfigMap.get(ModelConfigDao.class).clone();
        modelConfigDaoConfig.initIdentityScope(type);

        pluginDaoConfig = daoConfigMap.get(PluginDao.class).clone();
        pluginDaoConfig.initIdentityScope(type);

        gestureDaoConfig = daoConfigMap.get(GestureDao.class).clone();
        gestureDaoConfig.initIdentityScope(type);

        ruleDaoConfig = daoConfigMap.get(RuleDao.class).clone();
        ruleDaoConfig.initIdentityScope(type);

        rawGestureDataDaoConfig = daoConfigMap.get(RawGestureDataDao.class).clone();
        rawGestureDataDaoConfig.initIdentityScope(type);

        trainingDatasetDaoConfig = daoConfigMap.get(TrainingDatasetDao.class).clone();
        trainingDatasetDaoConfig.initIdentityScope(type);

        trainingRelationDaoConfig = daoConfigMap.get(TrainingRelationDao.class).clone();
        trainingRelationDaoConfig.initIdentityScope(type);

        keyEventDaoConfig = daoConfigMap.get(KeyEventDao.class).clone();
        keyEventDaoConfig.initIdentityScope(type);

        swipeEventDaoConfig = daoConfigMap.get(SwipeEventDao.class).clone();
        swipeEventDaoConfig.initIdentityScope(type);

        playbackEventDaoConfig = daoConfigMap.get(PlaybackEventDao.class).clone();
        playbackEventDaoConfig.initIdentityScope(type);

        dolphinContextDao = new DolphinContextDao(dolphinContextDaoConfig, this);
        modelDao = new ModelDao(modelDaoConfig, this);
        modelConfigDao = new ModelConfigDao(modelConfigDaoConfig, this);
        pluginDao = new PluginDao(pluginDaoConfig, this);
        gestureDao = new GestureDao(gestureDaoConfig, this);
        ruleDao = new RuleDao(ruleDaoConfig, this);
        rawGestureDataDao = new RawGestureDataDao(rawGestureDataDaoConfig, this);
        trainingDatasetDao = new TrainingDatasetDao(trainingDatasetDaoConfig, this);
        trainingRelationDao = new TrainingRelationDao(trainingRelationDaoConfig, this);
        keyEventDao = new KeyEventDao(keyEventDaoConfig, this);
        swipeEventDao = new SwipeEventDao(swipeEventDaoConfig, this);
        playbackEventDao = new PlaybackEventDao(playbackEventDaoConfig, this);

        registerDao(DolphinContext.class, dolphinContextDao);
        registerDao(Model.class, modelDao);
        registerDao(ModelConfig.class, modelConfigDao);
        registerDao(Plugin.class, pluginDao);
        registerDao(Gesture.class, gestureDao);
        registerDao(Rule.class, ruleDao);
        registerDao(RawGestureData.class, rawGestureDataDao);
        registerDao(TrainingDataset.class, trainingDatasetDao);
        registerDao(TrainingRelation.class, trainingRelationDao);
        registerDao(KeyEvent.class, keyEventDao);
        registerDao(SwipeEvent.class, swipeEventDao);
        registerDao(PlaybackEvent.class, playbackEventDao);
    }
    
    public void clear() {
        dolphinContextDaoConfig.getIdentityScope().clear();
        modelDaoConfig.getIdentityScope().clear();
        modelConfigDaoConfig.getIdentityScope().clear();
        pluginDaoConfig.getIdentityScope().clear();
        gestureDaoConfig.getIdentityScope().clear();
        ruleDaoConfig.getIdentityScope().clear();
        rawGestureDataDaoConfig.getIdentityScope().clear();
        trainingDatasetDaoConfig.getIdentityScope().clear();
        trainingRelationDaoConfig.getIdentityScope().clear();
        keyEventDaoConfig.getIdentityScope().clear();
        swipeEventDaoConfig.getIdentityScope().clear();
        playbackEventDaoConfig.getIdentityScope().clear();
    }

    public DolphinContextDao getDolphinContextDao() {
        return dolphinContextDao;
    }

    public ModelDao getModelDao() {
        return modelDao;
    }

    public ModelConfigDao getModelConfigDao() {
        return modelConfigDao;
    }

    public PluginDao getPluginDao() {
        return pluginDao;
    }

    public GestureDao getGestureDao() {
        return gestureDao;
    }

    public RuleDao getRuleDao() {
        return ruleDao;
    }

    public RawGestureDataDao getRawGestureDataDao() {
        return rawGestureDataDao;
    }

    public TrainingDatasetDao getTrainingDatasetDao() {
        return trainingDatasetDao;
    }

    public TrainingRelationDao getTrainingRelationDao() {
        return trainingRelationDao;
    }

    public KeyEventDao getKeyEventDao() {
        return keyEventDao;
    }

    public SwipeEventDao getSwipeEventDao() {
        return swipeEventDao;
    }

    public PlaybackEventDao getPlaybackEventDao() {
        return playbackEventDao;
    }

}
