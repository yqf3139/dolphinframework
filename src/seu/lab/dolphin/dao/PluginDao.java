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

import seu.lab.dolphin.dao.Plugin;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PLUGIN.
*/
public class PluginDao extends AbstractDao<Plugin, Long> {

    public static final String TABLENAME = "PLUGIN";

    /**
     * Properties of entity Plugin.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "plugin_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Plugin_type = new Property(2, int.class, "plugin_type", false, "PLUGIN_TYPE");
        public final static Property Discription = new Property(3, String.class, "discription", false, "DISCRIPTION");
        public final static Property Icon_path = new Property(4, String.class, "icon_path", false, "ICON_PATH");
        public final static Property Dolphin_context_id = new Property(5, long.class, "dolphin_context_id", false, "DOLPHIN_CONTEXT_ID");
    };

    private DaoSession daoSession;


    public PluginDao(DaoConfig config) {
        super(config);
    }
    
    public PluginDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PLUGIN' (" + //
                "'plugin_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'NAME' TEXT NOT NULL ," + // 1: name
                "'PLUGIN_TYPE' INTEGER NOT NULL ," + // 2: plugin_type
                "'DISCRIPTION' TEXT NOT NULL ," + // 3: discription
                "'ICON_PATH' TEXT NOT NULL ," + // 4: icon_path
                "'DOLPHIN_CONTEXT_ID' INTEGER NOT NULL );"); // 5: dolphin_context_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PLUGIN'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Plugin entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindLong(3, entity.getPlugin_type());
        stmt.bindString(4, entity.getDiscription());
        stmt.bindString(5, entity.getIcon_path());
        stmt.bindLong(6, entity.getDolphin_context_id());
    }

    @Override
    protected void attachEntity(Plugin entity) {
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
    public Plugin readEntity(Cursor cursor, int offset) {
        Plugin entity = new Plugin( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.getInt(offset + 2), // plugin_type
            cursor.getString(offset + 3), // discription
            cursor.getString(offset + 4), // icon_path
            cursor.getLong(offset + 5) // dolphin_context_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Plugin entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setPlugin_type(cursor.getInt(offset + 2));
        entity.setDiscription(cursor.getString(offset + 3));
        entity.setIcon_path(cursor.getString(offset + 4));
        entity.setDolphin_context_id(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Plugin entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Plugin entity) {
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
            builder.append(" FROM PLUGIN T");
            builder.append(" LEFT JOIN DOLPHIN_CONTEXT T0 ON T.'DOLPHIN_CONTEXT_ID'=T0.'dolphin_context_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Plugin loadCurrentDeep(Cursor cursor, boolean lock) {
        Plugin entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DolphinContext dolphinContext = loadCurrentOther(daoSession.getDolphinContextDao(), cursor, offset);
         if(dolphinContext != null) {
            entity.setDolphinContext(dolphinContext);
        }

        return entity;    
    }

    public Plugin loadDeep(Long key) {
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
    public List<Plugin> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Plugin> list = new ArrayList<Plugin>(count);
        
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
    
    protected List<Plugin> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Plugin> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
