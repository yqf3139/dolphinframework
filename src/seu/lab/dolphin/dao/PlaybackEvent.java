package seu.lab.dolphin.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table PLAYBACK_EVENT.
 */
public class PlaybackEvent {

    private Long id;
    /** Not-null value. */
    private String name;
    /** Not-null value. */
    private String script_name;

    public PlaybackEvent() {
    }

    public PlaybackEvent(Long id) {
        this.id = id;
    }

    public PlaybackEvent(Long id, String name, String script_name) {
        this.id = id;
        this.name = name;
        this.script_name = script_name;
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

    /** Not-null value. */
    public String getScript_name() {
        return script_name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setScript_name(String script_name) {
        this.script_name = script_name;
    }

}
