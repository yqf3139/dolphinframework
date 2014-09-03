package seu.lab.dolphin.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import seu.lab.dolphin.dao.DolphinContext;
import seu.lab.dolphin.dao.Rule;
import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.KeyEvent;
import seu.lab.dolphin.dao.SwipeEvent;
import seu.lab.dolphin.dao.PlaybackEvent;
import seu.lab.dolphin.dao.RawGestureData;
import seu.lab.dolphin.dao.TrainingDataset;
import seu.lab.dolphin.dao.Model;
import seu.lab.dolphin.dao.TrainingRelation;

import seu.lab.dolphin.dao.DolphinContextDao;
import seu.lab.dolphin.dao.RuleDao;
import seu.lab.dolphin.dao.PluginDao;
import seu.lab.dolphin.dao.GestureDao;
import seu.lab.dolphin.dao.KeyEventDao;
import seu.lab.dolphin.dao.SwipeEventDao;
import seu.lab.dolphin.dao.PlaybackEventDao;
import seu.lab.dolphin.dao.RawGestureDataDao;
import seu.lab.dolphin.dao.TrainingDatasetDao;
import seu.lab.dolphin.dao.ModelDao;
import seu.lab.dolphin.dao.TrainingRelationDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dolphinContextDaoConfig;
    private final DaoConfig ruleDaoConfig;
    private final DaoConfig pluginDaoConfig;
    private final DaoConfig gestureDaoConfig;
    private final DaoConfig keyEventDaoConfig;
    private final DaoConfig swipeEventDaoConfig;
    private final DaoConfig playbackEventDaoConfig;
    private final DaoConfig rawGestureDataDaoConfig;
    private final DaoConfig trainingDatasetDaoConfig;
    private final DaoConfig modelDaoConfig;
    private final DaoConfig trainingRelationDaoConfig;

    private final DolphinContextDao dolphinContextDao;
    private final RuleDao ruleDao;
    private final PluginDao pluginDao;
    private final GestureDao gestureDao;
    private final KeyEventDao keyEventDao;
    private final SwipeEventDao swipeEventDao;
    private final PlaybackEventDao playbackEventDao;
    private final RawGestureDataDao rawGestureDataDao;
    private final TrainingDatasetDao trainingDatasetDao;
    private final ModelDao modelDao;
    private final TrainingRelationDao trainingRelationDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        dolphinContextDaoConfig = daoConfigMap.get(DolphinContextDao.class).clone();
        dolphinContextDaoConfig.initIdentityScope(type);

        ruleDaoConfig = daoConfigMap.get(RuleDao.class).clone();
        ruleDaoConfig.initIdentityScope(type);

        pluginDaoConfig = daoConfigMap.get(PluginDao.class).clone();
        pluginDaoConfig.initIdentityScope(type);

        gestureDaoConfig = daoConfigMap.get(GestureDao.class).clone();
        gestureDaoConfig.initIdentityScope(type);

        keyEventDaoConfig = daoConfigMap.get(KeyEventDao.class).clone();
        keyEventDaoConfig.initIdentityScope(type);

        swipeEventDaoConfig = daoConfigMap.get(SwipeEventDao.class).clone();
        swipeEventDaoConfig.initIdentityScope(type);

        playbackEventDaoConfig = daoConfigMap.get(PlaybackEventDao.class).clone();
        playbackEventDaoConfig.initIdentityScope(type);

        rawGestureDataDaoConfig = daoConfigMap.get(RawGestureDataDao.class).clone();
        rawGestureDataDaoConfig.initIdentityScope(type);

        trainingDatasetDaoConfig = daoConfigMap.get(TrainingDatasetDao.class).clone();
        trainingDatasetDaoConfig.initIdentityScope(type);

        modelDaoConfig = daoConfigMap.get(ModelDao.class).clone();
        modelDaoConfig.initIdentityScope(type);

        trainingRelationDaoConfig = daoConfigMap.get(TrainingRelationDao.class).clone();
        trainingRelationDaoConfig.initIdentityScope(type);

        dolphinContextDao = new DolphinContextDao(dolphinContextDaoConfig, this);
        ruleDao = new RuleDao(ruleDaoConfig, this);
        pluginDao = new PluginDao(pluginDaoConfig, this);
        gestureDao = new GestureDao(gestureDaoConfig, this);
        keyEventDao = new KeyEventDao(keyEventDaoConfig, this);
        swipeEventDao = new SwipeEventDao(swipeEventDaoConfig, this);
        playbackEventDao = new PlaybackEventDao(playbackEventDaoConfig, this);
        rawGestureDataDao = new RawGestureDataDao(rawGestureDataDaoConfig, this);
        trainingDatasetDao = new TrainingDatasetDao(trainingDatasetDaoConfig, this);
        modelDao = new ModelDao(modelDaoConfig, this);
        trainingRelationDao = new TrainingRelationDao(trainingRelationDaoConfig, this);

        registerDao(DolphinContext.class, dolphinContextDao);
        registerDao(Rule.class, ruleDao);
        registerDao(Plugin.class, pluginDao);
        registerDao(Gesture.class, gestureDao);
        registerDao(KeyEvent.class, keyEventDao);
        registerDao(SwipeEvent.class, swipeEventDao);
        registerDao(PlaybackEvent.class, playbackEventDao);
        registerDao(RawGestureData.class, rawGestureDataDao);
        registerDao(TrainingDataset.class, trainingDatasetDao);
        registerDao(Model.class, modelDao);
        registerDao(TrainingRelation.class, trainingRelationDao);
    }
    
    public void clear() {
        dolphinContextDaoConfig.getIdentityScope().clear();
        ruleDaoConfig.getIdentityScope().clear();
        pluginDaoConfig.getIdentityScope().clear();
        gestureDaoConfig.getIdentityScope().clear();
        keyEventDaoConfig.getIdentityScope().clear();
        swipeEventDaoConfig.getIdentityScope().clear();
        playbackEventDaoConfig.getIdentityScope().clear();
        rawGestureDataDaoConfig.getIdentityScope().clear();
        trainingDatasetDaoConfig.getIdentityScope().clear();
        modelDaoConfig.getIdentityScope().clear();
        trainingRelationDaoConfig.getIdentityScope().clear();
    }

    public DolphinContextDao getDolphinContextDao() {
        return dolphinContextDao;
    }

    public RuleDao getRuleDao() {
        return ruleDao;
    }

    public PluginDao getPluginDao() {
        return pluginDao;
    }

    public GestureDao getGestureDao() {
        return gestureDao;
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

    public RawGestureDataDao getRawGestureDataDao() {
        return rawGestureDataDao;
    }

    public TrainingDatasetDao getTrainingDatasetDao() {
        return trainingDatasetDao;
    }

    public ModelDao getModelDao() {
        return modelDao;
    }

    public TrainingRelationDao getTrainingRelationDao() {
        return trainingRelationDao;
    }

}
