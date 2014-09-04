package seu.lab.dolphin.dao;

import seu.lab.dolphin.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TRAINING_RELATION.
 */
public class TrainingRelation {

    private Long id;
    private Long training_dataset_id;
    private Long gesture_id;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TrainingRelationDao myDao;

    private TrainingDataset trainingDataset;
    private Long trainingDataset__resolvedKey;

    private Gesture gesture;
    private Long gesture__resolvedKey;


    public TrainingRelation() {
    }

    public TrainingRelation(Long id) {
        this.id = id;
    }

    public TrainingRelation(Long id, Long training_dataset_id, Long gesture_id) {
        this.id = id;
        this.training_dataset_id = training_dataset_id;
        this.gesture_id = gesture_id;
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

    public Long getTraining_dataset_id() {
        return training_dataset_id;
    }

    public void setTraining_dataset_id(Long training_dataset_id) {
        this.training_dataset_id = training_dataset_id;
    }

    public Long getGesture_id() {
        return gesture_id;
    }

    public void setGesture_id(Long gesture_id) {
        this.gesture_id = gesture_id;
    }

    /** To-one relationship, resolved on first access. */
    public TrainingDataset getTrainingDataset() {
        Long __key = this.training_dataset_id;
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
        synchronized (this) {
            this.trainingDataset = trainingDataset;
            training_dataset_id = trainingDataset == null ? null : trainingDataset.getId();
            trainingDataset__resolvedKey = training_dataset_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Gesture getGesture() {
        Long __key = this.gesture_id;
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
        synchronized (this) {
            this.gesture = gesture;
            gesture_id = gesture == null ? null : gesture.getId();
            gesture__resolvedKey = gesture_id;
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