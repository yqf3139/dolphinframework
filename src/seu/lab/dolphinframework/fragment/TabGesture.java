package seu.lab.dolphinframework.fragment;

import java.util.List;

import seu.lab.dolphin.dao.Gesture;
import seu.lab.dolphin.server.DaoManager;
import android.app.ListFragment;  
import android.os.Bundle;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import seu.lab.dolphinframework.R;

public class TabGesture extends ListFragment {  
  
    private TabGestureAdapter adapter = null;
    public static List<Gesture> gestures = null;
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
          
        adapter = new TabGestureAdapter(getActivity());  
        setListAdapter(adapter);
        gestures = DaoManager.getDaoManager(getActivity()).listAllGestures();
        adapter.add(gestures);
         
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View v = inflater.inflate(R.layout.gesture, container, false);  
        return v;  
    }
}  
