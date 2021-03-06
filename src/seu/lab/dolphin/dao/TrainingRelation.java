package seu.lab.dolphin.dao;

import seu.lab.dolphin.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TRAINING_RELATION.
 */
public class TrainingRelation {

    private Long id;
    private long gesture_id;
    private long traing_data_set_id;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TrainingRelationDao myDao;

    private Gesture gesture;
    private Long gesture__resolvedKey;

    private TrainingDataset trainingDataset;
    private Long trainingDataset__resolvedKey;


    public TrainingRelation() {
    }

    public TrainingRelation(Long id) {
        this.id = id;
    }

    public TrainingRelation(Long id, long gesture_id, long traing_data_set_id) {
        this.id = id;
        this.gesture_id = gesture_id;
        this.traing_data_set_id = traing_data_set_id;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTrainingRelationDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getGesture_id() {
        return gesture_id;
    }

    public void setGesture_id(long gesture_id) {
        this.gesture_id = gesture_id;
    }

    public long getTraing_data_set_id() {
        return traing_data_set_id;
    }

    public void setTraing_data_set_id(long traing_data_set_id) {
        this.traing_data_set_id = traing_data_set_id;
    }

    /** To-one relationship, resolved on first access. */
    public Gesture getGesture() {
        long __key = this.gesture_id;
        if (gesture__resolvedKey == null || !gesture__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GestureDao targetDao = daoSession.getGestureDao();
            Gesture gestureNew = targetDao.load(__key);
            synchronized (this) {
                gesture = gestureNew;
            	gesture__resolvedKey = __key;
            }
        }
        return gesture;
    }

    public void setGesture(Gesture gesture) {
        if (gesture == null) {
            throw new DaoException("To-one property 'gesture_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.gesture = gesture;
            gesture_id = gesture.getId();
            gesture__resolvedKey = gesture_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public TrainingDataset getTrainingDataset() {
        long __key = this.traing_data_set_id;
        if (trainingDataset__resolvedKey == null || !trainingDataset__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TrainingDatasetDao targetDao = daoSession.getTrainingDatasetDao();
            TrainingDataset trainingDatasetNew = targetDao.load(__key);
            synchronized (this) {
                trainingDataset = trainingDatasetNew;
            	trainingDataset__resolvedKey = __key;
            }
        }
        return trainingDataset;
    }

    public void setTrainingDataset(TrainingDataset trainingDataset) {
        if (trainingDataset == null) {
            throw new DaoException("To-one property 'traing_data_set_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.trainingDataset = trainingDataset;
            traing_data_set_id = trainingDataset.getId();
            trainingDataset__resolvedKey = traing_data_set_id;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
