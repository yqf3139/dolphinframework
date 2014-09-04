package seu.lab.dolphin.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import seu.lab.dolphin.dao.ModelConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MODEL_CONFIG.
*/
public class ModelConfigDao extends AbstractDao<ModelConfig, Long> {

    public static final String TABLENAME = "MODEL_CONFIG";

    /**
     * Properties of entity ModelConfig.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "model_config");
        public final static Property Masks = new Property(1, String.class, "masks", false, "MASKS");
        public final static Property Models = new Property(2, String.class, "models", false, "MODELS");
        public final static Property Dolphin_context_id = new Property(3, Long.class, "dolphin_context_id", false, "DOLPHIN_CONTEXT_ID");
    };

    private DaoSession daoSession;


    public ModelConfigDao(DaoConfig config) {
        super(config);
    }
    
    public ModelConfigDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MODEL_CONFIG' (" + //
                "'model_config' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'MASKS' TEXT NOT NULL ," + // 1: masks
                "'MODELS' TEXT NOT NULL ," + // 2: models
                "'DOLPHIN_CONTEXT_ID' INTEGER);"); // 3: dolphin_context_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MODEL_CONFIG'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ModelConfig entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getMasks());
        stmt.bindString(3, entity.getModels());
 
        Long dolphin_context_id = entity.getDolphin_context_id();
        if (dolphin_context_id != null) {
            stmt.bindLong(4, dolphin_context_id);
        }
    }

    @Override
    protected void attachEntity(ModelConfig entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ModelConfig readEntity(Cursor cursor, int offset) {
        ModelConfig entity = new ModelConfig( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // masks
            cursor.getString(offset + 2), // models
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // dolphin_context_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ModelConfig entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMasks(cursor.getString(offset + 1));
        entity.setModels(cursor.getString(offset + 2));
        entity.setDolphin_context_id(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ModelConfig entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ModelConfig entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDolphinContextDao().getAllColumns());
            builder.append(" FROM MODEL_CONFIG T");
            builder.append(" LEFT JOIN DOLPHIN_CONTEXT T0 ON T.'DOLPHIN_CONTEXT_ID'=T0.'dolphin_context_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ModelConfig loadCurrentDeep(Cursor cursor, boolean lock) {
        ModelConfig entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DolphinContext dolphinContext = loadCurrentOther(daoSession.getDolphinContextDao(), cursor, offset);
        entity.setDolphinContext(dolphinContext);

        return entity;    
    }

    public ModelConfig loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ModelConfig> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ModelConfig> list = new ArrayList<ModelConfig>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ModelConfig> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ModelConfig> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
