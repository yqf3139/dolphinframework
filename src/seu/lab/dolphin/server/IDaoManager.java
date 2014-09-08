package seu.lab.dolphin.server;

import java.util.List;

import android.R.integer;

import seu.lab.dolphin.dao.DolphinContext;
import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.dao.KeyEvent;
import seu.lab.dolphin.dao.PlaybackEvent;
import seu.lab.dolphin.dao.Plugin;
import seu.lab.dolphin.dao.RawGestureData;
import seu.lab.dolphin.dao.Rule;
import seu.lab.dolphin.dao.SwipeEvent;

public interface IDaoManager {

	List<Gesture> listAllGestures();
	List<Plugin> listAllPlugins();
	List<Gesture> listGesturesAvailble(Plugin plugin);
	List<KeyEvent> listAllKeyEvents();
	List<SwipeEvent> listAllSwipeEvents();
	List<PlaybackEvent> listPlaybackEventsRecord(Plugin plugin);
	
	long addPlaybackEvent(PlaybackEvent playbackEvent);
	long addRule(Rule rule);
	boolean addDolphinContextAndPlugin(DolphinContext dolphinContext, Plugin plugin);
	
	boolean deletePlaybackEvent(PlaybackEvent playbackEvent);
	boolean deleteRule(Rule rule);
	boolean deleteDolphinContextAndPlugin(Plugin plugin);
	
	boolean updatePlugin(Plugin plugin);
	boolean updateRuleWithoutGestureChanged(Rule rule);
	boolean updateRuleWithGestureChanged(Rule rule);

	long addRawGestureData(RawGestureData rawGestureData);
	long countGestureRawData(Gesture gesture);
}
