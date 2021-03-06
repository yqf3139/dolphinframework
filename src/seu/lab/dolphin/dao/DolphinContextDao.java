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

import seu.lab.dolphin.dao.DolphinContext;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DOLPHIN_CONTEXT.
*/
public class DolphinContextDao extends AbstractDao<DolphinContext, Long> {

    public static final String TABLENAME = "DOLPHIN_CONTEXT";

    /**
     * Properties of entity DolphinContext.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "dolphin_context_id");
        public final static Property Activity_name = new Property(1, String.class, "activity_name", false, "ACTIVITY_NAME");
        public final static Property Device_state = new Property(2, String.class, "device_state", false, "DEVICE_STATE");
        public final static Property Model_config_id = new Property(3, Long.class, "model_config_id", false, "MODEL_CONFIG_ID");
        public final static Property Plugin_id = new Property(4, long.class, "plugin_id", false, "PLUGIN_ID");
    };

    private DaoSession daoSession;


    public DolphinContextDao(DaoConfig config) {
        super(config);
    }
    
    public DolphinContextDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DOLPHIN_CONTEXT' (" + //
                "'dolphin_context_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'ACTIVITY_NAME' TEXT NOT NULL ," + // 1: activity_name
                "'DEVICE_STATE' TEXT NOT NULL ," + // 2: device_state
                "'MODEL_CONFIG_ID' INTEGER," + // 3: model_config_id
                "'PLUGIN_ID' INTEGER NOT NULL );"); // 4: plugin_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DOLPHIN_CONTEXT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DolphinContext entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getActivity_name());
        stmt.bindString(3, entity.getDevice_state());
 
        Long model_config_id = entity.getModel_config_id();
        if (model_config_id != null) {
            stmt.bindLong(4, model_config_id);
        }
        stmt.bindLong(5, entity.getPlugin_id());
    }

    @Override
    protected void attachEntity(DolphinContext entity) {
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
    public DolphinContext readEntity(Cursor cursor, int offset) {
        DolphinContext entity = new DolphinContext( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // activity_name
            cursor.getString(offset + 2), // device_state
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // model_config_id
            cursor.getLong(offset + 4) // plugin_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DolphinContext entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setActivity_name(cursor.getString(offset + 1));
        entity.setDevice_state(cursor.getString(offset + 2));
        entity.setModel_config_id(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setPlugin_id(cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DolphinContext entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DolphinContext entity) {
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
            SqlUtils.appendColumns(builder, "T0", daoSession.getModelConfigDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getPluginDao().getAllColumns());
            builder.append(" FROM DOLPHIN_CONTEXT T");
            builder.append(" LEFT JOIN MODEL_CONFIG T0 ON T.'MODEL_CONFIG_ID'=T0.'model_config'");
            builder.append(" LEFT JOIN PLUGIN T1 ON T.'PLUGIN_ID'=T1.'plugin_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DolphinContext loadCurrentDeep(Cursor cursor, boolean lock) {
        DolphinContext entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        ModelConfig modelConfig = loadCurrentOther(daoSession.getModelConfigDao(), cursor, offset);
        entity.setModelConfig(modelConfig);
        offset += daoSession.getModelConfigDao().getAllColumns().length;

        Plugin plugin = loadCurrentOther(daoSession.getPluginDao(), cursor, offset);
         if(plugin != null) {
            entity.setPlugin(plugin);
        }

        return entity;    
    }

    public DolphinContext loadDeep(Long key) {
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
    public List<DolphinContext> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DolphinContext> list = new ArrayList<DolphinContext>(count);
        
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
    
    protected List<DolphinContext> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DolphinContext> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
