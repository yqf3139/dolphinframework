package seu.lab.dolphin.dao;

import java.util.List;
import seu.lab.dolphin.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PLUGIN.
 */
public class Plugin {

    private Long id;
    /** Not-null value. */
    private String name;
    private int plugin_type;
    /** Not-null value. */
    private String discription;
    /** Not-null value. */
    private String icon_path;
    private long dolphin_context_id;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PluginDao myDao;

    private DolphinContext dolphinContext;
    private Long dolphinContext__resolvedKey;

    private List<Rule> rules;

    public Plugin() {
    }

    public Plugin(Long id) {
        this.id = id;
    }

    public Plugin(Long id, String name, int plugin_type, String discription, String icon_path, long dolphin_context_id) {
        this.id = id;
        this.name = name;
        this.plugin_type = plugin_type;
        this.discription = discription;
        this.icon_path = icon_path;
        this.dolphin_context_id = dolphin_context_id;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPluginDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public int getPlugin_type() {
        return plugin_type;
    }

    public void setPlugin_type(int plugin_type) {
        this.plugin_type = plugin_type;
    }

    /** Not-null value. */
    public String getDiscription() {
        return discription;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDiscription(String discription) {
        this.discription = discription;
    }

    /** Not-null value. */
    public String getIcon_path() {
        return icon_path;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
    }

    public long getDolphin_context_id() {
        return dolphin_context_id;
    }

    public void setDolphin_context_id(long dolphin_context_id) {
        this.dolphin_context_id = dolphin_context_id;
    }

    /** To-one relationship, resolved on first access. */
    public DolphinContext getDolphinContext() {
        long __key = this.dolphin_context_id;
        if (dolphinContext__resolvedKey == null || !dolphinContext__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DolphinContextDao targetDao = daoSession.getDolphinContextDao();
            DolphinContext dolphinContextNew = targetDao.load(__key);
            synchronized (this) {
                dolphinContext = dolphinContextNew;
            	dolphinContext__resolvedKey = __key;
            }
        }
        return dolphinContext;
    }

    public void setDolphinContext(DolphinContext dolphinContext) {
        if (dolphinContext == null) {
            throw new DaoException("To-one property 'dolphin_context_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.dolphinContext = dolphinContext;
            dolphin_context_id = dolphinContext.getId();
            dolphinContext__resolvedKey = dolphin_context_id;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Rule> getRules() {
        if (rules == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RuleDao targetDao = daoSession.getRuleDao();
            List<Rule> rulesNew = targetDao._queryPlugin_Rules(id);
            synchronized (this) {
                if(rules == null) {
                    rules = rulesNew;
                }
            }
        }
        return rules;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetRules() {
        rules = null;
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
