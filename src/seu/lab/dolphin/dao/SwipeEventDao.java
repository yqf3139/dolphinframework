package seu.lab.dolphin.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import seu.lab.dolphin.dao.SwipeEvent;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SWIPE_EVENT.
*/
public class SwipeEventDao extends AbstractDao<SwipeEvent, Long> {

    public static final String TABLENAME = "SWIPE_EVENT";

    /**
     * Properties of entity SwipeEvent.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "swipe_event_id");
        public final static Property X1 = new Property(1, int.class, "x1", false, "X1");
        public final static Property Y1 = new Property(2, int.class, "y1", false, "Y1");
        public final static Property X2 = new Property(3, int.class, "x2", false, "X2");
        public final static Property Y2 = new Property(4, int.class, "y2", false, "Y2");
    };


    public SwipeEventDao(DaoConfig config) {
        super(config);
    }
    
    public SwipeEventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SWIPE_EVENT' (" + //
                "'swipe_event_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'X1' INTEGER NOT NULL ," + // 1: x1
                "'Y1' INTEGER NOT NULL ," + // 2: y1
                "'X2' INTEGER NOT NULL ," + // 3: x2
                "'Y2' INTEGER NOT NULL );"); // 4: y2
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SWIPE_EVENT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SwipeEvent entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getX1());
        stmt.bindLong(3, entity.getY1());
        stmt.bindLong(4, entity.getX2());
        stmt.bindLong(5, entity.getY2());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SwipeEvent readEntity(Cursor cursor, int offset) {
        SwipeEvent entity = new SwipeEvent( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // x1
            cursor.getInt(offset + 2), // y1
            cursor.getInt(offset + 3), // x2
            cursor.getInt(offset + 4) // y2
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SwipeEvent entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setX1(cursor.getInt(offset + 1));
        entity.setY1(cursor.getInt(offset + 2));
        entity.setX2(cursor.getInt(offset + 3));
        entity.setY2(cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SwipeEvent entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SwipeEvent entity) {
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
    
}